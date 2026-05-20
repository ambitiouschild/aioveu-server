package com.aioveu.event.service;


import com.aioveu.common.rabbitmq.producer.model.vo.RabbitBatchSendResult;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendResult;

/**
 * @ClassName: MessageEventPublisher
 * @Description TODO 消息事件发布器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 20:35
 * @Version 1.0
 **/

public interface MessageEventPublisher {

    /**
     * 发布消息发送成功事件
     */
    void publishMessageSent(RabbitSendResult result);

    /**
     * 发布消息发送失败事件
     */
    void publishMessageSendFailed(RabbitSendResult result);

    /**
     * 发布消息发送开始事件
     */
    void publishMessageSending(String messageId, String tenantId, String messageType);

    /**
     * 发布消息重试事件
     */
    void publishMessageRetry(RabbitSendResult result, int retryCount);

    /**
     * 发布批量消息发送事件
     */
    void publishBatchMessageSent(RabbitBatchSendResult result);
}
