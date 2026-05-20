package com.aioveu.pay.aioveu12MqProducerPayment.service.impl;


import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatus;
import com.aioveu.pay.aioveu10MqSendRecord.mapper.MqSendRecordMapper;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.aioveu.common.util.MessageIdGenerator;
import com.aioveu.pay.aioveu12MqProducerPayment.Monitor.RabbitMQ.ProducerMetricsCollector;
import com.aioveu.pay.aioveu12MqProducerPayment.Monitor.RabbitMQ.ProducerMonitor;
import com.aioveu.pay.aioveu12MqProducerPayment.enums.MessageQueueTypeEnum;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitBatchSendResult;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendRequest;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendResult;
import com.aioveu.common.rabbitmq.producer.model.vo.SendContext;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.MessageSendException;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.PaymentFailedMessage;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.PaymentSuccessMessage;
import com.aioveu.common.rabbitmq.producer.service.RabbitMessageServicePayment;
import com.aioveu.common.rabbitmq.producer.util.AdapterMessageBuilderImpl;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
public class RabbitMessageServicePaymentImpl extends ServiceImpl<MqSendRecordMapper, MqSendRecord> implements RabbitMessageServicePayment {


    private final MessageIdGenerator messageIdGenerator;
    private final MqSendRecordMapper mqSendRecordMapper;
    private final MqSendRecordService mqSendRecordService;
    private final ProducerMonitor producerMonitor;
    private final ProducerMetricsCollector metricsCollector;

    private final AdapterMessageBuilderImpl adapterMessageBuilderImpl;



    @Value("${rabbitmq.exchange.payment:payment.exchange}")
    private String paymentExchange;

    @Value("${rabbitmq.routing-key.payment-success:payment.success}")
    private String paymentSuccessRoutingKey;

    @Value("${rabbitmq.routing-key.payment-failed:payment.failed}")
    private String paymentFailedRoutingKey;




    // 存储消息回调上下文，用于重试
    private final Map<String, SendContext> sendContextMap = new ConcurrentHashMap<>();


    /**
     * 发送支付成功消息
     */
    @Override
    public boolean sendPaymentSuccessMessage(PayOrder payOrder, Map<String, String> params) {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        String messageId = null;

        try {

//            // 构建消息
//            PaymentSuccessMessage message = PaymentSuccessMessage.builder()
//                    .messageId(UUID.randomUUID().toString())
//                    .paymentNo(payOrder.getPaymentNo())
//                    .orderNo(payOrder.getOrderNo())
//                    .transactionId(params.get("transaction_id"))
//                    .amount(payOrder.getPaymentAmount())
//                    .channel(payOrder.getPaymentChannel())
//                    .paymentTime(LocalDateTime.now())
//                    .memberId(payOrder.getUserId())
//                    .build();

            // 构建消息
            PaymentSuccessMessage message = buildPaymentSuccessMessage(payOrder, params);

                // 创建 RabbitSendRequest
            RabbitSendRequest request = RabbitSendRequest.builder()
                    .body(message)
                    .messageId(UUID.randomUUID().toString())
                    .exchange(paymentExchange)
                    .routingKey(paymentSuccessRoutingKey)
                    .messageType("PAYMENT_SUCCESS")
                    .persistent(true)
                    .priority(5)
                    .tenantId(payOrder.getTenantId())
                    .bizId(payOrder.getPaymentNo())
                    .build();



            // 保存发送记录
            messageId = saveMqSendRecord(request);

            // 发送消息
            RabbitSendResult result = sendSingleMessage(request);
            success = result.isSuccess();


            // 更新发送状态
            mqSendRecordService.updateSendStatus(messageId,
                    success ? SendStatus.SUCCESS : SendStatus.FAILED,
                    success ? null : result.getErrorMessage()
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
                    UUID.randomUUID().toString(),
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
    public boolean sendPaymentFailedMessage(PayOrder payOrder, Map<String, String> params) {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        String messageId = null;

        try {
            // 构建消息
            PaymentFailedMessage message = buildPaymentFailedMessage(payOrder, params);

            // 创建 RabbitSendRequest
            RabbitSendRequest request = RabbitSendRequest.builder()
                    .body(message)
                    .messageId(UUID.randomUUID().toString())
                    .exchange(paymentExchange)
                    .routingKey(paymentFailedRoutingKey)
                    .messageType("PAYMENT_FAILED")
                    .persistent(true)
                    .priority(3)
                    .tenantId(payOrder.getTenantId())
                    .bizId(payOrder.getPaymentNo())
                    .build();

            // 保存发送记录
            messageId = saveMqSendRecord(request);

            // 发送消息
            RabbitSendResult result = sendSingleMessage(request);
            success = result.isSuccess();

            // 更新发送状态
            mqSendRecordService.updateSendStatus(messageId,
                    success ? SendStatus.SUCCESS : SendStatus.FAILED,
                    success ? null : result.getErrorMessage()
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

            try {

                // 创建 RabbitSendRequest
                RabbitSendRequest request = RabbitSendRequest.builder()
                        .body(message)
                        .messageId(UUID.randomUUID().toString())
                        .exchange(paymentExchange)
                        .routingKey(paymentSuccessRoutingKey)
                        .messageType("PAYMENT_SUCCESS")
                        .persistent(true)
                        .priority(5)
                        .build();


                // 发送单条消息
                RabbitSendResult sendResult = sendSingleMessage(request);
                success = sendResult.isSuccess();

                // 更新统计
                if (success) {
                    result.incrementSuccess();
                } else {
                    result.incrementFailed();
                    result.addFailedMessage(request.getMessageId(), sendResult.getErrorMessage());
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

            // 5. 记录成功日志
            adapterMessageBuilderImpl.logSendSuccess(request, sendResult, startTime);

            return sendResult;

        } catch (Exception e) {
            // 6. 记录失败日志
            adapterMessageBuilderImpl.logSendFailure(request, messageId, e, startTime);
            throw new MessageSendException("消息发送失败", e, messageId);
        }
    }

    /**
     * 构建支付成功消息
     */
    private PaymentSuccessMessage buildPaymentSuccessMessage(PayOrder payOrder, Map<String, String> params) {
        return PaymentSuccessMessage.builder()
                .messageId(UUID.randomUUID().toString())
                .paymentNo(payOrder.getPaymentNo())
                .orderNo(payOrder.getOrderNo())
                .transactionId(params.get("transaction_id"))
                .amount(payOrder.getPaymentAmount())
                .channel(payOrder.getPaymentChannel())
                .paymentTime(LocalDateTime.now())
                .memberId(payOrder.getUserId())
                .build();
    }

    /**
     * 构建支付失败消息
     */
    private PaymentFailedMessage buildPaymentFailedMessage(PayOrder payOrder, Map<String, String> params) {
        return PaymentFailedMessage.builder()
                .paymentNo(payOrder.getPaymentNo())
                .orderNo(payOrder.getOrderNo())
                .errorCode(params.get("err_code"))
                .errorMsg(params.get("err_code_des"))
                .channel(payOrder.getPaymentChannel())
                .build();
    }


    /**
     * 保存消息发送记录
     */
    private String saveMqSendRecord(RabbitSendRequest request) {
        return mqSendRecordService.saveMqSendRecord(
                request.getExchange(),
                request.getRoutingKey(),
                request.getBizId(),
                request.getBody()
        );
    }


}
