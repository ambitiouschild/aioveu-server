package com.aioveu.common.rabbitmq.producer.model.payment;


/**
 * @ClassName: MessageSendException
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/19 19:01
 * @Version 1.0
 **/

/**
 * 消息发送异常
 */
public class MessageSendException extends RuntimeException {

    private String messageId;
    private String queueType;

    public MessageSendException(String message) {
        super(message);
    }

    public MessageSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageSendException(String message, Throwable cause, String messageId) {
        super(message, cause);
        this.messageId = messageId;
    }

    public MessageSendException(String message, Throwable cause, String messageId, String queueType) {
        super(message, cause);
        this.messageId = messageId;
        this.queueType = queueType;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }
}
