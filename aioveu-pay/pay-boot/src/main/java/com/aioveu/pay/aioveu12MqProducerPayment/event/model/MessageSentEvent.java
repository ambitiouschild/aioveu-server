package com.aioveu.pay.aioveu12MqProducerPayment.event.model;


import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSentEvent extends ApplicationEvent {

    /** 消息ID */
    private String messageId;

    /** 租户ID */
    private String tenantId;

    /** 消息类型 */
    private String messageType;

    /** 交换机 */
    private String exchange;

    /** 路由键 */
    private String routingKey;

    /** 发送耗时（毫秒） */
    private Long costTime;

    /** 发送时间 */
    private LocalDateTime sendTime;

    /** 确认时间 */
    private LocalDateTime confirmTime;

    /** 业务ID（订单号、用户ID等） */
    private String businessId;

    /** 消息体大小（字节） */
    private Integer messageSize;

    /** 扩展信息 */
    private Map<String, Object> extraInfo;

    /** 发送线程 */
    private String sendThread;

    /** 客户端IP */
    private String clientIp;

    /** 事件时间 */
    @Builder.Default
    private LocalDateTime eventTime = LocalDateTime.now();

    public MessageSentEvent(Object source) {
        super(source);
    }

    /**
     * 从RabbitSendResult创建事件
     */
    public static MessageSentEvent fromResult(RabbitSendResult result) {
        return MessageSentEvent.builder()
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

    /**
     * 获取事件唯一标识
     */
    public String getEventId() {
        return "msg-sent-" + messageId + "-" + eventTime.toEpochSecond(java.time.ZoneOffset.UTC);
    }

    /**
     * 获取事件摘要
     */
    public String getSummary() {
        return String.format("MessageSentEvent{messageId=%s, tenant=%s, type=%s, exchange=%s, cost=%dms}",
                messageId, tenantId, messageType, exchange, costTime);
    }
}
