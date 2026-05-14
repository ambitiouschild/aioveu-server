package com.aioveu.pay.aioveu12MqProducerPayment.service.RocketMQ.impl;


import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatus;
import com.aioveu.pay.aioveu10MqSendRecord.mapper.MqSendRecordMapper;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.aioveu.pay.aioveu10MqSendRecord.utils.MessageIdGenerator;
import com.aioveu.pay.aioveu12MqProducerPayment.MQMonitorProducer.ProducerMetricsCollector;
import com.aioveu.pay.aioveu12MqProducerPayment.MQMonitorProducer.ProducerMonitor;
import com.aioveu.pay.aioveu12MqProducerPayment.enums.MessageQueueTypeEnum;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendRequest;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RocketMQ.RocketBatchSendResult;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendResult;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.PaymentFailedMessage;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.PaymentSuccessMessage;
import com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ.RabbitMessageService;
import com.aioveu.pay.aioveu12MqProducerPayment.util.AdapterMessageBuilder;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;  // ✅
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
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
public class RabbitMessageServiceImpl extends ServiceImpl<MqSendRecordMapper, MqSendRecord> implements RabbitMessageService {


    private final MessageProducer messageProducer;
    private final MessageIdGenerator messageIdGenerator;


    private final MqSendRecordMapper mqSendRecordMapper;
    private final RocketMQTemplate rocketMQTemplate;

    private final MqSendRecordService mqSendRecordService;

    @Value("${rocketmq.topic.payment-success:payment_success_topic}")
    private String paymentSuccessTopic;

    private final ProducerMonitor producerMonitor;
    private final ProducerMetricsCollector metricsCollector;

    private final AdapterMessageBuilder adapterMessageBuilder;


    // 存储消息回调上下文，用于重试
    private final Map<String, SendContext> sendContextMap = new ConcurrentHashMap<>();


    /**
     * 发送支付成功消息
     */
    @Override
    public boolean sendPaymentSuccessMessage(PayOrder payOrder, Map<String, String> params) {
        long startTime = System.currentTimeMillis();
        boolean success = false;

        try {

            // 构建消息
            PaymentSuccessMessage message = PaymentSuccessMessage.builder()
                    .messageId(UUID.randomUUID().toString())
                    .paymentNo(payOrder.getPaymentNo())
                    .orderNo(payOrder.getOrderNo())
                    .transactionId(params.get("transaction_id"))
                    .amount(payOrder.getPaymentAmount())
                    .channel(payOrder.getPaymentChannel())
                    .paymentTime(LocalDateTime.now())
                    .memberId(payOrder.getUserId())
                    .build();

            // 保存发送记录
            String messageId = mqSendRecordService.saveMqSendRecord(
                    "payment_success_topic",
                    "wechat_pay",
                    payOrder.getPaymentNo(),  // 使用订单号保证顺序
                    message
            );



            // 发送顺序消息，确保同一订单的消息顺序
            SendResult sendResult = rocketMQTemplate.syncSendOrderly(
                    paymentSuccessTopic + ":wechat_pay",
                    MessageBuilder
                            .withPayload(message)
                            .setHeader(MessageConst.PROPERTY_KEYS, payOrder.getOrderNo())
                            .build(),
                    payOrder.getOrderNo(),  // 使用订单号作为Sharding Key  // 使用订单号保证顺序
                    3000  // 3秒超时
            );

            // 更新发送状态
           success =  mqSendRecordService.updateSendStatus(messageId, SendStatus.SUCCESS, null);

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
        PaymentFailedMessage message = PaymentFailedMessage.builder()
                .paymentNo(payOrder.getPaymentNo())
                .orderNo(payOrder.getOrderNo())
                .errorCode(params.get("err_code"))
                .errorMsg(params.get("err_code_des"))
                .channel(payOrder.getPaymentChannel())
                .build();

        rocketMQTemplate.syncSend("payment-failed-topic", message);

        // 记录发送结果
        producerMonitor.recordSendResult(false);

        return true;

    }

    /**
     * 批量发送消息
     */
    @Override
    public RocketBatchSendResult batchSend(List<PaymentSuccessMessage> messages) {
        RocketBatchSendResult result = new RocketBatchSendResult();
        long totalStartTime = System.currentTimeMillis();

        for (PaymentSuccessMessage message : messages) {
            long startTime = System.currentTimeMillis();
            boolean success = false;

            try {
                // 发送单条消息
                success = sendSingleMessage(message);

                // 更新统计
                if (success) {
                    result.incrementSuccess();
                } else {
                    result.incrementFailed();
                }

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
        }

        try {
            // 1. 参数校验
            adapterMessageBuilder.validateRequest(request);

            // 3. 根据配置选择MQ
            MessageQueueTypeEnum queueType = adapterMessageBuilder.determineQueueType(request);


            // 3. 发送消息
            SendResult sendResult = adapterMessageBuilder.doSend(queueType, request);

            // 5. 记录成功日志
            adapterMessageBuilder.logSendSuccess(request, sendResult, startTime);

            return sendResult;

        } catch (Exception e) {
            // 6. 记录失败日志
            adapterMessageBuilder.logSendFailure(request, messageId, e, startTime);
            throw new MessageSendException("消息发送失败", e, messageId);
        }
    }




}
