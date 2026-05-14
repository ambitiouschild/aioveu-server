package com.aioveu.pay.aioveu12MqProducerPayment.event.service.impl;


import com.aioveu.pay.aioveu12MqProducerPayment.event.model.MessageSendFailedEvent;
import com.aioveu.pay.aioveu12MqProducerPayment.event.model.MessageSentEvent;
import com.aioveu.pay.aioveu12MqProducerPayment.event.service.MessageEventPublisher;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName: MessageEventPublisherImpl
 * @Description TODO 事件发布器实现
 *                   这个设计遵循了领域事件和观察者模式，是微服务架构中常见的解耦方式。
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 20:35
 * @Version 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageEventPublisherImpl implements MessageEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishMessageSent(RabbitSendResult result) {
        try {
            // 1. 创建事件对象
            MessageSentEvent event = MessageSentEvent.fromResult(result);

            // 2. 添加业务信息
            addBusinessInfo(event, result);

            // 3. 发布事件
            applicationEventPublisher.publishEvent(event);

            // 4. 记录日志
            log.info("发布消息发送成功事件: {}", event.getSummary());
            log.debug("事件详情: {}", event);

        } catch (Exception e) {
            log.error("发布消息发送成功事件失败: messageId={}", result.getMessageId(), e);
        }
    }

    @Override
    public void publishMessageSendFailed(RabbitSendResult result) {
        try {
            MessageSendFailedEvent event = MessageSendFailedEvent.fromResult(result);
            applicationEventPublisher.publishEvent(event);

            log.warn("发布消息发送失败事件: messageId={}, error={}",
                    result.getMessageId(), result.getErrorMessage());

        } catch (Exception e) {
            log.error("发布消息发送失败事件失败", e);
        }
    }

    @Override
    public void publishMessageSending(String messageId, String tenantId, String messageType) {
        MessageSendingEvent event = MessageSendingEvent.builder()
                .messageId(messageId)
                .tenantId(tenantId)
                .messageType(messageType)
                .sendTime(LocalDateTime.now())
                .build();

        applicationEventPublisher.publishEvent(event);
        log.debug("发布消息发送开始事件: messageId={}", messageId);
    }

    @Override
    public void publishMessageRetry(RabbitSendResult result, int retryCount) {
        MessageRetryEvent event = MessageRetryEvent.builder()
                .messageId(result.getMessageId())
                .tenantId(result.getTenantId())
                .retryCount(retryCount)
                .lastError(result.getErrorMessage())
                .retryTime(LocalDateTime.now())
                .build();

        applicationEventPublisher.publishEvent(event);
        log.info("发布消息重试事件: messageId={}, retryCount={}",
                result.getMessageId(), retryCount);
    }

    @Override
    public void publishBatchMessageSent(BatchSendResult result) {
        BatchMessageSentEvent event = BatchMessageSentEvent.fromResult(result);
        applicationEventPublisher.publishEvent(event);

        log.info("发布批量消息发送事件: batchId={}, total={}, success={}, failed={}",
                result.getBatchId(), result.getTotalCount(),
                result.getSuccessCount(), result.getFailedCount());
    }

    /**
     * 从扩展信息中提取业务信息
     */
    private void addBusinessInfo(MessageSentEvent event, RabbitSendResult result) {
        if (result.getExtraInfo() != null) {
            // 提取业务ID
            Object bizId = result.getExtraInfo().get("businessId");
            if (bizId != null) {
                event.setBusinessId(bizId.toString());
            }

            // 提取其他业务信息
            Object orderId = result.getExtraInfo().get("orderId");
            if (orderId != null) {
                event.getExtraInfo().put("orderId", orderId);
            }

            Object userId = result.getExtraInfo().get("userId");
            if (userId != null) {
                event.getExtraInfo().put("userId", userId);
            }
        }
    }
}
