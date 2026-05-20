package com.aioveu.pay.aioveu12MqProducerPayment.event.service.impl;


import com.aioveu.pay.aioveu12MqProducerPayment.event.model.*;
import com.aioveu.pay.aioveu12MqProducerPayment.event.service.MessageEventPublisher;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitBatchSendResult;
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
            MessageSentEvent event = MessageSentEvent.fromResult(this, result, result.getTenantId());

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
            // 1. 创建事件对象
            MessageSentEvent event = MessageSentEvent.fromResult(this, result, result.getTenantId());
            applicationEventPublisher.publishEvent(event);

            log.warn("发布消息发送失败事件: messageId={}, error={}",
                    result.getMessageId(), result.getErrorMessage());

        } catch (Exception e) {
            log.error("发布消息发送失败事件失败", e);
        }
    }

    @Override
    public void publishMessageSending(String messageId, String tenantId, String messageType) {

        // 将 String 转换为 Long
        Long tenantIdLong = null;
        if (tenantId != null && !tenantId.trim().isEmpty()) {
            try {
                tenantIdLong = Long.parseLong(tenantId);
            } catch (NumberFormatException e) {
                log.warn("租户ID格式错误，无法转换为Long: {}", tenantId);
            }
        }

        MessageSendingEvent event = new MessageSendingEvent(
                null,  // source
                messageId,
                tenantIdLong,  // ✅ 使用转换后的 Long
                messageType,
                LocalDateTime.now());

        applicationEventPublisher.publishEvent(event);
        log.debug("发布消息发送开始事件: messageId={}", messageId);
    }

    @Override
    public void publishMessageRetry(RabbitSendResult result, int retryCount) {
        MessageRetryEvent event = new MessageRetryEvent(
                null,
                result.getMessageId(),
                result.getTenantId(),
                retryCount,
                result.getErrorMessage(),
                LocalDateTime.now());

        applicationEventPublisher.publishEvent(event);
        log.info("发布消息重试事件: messageId={}, retryCount={}",
                result.getMessageId(), retryCount);
    }

    @Override
    public void publishBatchMessageSent(RabbitBatchSendResult result) {

            try {
                // 传递 this 作为 source 参数
                BatchMessageSentEvent event = BatchMessageSentEvent.fromResult(this, result);
                applicationEventPublisher.publishEvent(event);

                log.info("发布批量消息发送事件: batchId={}, total={}, success={}, failed={}",
                        result.getBatchId(), result.getTotalCount(),
                        result.getSuccessCount(), result.getFailedCount());
            } catch (Exception e) {
                log.error("发布批量消息发送事件失败: batchId={}", result.getBatchId(), e);
            }
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
