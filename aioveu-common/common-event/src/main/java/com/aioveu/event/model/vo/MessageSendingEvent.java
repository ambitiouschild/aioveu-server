package com.aioveu.event.model.vo;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * @ClassName: MessageSendingEvent
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/17 18:25
 * @Version 1.0
 **/

/*
* 因为 Lombok 的 @Builder注解生成的 Builder 不会自动处理父类 ApplicationEvent的构造器参数。你需要手动定义 Builder 或者使用 @SuperBuilder。
* 如果你使用的是 Lombok 1.18.16+，可以使用 @SuperBuilder
* 如果你不能升级 Lombok 或者想保持兼容性，使用手动定义的 Builder
* */
@Data
public class MessageSendingEvent extends ApplicationEvent {

    private String messageId;
    private Long tenantId;
    private String messageType;
    private LocalDateTime sendTime;

    public MessageSendingEvent(Object source, String messageId, Long tenantId,
                               String messageType, LocalDateTime sendTime) {
        super(source);
        this.messageId = messageId;
        this.tenantId = tenantId;
        this.messageType = messageType;
        this.sendTime = sendTime;
    }

    /**
     * 创建消息发送事件
     */
    public static MessageSendingEvent create(Object source, String messageId,
                                             Long tenantId, String messageType) {
        return new MessageSendingEvent(source, messageId, tenantId, messageType, LocalDateTime.now());
    }

}
