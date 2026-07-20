package com.aioveu.event.model.vo;


import com.aioveu.common.rabbitmq.enums.SendStatusEnum;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendResult;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MessageSendFailedEvent
 * @Description TODO 消息发送失败事件
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 20:32
 * @Version 1.0
 **/
@Data
// 移除 @Builder, @NoArgsConstructor, @AllArgsConstructor
public class MessageSendFailedEvent extends ApplicationEvent {

    private String messageId;
    private Long tenantId;
    private String messageType;
    private String exchange;
    private String routingKey;
    private Long costTime;
    private LocalDateTime sendTime;
    private String errorCode;
    private String errorMessage;
    private SendStatusEnum sendStatusEnum;
    private Integer retryCount;
    private Map<String, Object> extraInfo = new HashMap<>();
    private LocalDateTime eventTime = LocalDateTime.now();

    public MessageSendFailedEvent(Object source, String messageId, Long tenantId,
                                  String messageType, String exchange, String routingKey,
                                  Long costTime, LocalDateTime sendTime, String errorCode,
                                  String errorMessage, SendStatusEnum sendStatusEnum, Integer retryCount,
                                  Map<String, Object> extraInfo, LocalDateTime eventTime) {
        super(source);
        this.messageId = messageId;
        this.tenantId = tenantId;
        this.messageType = messageType;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.costTime = costTime;
        this.sendTime = sendTime;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.sendStatusEnum = sendStatusEnum;
        this.retryCount = retryCount;
        this.extraInfo = extraInfo;
        this.eventTime = eventTime;
    }

    // 手动定义 Builder
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
        private String errorCode;
        private String errorMessage;
        private SendStatusEnum sendStatusEnum;
        private Integer retryCount = 0;
        private Map<String, Object> extraInfo = new HashMap<>();
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

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder sendStatus(SendStatusEnum sendStatusEnum) {
            this.sendStatusEnum = sendStatusEnum;
            return this;
        }

        public Builder retryCount(Integer retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public Builder extraInfo(Map<String, Object> extraInfo) {
            this.extraInfo = extraInfo;
            return this;
        }

        public Builder eventTime(LocalDateTime eventTime) {
            this.eventTime = eventTime;
            return this;
        }

        public MessageSendFailedEvent build() {
            if (source == null) {
                throw new IllegalStateException("source 参数不能为空");
            }
            return new MessageSendFailedEvent(
                    source, messageId, tenantId, messageType, exchange, routingKey,
                    costTime, sendTime, errorCode, errorMessage, sendStatusEnum, retryCount,
                    extraInfo, eventTime
            );
        }
    }

    public static MessageSendFailedEvent fromResult(Object source, RabbitSendResult result) {
        return MessageSendFailedEvent.builder()
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
                .errorMessage(result.getErrorMessage())
                .sendStatus(result.getSendStatusEnum())
                .eventTime(LocalDateTime.now())
                .build();
    }
}
