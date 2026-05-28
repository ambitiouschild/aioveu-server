package com.aioveu.pay.aioveu12MqProducerPayment.service.impl;


import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.rabbitmq.constant.PayCommonMqConstantWithBizName;
import com.aioveu.common.rabbitmq.enums.SendStatus;
import com.aioveu.common.rabbitmq.producer.monitor.ProducerMetricsCollector;
import com.aioveu.common.rabbitmq.producer.monitor.ProducerMonitor;
import com.aioveu.pay.aioveu01.enums.PaymentStatusEnum;
import com.aioveu.pay.aioveu01PayOrder.mapper.PayOrderMapper;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu10MqSendRecord.mapper.MqSendRecordMapper;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.aioveu.common.rabbitmq.producer.util.MessageIdGenerator;
import com.aioveu.pay.aioveu12MqProducerPayment.enums.MessageQueueTypeEnum;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitBatchSendResult;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendRequest;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendResult;
import com.aioveu.common.rabbitmq.producer.model.vo.SendContext;
import com.aioveu.common.rabbitmq.producer.model.payment.MessageSendException;
import com.aioveu.common.rabbitmq.producer.model.payment.PaymentFailedMessage;
import com.aioveu.common.rabbitmq.producer.model.payment.PaymentSuccessMessage;
import com.aioveu.pay.aioveu12MqProducerPayment.enums.PaymentMqBizType;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.SendPaymentMqDTO;
import com.aioveu.pay.aioveu12MqProducerPayment.service.PayCommonMessageProducerService;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @ClassName: MqSendRecordServiceImpl
 * @Description TODO RabbitMQ专属消息发送服务
 *                    消息发送服务（使用自定义Request）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:48
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PayCommonMessageProducerServiceImpl extends ServiceImpl<MqSendRecordMapper, MqSendRecord> implements PayCommonMessageProducerService {


    private final MessageIdGenerator messageIdGenerator;
    private final MqSendRecordMapper mqSendRecordMapper;
    private final MqSendRecordService mqSendRecordService;
    private final ProducerMonitor producerMonitor;
    private final ProducerMetricsCollector metricsCollector;

    private final AdapterMessageBuilderImpl adapterMessageBuilderImpl;

    private final PayOrderMapper payOrderMapper;


    //正确做法：使用公共常量
    private final String paymentExchange = PayCommonMqConstantWithBizName.Exchange.PAYMENT;
    private final String paymentSuccessRoutingKey = PayCommonMqConstantWithBizName.RoutingKey.SUCCESS;
    private final String paymentFailedRoutingKey = PayCommonMqConstantWithBizName.RoutingKey.FAILED;




    // 存储消息回调上下文，用于重试
    private final Map<String, SendContext> sendContextMap = new ConcurrentHashMap<>();


    /**
     * 发送支付成功消息
     */
    @Override
    public boolean sendPaymentSuccessMessage(SendPaymentMqDTO dto) {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        String messageId = null;
        String errorMsg = null;

        PayOrder payOrder = payOrderMapper.getPayOrderByNo(dto.getPaymentNo());
        if (payOrder == null) {
            throw new BusinessException("支付订单不存在:{}",dto.getPaymentNo());
        }


        // ✅ 补上这两行
        // ✅ 业务类型
        dto.setBizTypeEnum(PaymentMqBizType.PAYMENT_SUCCESS);


        // ✅ 人工发送：全部强制从订单来
        if (Boolean.TRUE.equals(dto.getManualSend())) {

        // ✅ 金额兜底
            BigDecimal amount = Optional.ofNullable(payOrder.getPaymentAmount())
                    .filter(a -> a.compareTo(BigDecimal.ZERO) > 0)
                    .orElse(new BigDecimal("0.01"));

            dto.setPaymentAmount(amount);
            log.info("金额兜底amount=:{}",amount);

            // 交易号（没有就 mock）
            dto.setTransactionId(
                    StringUtils.isNotBlank(payOrder.getThirdPaymentNo())
                            ? payOrder.getThirdTransactionNo()
                            : "MANUAL_" + dto.getPaymentNo() + "_" + System.currentTimeMillis()
            );

            // 渠道
            dto.setChannel(
                    StringUtils.isNotBlank(payOrder.getPaymentChannel())
                            ? payOrder.getPaymentChannel()
                            : "manual"
            );

            // 支付时间
            dto.setPaymentTime(LocalDateTime.now());
        }

        // ✅ transactionId 处理（核心）
        String transactionId = resolveTransactionId(dto);
        dto.setTransactionId(transactionId);




        try {

            // ✅ 判断 messageId
            messageId = dto.getMessageId();
            if (StringUtils.isBlank(messageId)) {
                messageId = messageIdGenerator.generatePaymentMessageId(payOrder.getPaymentNo());
                dto.setMessageId(messageId); // ✅ 这一行必须有  写入 DTO
                log.info("【Pay-mq】使用payOrder.getPaymentNo(),生成messageId:{}",messageId);
            }

            // 构建消息
            PaymentSuccessMessage message = buildPaymentSuccessMessage(payOrder, dto);
            //1️把 PaymentSuccessMessage设计成 “不可变、自解释”
            log.info("【Pay-mq】构建消息成功:{}",message);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                // 创建 RabbitSendRequest
            RabbitSendRequest request = RabbitSendRequest.builder()
                    .body(message)   // ✅ MQ 发送用（对象）
                    .messageId(dto.getMessageId()) // ✅ 同一个 构建 MQ Request（从 DTO / messageId 拿）
                    .exchange(paymentExchange)
                    .routingKey(paymentSuccessRoutingKey)
                    .messageType("PAYMENT_SUCCESS")
                    //根因是 Fastjson 不支持 LocalDateTime
                    //✅ 换 Jackson + JavaTimeModule，今天所有异常结束
                    .messageBody(objectMapper.writeValueAsString(message)) // ✅ 安全 // ✅ 落库用（字符串）
                    .persistent(true)
                    .priority(5)
                    .tenantId(payOrder.getTenantId())
                    .bizId(payOrder.getPaymentNo())
                    .bizType(message.getBizType())     // ✅ 从业务消息来  // ✅ 必填
                    .topic(message.getTopic())         // ✅ 从业务消息来  // ✅ 必填
                    .build();

            //2️把 RabbitSendRequest抽成 通用 MQ 发送模型
            log.info("【Pay-mq】构建mq成功, RabbitSendRequest 不做任何业务决策:{}",request);

            // 保存发送记录
            boolean saveMqSendRecord = saveMqSendRecord(request);
            if (!saveMqSendRecord) {
                log.error("【Pay-mq】保存发送记录失败, messageId={}", messageId);
                return false;
            }
            //3️把 MqSendRecord做成 完全从 Request 映射，零业务逻辑
            log.info("【Pay-mq】保存发送记录成功,saveMqSendRecord()—— 只负责落库:{}",saveMqSendRecord);





            // 发送消息
            try {
                RabbitSendResult result = sendSingleMessage(request);
                success = result != null && result.isSuccess();
            } catch (Exception e) {

                // ✅ 超时 ≠ 失败   这一版，不管你怎么包装异常，都能命中
                Throwable cause = e;
                while (cause != null) {
                    if (cause instanceof java.util.concurrent.TimeoutException) {
                        log.warn("MQ发送超时，但可能已送达, messageId={}", request.getMessageId());
                        success = true;
                        break;
                    }
                    cause = cause.getCause();
                }

                if (!success) {
                    errorMsg = e.getMessage();
                }
            }

            log.info("【Pay-mq】发送消息结果: success={}, messageId={}", success, messageId);


            //MyBatis 一级缓存 / 自动提交 / 多数据源，都可能导致“同一线程查不到”
            // ✅ 方案一：根本不查，直接 update（最稳）更新发送状态
            mqSendRecordService.updateSendStatus(messageId,
                    success ? SendStatus.SUCCESS : SendStatus.FAILED,
                    success ? null : errorMsg
            );

            if (success) {
                log.info("【MQ发送】支付成功消息发送成功: paymentNo={}, messageId={}",
                        payOrder.getPaymentNo(), messageId);
            } else {
                log.error("消息发送失败: status={}", messageId);
            }

            return success;

        } catch (Exception e) {
            log.error("发送支付成功消息异常: paymentNo={}", payOrder.getPaymentNo(), e);
            // 可以记录到补偿表，定时任务重试

            // 记录发送失败，后续补偿任务会重试
            mqSendRecordService.updateSendStatus(
                    messageId,
                    SendStatus.FAILED,
                    e.getMessage()
            );
            return false;

        }finally {
            // 记录发送结果
            producerMonitor.recordSendResult(success);
            long costTime = System.currentTimeMillis() - startTime;
            // ✅ 使用 ProducerMetricsCollector 记录指标
            metricsCollector.recordSendResult(success, costTime);
        }

    }


    /**
     * 发送支付失败消息
     */
    @Override
    public boolean sendPaymentFailedMessage(SendPaymentMqDTO dto) {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        String errorMsg = null;

        PayOrder payOrder = payOrderMapper.getPayOrderByNo(dto.getPaymentNo());
        if (payOrder == null) {
            throw new BusinessException("支付订单不存在");
        }

        //✅ 成功 / 失败用同一个生成规则
        String messageId = dto.getMessageId();
        if (StringUtils.isBlank(messageId)) {
            messageId = messageIdGenerator.generatePaymentMessageId(payOrder.getPaymentNo());
            dto.setMessageId(messageId); // ✅ 这一行必须有
            log.info("【Pay-mq】使用payOrder.getPaymentNo(),生成messageId:{}", messageId);
        }

        try {

            // 构建消息
            PaymentFailedMessage message = buildPaymentFailedMessage(payOrder, dto);

            // 创建 RabbitSendRequest
            RabbitSendRequest request = RabbitSendRequest.builder()
                    .body(message)
                    .messageId(dto.getMessageId()) // ✅ 同一个 构建 MQ Request（从 DTO / messageId 拿）
                    .exchange(paymentExchange)
                    .routingKey(paymentFailedRoutingKey)
                    .messageType("PAYMENT_FAILED")
                    .persistent(true)
                    .priority(3)
                    .tenantId(payOrder.getTenantId())
                    .bizId(payOrder.getPaymentNo())
                    .build();

            // 保存发送记录
            boolean saveMqSendRecord = saveMqSendRecord(request);

            // 发送消息
            try {
                RabbitSendResult result = sendSingleMessage(request);
                success = result != null && result.isSuccess();
            } catch (Exception e) {

                if (e instanceof java.util.concurrent.TimeoutException) {
                    log.warn("MQ发送超时，但可能已送达, messageId={}", request.getMessageId());
                    success = true;
                } else {
                    success = false;
                    errorMsg = e.getMessage();
                }
            }

            // 更新发送状态
            mqSendRecordService.updateSendStatus(messageId,
                    success ? SendStatus.SUCCESS : SendStatus.FAILED,
                    success ? null : errorMsg
            );

            if (success) {
                log.info("【RabbitMQ发送】支付失败消息发送成功: paymentNo={}, messageId={}",
                        payOrder.getPaymentNo(), messageId);
            }

            return success;

        } catch (Exception e) {
            log.error("发送支付失败消息异常: paymentNo={}", payOrder.getPaymentNo(), e);

            if (messageId != null) {
                mqSendRecordService.updateSendStatus(messageId, SendStatus.FAILED, e.getMessage());
            }
            return false;

        } finally {
            // 记录发送结果
            producerMonitor.recordSendResult(success);
            long costTime = System.currentTimeMillis() - startTime;
            metricsCollector.recordSendResult(success, costTime);
        }
    }

    /**
     * 批量发送消息
     */
    @Override
    public RabbitBatchSendResult batchSend(List<PaymentSuccessMessage> messages) {
        RabbitBatchSendResult result = new RabbitBatchSendResult();
        long totalStartTime = System.currentTimeMillis();

        for (PaymentSuccessMessage message : messages) {
            long startTime = System.currentTimeMillis();
            boolean success = false;

            String messageId = messageIdGenerator.generatePaymentMessageId(message.getPaymentNo());

            try {

                // 创建 RabbitSendRequest
                RabbitSendRequest request = RabbitSendRequest.builder()
                        .body(message)
                        .messageId(messageId)
                        .exchange(paymentExchange)
                        .routingKey(paymentSuccessRoutingKey)
                        .messageType("PAYMENT_SUCCESS")
                        .persistent(true)
                        .priority(5)
                        .build();


                // 发送单条消息
                try {
                    RabbitSendResult sendResult = sendSingleMessage(request);
                    success = result != null && sendResult.isSuccess();

                    // 更新统计
                    if (success) {
                        result.incrementSuccess();
                    } else {
                        result.incrementFailed();
                        result.addFailedMessage(request.getMessageId(), sendResult.getErrorMessage());
                    }

                } catch (Exception e) {

                    // ✅ Timeout 不算失败
                    if (e instanceof java.util.concurrent.TimeoutException) {
                        log.warn("MQ发送超时，但可能已送达, messageId={}", request.getMessageId());
                        success = true; // ✅ 或单独状态
                    } else {
                        success = false;
                    }
                }



            }catch (Exception e) {
                result.incrementFailed();
                result.addFailedMessage(message.getMessageId(), e.getMessage());
                log.warn("批量发送消息失败: paymentNo={}", message.getPaymentNo(), e);

            } finally {
                long costTime = System.currentTimeMillis() - startTime;
                // ✅ 记录每条消息的发送指标
                metricsCollector.recordSendResult(success, costTime);
            }
        }

        long totalCostTime = System.currentTimeMillis() - totalStartTime;
        // ✅ 记录批量发送的整体指标
        metricsCollector.recordSendResult(result.getFailedCount() == 0, totalCostTime);

        return result;
    }


    /**
     * 发送单条消息（完整功能版）
     *
     * @param request 发送请求
     * @return 发送结果
     */
    @Override
    public RabbitSendResult sendSingleMessage(RabbitSendRequest request) {
        long startTime = System.currentTimeMillis();
        String messageId = request.getMessageId();

        if (StringUtils.isBlank(messageId)) {
            messageId = messageIdGenerator.generateMessageId();
            request.setMessageId(messageId);
        }

        try {
            // 1. 参数校验
            adapterMessageBuilderImpl.validateRequest(request);

            // 3. 根据配置选择MQ
            MessageQueueTypeEnum queueType = adapterMessageBuilderImpl.determineQueueType(request);


            // 3. 发送消息
            RabbitSendResult sendResult = (RabbitSendResult)adapterMessageBuilderImpl.doSend(queueType, request);

            // 5. 记录成功日志 已经记录
//            adapterMessageBuilderImpl.logSendSuccess(request, sendResult, startTime);

            return sendResult;

        } catch (Exception e) {
            // 6. 记录失败日志
            adapterMessageBuilderImpl.logSendFailure(request, messageId, e, startTime);
            // ✅ 区分超时和其他异常
            Throwable cause = e;
            while (cause != null) {
                if (cause instanceof java.util.concurrent.TimeoutException) {
                    throw new MessageSendException(
                            "MQ发送超时，可能已送达",
                            e,
                            messageId
                    );
                }
                cause = cause.getCause();
            }
            throw new MessageSendException("消息发送失败", e, messageId);
        }
    }

    /**
     * 构建支付成功消息
     */


    private PaymentSuccessMessage buildPaymentSuccessMessage(PayOrder payOrder, SendPaymentMqDTO dto) {

        String bizType = dto.getBizTypeEnum().getBizType();
        String topic =  dto.getBizTypeEnum().getTopic();

        log.info("构建支付成功消息:bizType:{},topic:{}",bizType,topic);


        return PaymentSuccessMessage.builder()
                // ✅ MQ 发送相关（来自 DTO）
                .messageId(dto.getMessageId())    // 构建业务消息（从 DTO 拿）
                .bizType(dto.getBizTypeEnum().getBizType())     // ✅ 从业务消息来
                .topic(dto.getBizTypeEnum().getTopic())         // ✅ 从业务消息来

                // ✅ 支付事实（来自 PayOrder）
                .paymentNo(payOrder.getPaymentNo())
                .orderNo(payOrder.getOrderNo())
                .tenantId(payOrder.getTenantId())   // ✅ 从 PayOrder 取
                .transactionId(dto.getTransactionId())  // ✅ 可能 mock
                .amount(dto.getPaymentAmount())   // ✅ 已兜底
                .channel(payOrder.getPaymentChannel())
                .paymentTime(payOrder.getPaymentTime()) // ✅ 关键
                .memberId(payOrder.getUserId())
                .build();
    }

    /**
     * 构建支付失败消息
     */
    private PaymentFailedMessage buildPaymentFailedMessage(PayOrder payOrder,SendPaymentMqDTO dto) {
        return PaymentFailedMessage.builder()
                .messageId(dto.getMessageId())
                .tenantId(payOrder.getTenantId())
                .paymentNo(payOrder.getPaymentNo())
                .orderNo(payOrder.getOrderNo())
                .amount(payOrder.getPaymentAmount())
                .channel(payOrder.getPaymentChannel())
                .bizType("PAY")
                .paymentStatus(PaymentStatusEnum.FAILED.getValue())
//                .errorCode(params.get("err_code"))
//                .errorMsg(params.get("err_code_des"))
                .retryable(true)
                .failTime(LocalDateTime.now())
                .build();
    }


    /**
     * 保存消息发送记录
     */
    private Boolean saveMqSendRecord(RabbitSendRequest request) {


        //✅ messageId 一旦生成，永不改变
        //只要把 saveMqSendRecord改成「只保存，不返 ID」
        //👉 就已经是标准支付中台实现了 👍
        MqSendRecord record = new MqSendRecord();
        record.setMessageId(request.getMessageId());
        record.setExchange(request.getExchange());
        record.setRoutingKey(request.getRoutingKey());
        record.setBizId(request.getBizId());
        record.setBizType(request.getBizType());   //业务类型，必填
        record.setTopic(request.getTopic());   // ✅ 这一行必须有
        record.setMessageBody(request.getMessageBody()); // ✅ 必须有
        record.setSendStatus(SendStatus.SENDING.getValue());
        record.setCreateTime(LocalDateTime.now());

        return mqSendRecordService.save(record);
//        return mqSendRecordService.saveMqSendRecord(
//                request.getExchange(),
//                request.getRoutingKey(),
//                request.getBizId(),
//                request.getBody()
//        );
    }


    private String resolveTransactionId(SendPaymentMqDTO dto) {

        // 1️优先用微信回调的交易号
//        if (StringUtils.isNotBlank(dto.getWechatTransactionId())) {
//            return dto.getWechatTransactionId();
//        }

        // 2️其次用 DTO 里已有的 transactionId
        if (StringUtils.isNotBlank(dto.getTransactionId())) {
            return dto.getTransactionId();
        }

        // 3️⃣ 兜底：生成一个模拟交易号（不影响业务）
        return generateMockTransactionId(dto.getPaymentNo());
    }

    private String generateMockTransactionId(String paymentNo) {
        return "MOCK_" + paymentNo + "_" + System.currentTimeMillis();
    }

}
