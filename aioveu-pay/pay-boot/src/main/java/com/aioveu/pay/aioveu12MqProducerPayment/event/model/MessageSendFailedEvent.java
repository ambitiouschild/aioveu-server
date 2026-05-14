package com.aioveu.pay.aioveu12MqProducerPayment.event.model;


import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatus;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendFailedEvent extends ApplicationEvent {

    private String messageId;
    private String tenantId;
    private String messageType;
    private String exchange;
    private String routingKey;
    private Long costTime;
    private LocalDateTime sendTime;
    private String errorCode;
    private String errorMessage;
    private SendStatus sendStatus;
    private Integer retryCount;
    private Map<String, Object> extraInfo;
    @Builder.Default
    private LocalDateTime eventTime = LocalDateTime.now();

    public MessageSendFailedEvent(Object source) {
        super(source);
    }

    public static MessageSendFailedEvent fromResult(RabbitSendResult result) {
        return MessageSendFailedEvent.builder()
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
                .sendStatus(convertStatus(result.getSendStatus()))
                .eventTime(LocalDateTime.now())
                .build();
    }

    private static SendStatus convertStatus(SendStatus status) {
        if (status == null) return SendStatus.UNKNOWN;
        // 转换逻辑
        return SendStatus.FAILED;
    }
}
