package com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ.impl;


import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatus;
import com.aioveu.pay.aioveu10MqSendRecord.mapper.MqSendRecordMapper;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.aioveu.pay.aioveu10MqSendRecord.utils.MessageIdGenerator;
import com.aioveu.pay.aioveu12MqProducerPayment.Monitor.RabbitMQ.ProducerMonitor;
import com.aioveu.pay.aioveu12MqProducerPayment.Monitor.RabbitMQ.ProducerMetricsCollector;
import com.aioveu.pay.aioveu12MqProducerPayment.enums.MessageQueueTypeEnum;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RocketMQ.RocketBatchSendResult;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendResult;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.PaymentFailedMessage;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.PaymentSuccessMessage;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendRequest;
import com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ.RabbitMessageService;
import com.aioveu.pay.aioveu12MqProducerPayment.util.AdapterMessageBuilder;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.apache.rocketmq.client.producer.SendResult;  // ✅
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 同步发送并获取结果
     */
    public RabbitSendResult sendMessageSync(String exchange, String routingKey, Object message) {
        long startTime = System.currentTimeMillis();
        String messageId = UUID.randomUUID().toString();

        try {
            // 构建消息
            CorrelationData correlationData = new CorrelationData(messageId);

            // 发送消息
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);

            // 等待确认
            correlationData.getFuture().get(5000, TimeUnit.MILLISECONDS);

            // 构建成功结果
            return RabbitSendResult.success(messageId, correlationData.getId(),
                            System.currentTimeMillis() - startTime, exchange, routingKey)
                    .withTenant("tenant_001")
                    .withMessageType("ORDER_CREATE")
                    .addExtraInfo("bizId", "order_1001");

        } catch (TimeoutException e) {
            // 超时结果
            return RabbitSendResult.timeout(messageId,
                            System.currentTimeMillis() - startTime, exchange, routingKey)
                    .withTenant("tenant_001");

        } catch (Exception e) {
            // 失败结果
            return RabbitSendResult.failure(messageId, e.getMessage(),
                            System.currentTimeMillis() - startTime, exchange, routingKey)
                    .withTenant("tenant_001");
        }
    }

    /**
     * 处理发送结果
     */
    public void handleSendResult(RabbitSendResult result) {
        // 记录日志
        log.info("消息发送结果: {}", result.getSimpleInfo());

        // 判断是否成功
        if (result.isSuccess()) {
            log.info("消息发送成功: messageId={}, exchange={}, cost={}ms",
                    result.getMessageId(), result.getExchange(), result.getCostTime());

            // 成功后的业务处理
            onMessageSendSuccess(result);

        } else {
            log.error("消息发送失败: messageId={}, error={}",
                    result.getMessageId(), result.getFullErrorMessage());

            // 失败处理
            if (result.shouldRetry()) {
                log.warn("消息发送失败，准备重试: messageId={}, retryCount={}",
                        result.getMessageId(), result.getRetryCount());
                retrySend(result);
            } else {
                onMessageSendPermanentFailure(result);
            }
        }

        // 监控指标
        recordMetrics(result);
    }

    /**
     * 批量发送结果处理
     */
    public void handleBatchResults(List<RabbitSendResult> results) {
        int successCount = 0;
        int failureCount = 0;

        for (RabbitSendResult result : results) {
            if (result.isSuccess()) {
                successCount++;
            } else {
                failureCount++;
                log.error("批量发送失败项: {}", result.getSimpleInfo());
            }
        }

        log.info("批量发送完成: 总数={}, 成功={}, 失败={}",
                results.size(), successCount, failureCount);
    }

    /**
     * 从ReturnedMessage构建结果
     */
    public RabbitSendResult createResultFromReturned(ReturnedMessage returnedMessage) {
        String messageId = extractMessageId(returnedMessage.getMessage());

        return RabbitSendResult.routingFailed(messageId, returnedMessage)
                .withTenant("tenant_001")
                .withMessageType("ORDER_NOTIFY")
                .addExtraInfo("originalExchange", returnedMessage.getExchange())
                .addExtraInfo("originalRoutingKey", returnedMessage.getRoutingKey());
    }

    /**
     * 从CorrelationData构建结果
     */
    public RabbitSendResult createResultFromCorrelation(CorrelationData correlationData,
                                                        long startTime, String exchange,
                                                        String routingKey) {
        RabbitSendResult result = RabbitSendResult.fromCorrelationData(
                correlationData, startTime, exchange, routingKey);

        // 添加业务信息
        result.setTenantId("tenant_001");
        result.setMessageType("ORDER_CREATE");
        result.setSendThread(Thread.currentThread().getName());

        return result;
    }

    private void onMessageSendSuccess(RabbitSendResult result) {
        // 更新消息状态为已发送
        messageStatusService.updateStatus(result.getMessageId(), MessageStatus.SENT);

        // 发送成功通知
        eventPublisher.publishMessageSent(result);
    }

    private void retrySend(RabbitSendResult result) {
        // 实现重试逻辑
        result.setRetried(true);
        result.setRetryCount(result.getRetryCount() + 1);

        // 重新发送消息
        // sendMessageSync(...);
    }

    private void onMessageSendPermanentFailure(RabbitSendResult result) {
        // 记录到失败表
        failedMessageService.recordFailure(result);

        // 发送告警
        alertService.sendAlert("消息发送永久失败", result.toJson());
    }

    private void recordMetrics(RabbitSendResult result) {
        // 记录监控指标
        metricsService.recordMessageSend(
                result.getTenantId(),
                result.getMessageType(),
                result.isSuccess(),
                result.getCostTime()
        );
    }

    private String extractMessageId(Message message) {
        // 从消息中提取messageId
        return message.getMessageProperties().getMessageId();
    }


}
