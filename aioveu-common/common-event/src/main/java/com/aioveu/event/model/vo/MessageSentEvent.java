package com.aioveu.event.model.vo;


import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendResult;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MessageSentEvent
 * @Description TODO 消息发送成功事件
 *                      这是一个典型的领域事件发布模式，在DDD（领域驱动设计）中很常见。
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 20:30
 * @Version 1.0
 **/


/*
*
*   TODO    推荐使用方案1（移除 @Builder注解，保留手动 Builder），因为：
                1.控制权：完全控制 Builder 的行为
                2.可读性：代码意图明确
                3.可维护性：易于调试和修改
                4.兼容性：不依赖 Lombok 特定版本
                5.可扩展性：可以轻松添加验证逻辑
*
*
* */
@Data
// 移除 @Builder
public class MessageSentEvent extends ApplicationEvent {

    private String messageId;
    private Long tenantId;
    private String messageType;
    private String exchange;
    private String routingKey;
    private Long costTime;
    private LocalDateTime sendTime;
    private LocalDateTime confirmTime;
    private String businessId;
    private Integer messageSize;
    private Map<String, Object> extraInfo = new HashMap<>();
    private String sendThread;
    private String clientIp;
    private LocalDateTime eventTime = LocalDateTime.now();

    public MessageSentEvent(Object source, String messageId, Long tenantId,
                            String messageType, String exchange, String routingKey,
                            Long costTime, LocalDateTime sendTime, LocalDateTime confirmTime,
                            String businessId, Integer messageSize, Map<String, Object> extraInfo,
                            String sendThread, String clientIp, LocalDateTime eventTime) {
        super(source);
        this.messageId = messageId;
        this.tenantId = tenantId;
        this.messageType = messageType;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.costTime = costTime;
        this.sendTime = sendTime;
        this.confirmTime = confirmTime;
        this.businessId = businessId;
        this.messageSize = messageSize;
        this.extraInfo = extraInfo;
        this.sendThread = sendThread;
        this.clientIp = clientIp;
        this.eventTime = eventTime;
    }

    // 保留你手动定义的 Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object source;
        private String messageId;
        private Long tenantId;
        private String messageType;
        private String exchange;
        private String routingKey;
        private Long costTime;
        private LocalDateTime sendTime;
        private LocalDateTime confirmTime;
        private String businessId;
        private Integer messageSize;
        private Map<String, Object> extraInfo = new HashMap<>();
        private String sendThread;
        private String clientIp;
        private LocalDateTime eventTime = LocalDateTime.now();

        public Builder source(Object source) {
            this.source = source;
            return this;
        }

        public Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder tenantId(Long tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder messageType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder exchange(String exchange) {
            this.exchange = exchange;
            return this;
        }

        public Builder routingKey(String routingKey) {
            this.routingKey = routingKey;
            return this;
        }

        public Builder costTime(Long costTime) {
            this.costTime = costTime;
            return this;
        }

        public Builder sendTime(LocalDateTime sendTime) {
            this.sendTime = sendTime;
            return this;
        }

        public Builder confirmTime(LocalDateTime confirmTime) {
            this.confirmTime = confirmTime;
            return this;
        }

        public Builder businessId(String businessId) {
            this.businessId = businessId;
            return this;
        }

        public Builder messageSize(Integer messageSize) {
            this.messageSize = messageSize;
            return this;
        }

        public Builder extraInfo(Map<String, Object> extraInfo) {
            this.extraInfo = extraInfo;
            return this;
        }

        public Builder sendThread(String sendThread) {
            this.sendThread = sendThread;
            return this;
        }

        public Builder clientIp(String clientIp) {
            this.clientIp = clientIp;
            return this;
        }

        public Builder eventTime(LocalDateTime eventTime) {
            this.eventTime = eventTime;
            return this;
        }

        public MessageSentEvent build() {
            if (source == null) {
                throw new IllegalStateException("source 参数不能为空");
            }
            return new MessageSentEvent(
                    source, messageId, tenantId, messageType, exchange, routingKey,
                    costTime, sendTime, confirmTime, businessId, messageSize, extraInfo,
                    sendThread, clientIp, eventTime
            );
        }
    }

    public static MessageSentEvent fromResult(Object source, RabbitSendResult result, Long tenantId) {
        return MessageSentEvent.builder()
                .source(source)
                .messageId(result.getMessageId())
                .tenantId(result.getTenantId())
                .messageType(result.getMessageType())
                .exchange(result.getExchange())
                .routingKey(result.getRoutingKey())
                .costTime(result.getCostTime())
                .sendTime(result.getSendTime() != null ?
                        LocalDateTime.ofInstant(result.getSendTime().toInstant(), java.time.ZoneId.systemDefault()) :
                        null)
                .confirmTime(result.getConfirmTime() != null ?
                        LocalDateTime.ofInstant(result.getConfirmTime().toInstant(), java.time.ZoneId.systemDefault()) :
                        null)
                .sendThread(result.getSendThread())
                .clientIp(result.getClientIp())
                .eventTime(LocalDateTime.now())
                .build();
    }

    public String getEventId() {
        return "msg-sent-" + messageId + "-" + eventTime.toEpochSecond(java.time.ZoneOffset.UTC);
    }

    public String getSummary() {
        return String.format("MessageSentEvent{messageId=%s, tenant=%s, type=%s, exchange=%s, cost=%dms}",
                messageId, tenantId, messageType, exchange, costTime);
    }
}
