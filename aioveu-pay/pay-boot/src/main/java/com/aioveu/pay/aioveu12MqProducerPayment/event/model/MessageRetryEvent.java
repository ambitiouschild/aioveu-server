package com.aioveu.pay.aioveu12MqProducerPayment.event.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * @ClassName: MessageRetryEvent
 * @Description TODO 方案5最简单，直接移除 @Builder注解，因为 ApplicationEvent 通常不需要 Builder 模式
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/17 18:27
 * @Version 1.0
 **/
@Data
public class MessageRetryEvent extends ApplicationEvent {

    private String messageId;
    private Long tenantId;
    private int retryCount;
    private String lastError;
    private LocalDateTime retryTime;

    public MessageRetryEvent(Object source, String messageId, Long tenantId,
                             int retryCount, String lastError, LocalDateTime retryTime) {
        super(source);
        this.messageId = messageId;
        this.tenantId = tenantId;
        this.retryCount = retryCount;
        this.lastError = lastError;
        this.retryTime = retryTime;
    }
}
