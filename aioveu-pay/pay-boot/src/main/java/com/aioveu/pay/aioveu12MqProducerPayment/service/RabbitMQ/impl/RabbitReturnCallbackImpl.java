package com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ.impl;


import com.aioveu.pay.aioveu12MqProducerPayment.config.RabbitMQ.RabbitTemplateManager;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendResult;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.ReturnCallbackStats;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: RabbitReturnCallback
 * @Description TODO RabbitMQ ReturnCallback 实现
 *                      处理路由失败的消息
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 18:18
 * @Version 1.0
 **/
@Slf4j
@Component
public class RabbitReturnCallbackImpl implements RabbitTemplate.ReturnsCallback {


    @Autowired
    private RabbitMessageServiceImpl rabbitMessageService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitTemplateManager rabbitTemplateManager;

    // 存储返回的消息（用于监控和重试）
    private final ConcurrentLinkedQueue<ReturnedMessage> returnedMessages = new ConcurrentLinkedQueue<>();
    private final AtomicInteger returnedCount = new AtomicInteger(0);


    @PostConstruct
    public void init() {
        // 设置ReturnCallback
        rabbitTemplate.setReturnsCallback(this);
        log.info("FixedRabbitReturnCallback已注册到RabbitTemplate");
    }


    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        long startTime = System.currentTimeMillis();

        try {
            // 记录返回的消息
            returnedMessages.offer(returnedMessage);
            int count = returnedCount.incrementAndGet();

            // 记录统计
            rabbitTemplateManager.recordReturnCallback();

            // 处理返回的消息
            processReturnedMessage(returnedMessage);

            // 创建结果对象
            RabbitSendResult result = rabbitMessageService.createEnhancedResultFromReturned(
                    returnedMessage, "default_tenant", "RETURNED_MESSAGE"
            );

            // 记录日志
            log.error("消息路由失败: {}", result.getDetailInfo());

            // 分析原因
            String cause = rabbitMessageService.analyzeReturnedCause(returnedMessage);
            log.error("路由失败原因: {}", cause);

            // 添加监控
            monitorReturnedMessage(returnedMessage, result, count);

            // 处理返回的消息（可以重试、记录到数据库等）
            handleReturnedMessage(returnedMessage, result);

            long costTime = System.currentTimeMillis() - startTime;
            log.debug("处理ReturnedMessage完成，耗时: {}ms", costTime);

        } catch (Exception e) {
            log.error("处理ReturnedMessage异常", e);
        }
    }

    /**
     * 处理返回的消息
     */
    private void handleReturnedMessage(ReturnedMessage returnedMessage, RabbitSendResult result) {
        // 1. 记录到数据库
        recordToDatabase(returnedMessage, result);

        // 2. 发送告警
        sendAlert(returnedMessage, result);

        // 3. 根据策略决定是否重试
        if (shouldRetry(returnedMessage)) {
            retryMessage(returnedMessage, result);
        }

        // 4. 清理旧消息
        cleanupOldMessages();
    }

    /**
     * 记录到数据库
     */
    private void recordToDatabase(ReturnedMessage returnedMessage, RabbitSendResult result) {
        try {
            // 这里实现数据库记录逻辑
            // 例如：保存到 failed_messages 表
            log.info("记录返回消息到数据库: {}", result.getMessageId());

        } catch (Exception e) {
            log.error("记录返回消息到数据库失败", e);
        }
    }

    /**
     * 发送告警
     */
    private void sendAlert(ReturnedMessage returnedMessage, RabbitSendResult result) {
        // 发送邮件、钉钉、企业微信等告警
        String alertMessage = String.format(
                "RabbitMQ消息路由失败告警:\n" +
                        "消息ID: %s\n" +
                        "交换机: %s\n" +
                        "路由键: %s\n" +
                        "错误码: %d\n" +
                        "错误信息: %s\n" +
                        "时间: %s",
                result.getMessageId(),
                returnedMessage.getExchange(),
                returnedMessage.getRoutingKey(),
                returnedMessage.getReplyCode(),
                returnedMessage.getReplyText(),
                new java.util.Date()
        );

        log.warn("发送告警: {}", alertMessage);
    }

    /**
     * 判断是否需要重试
     */
    private boolean shouldRetry(ReturnedMessage returnedMessage) {
        // 某些错误不需要重试
        int replyCode = returnedMessage.getReplyCode();

        // 以下错误不需要重试
        if (replyCode == 312) { // NO_ROUTE - 路由配置错误
            return false;
        }
        if (replyCode == 403) { // ACCESS_REFUSED - 权限问题
            return false;
        }
        if (replyCode == 404) { // NOT_FOUND - 资源不存在
            return false;
        }

        // 其他情况可以重试
        return true;
    }

    /**
     * 重试消息
     */
    private void retryMessage(ReturnedMessage returnedMessage, RabbitSendResult result) {
        try {
            log.info("准备重试消息: {}", result.getMessageId());

            // 这里实现重试逻辑
            // 例如：重新发送到死信队列或重试队列

        } catch (Exception e) {
            log.error("重试消息失败: {}", result.getMessageId(), e);
        }
    }

    /**
     * 清理旧消息
     */
    private void cleanupOldMessages() {
        // 限制队列大小
        while (returnedMessages.size() > 1000) {
            returnedMessages.poll();
        }
    }

    /**
     * 监控返回的消息
     */
    private void monitorReturnedMessage(ReturnedMessage returnedMessage,
                                        RabbitSendResult result, int count) {
        // 这里可以集成监控系统
        // 例如：Prometheus, Grafana, ELK等

        log.debug("当前返回消息总数: {}", count);

        // 可以按交换机、路由键统计
        // 可以按错误码统计
    }

    /**
     * 获取统计信息
     */
    public ReturnCallbackStats getStats() {
        ReturnCallbackStats stats = new ReturnCallbackStats();
        stats.setTotalReturned(returnedCount.get());
        stats.setCurrentQueueSize(returnedMessages.size());
        stats.setLastReturnedTime(new java.util.Date());
        return stats;
    }


    private void processReturnedMessage(ReturnedMessage returnedMessage) {
        try {
            log.error("消息路由失败 - 详细:");
            log.error("  交换机: {}", returnedMessage.getExchange());
            log.error("  路由键: {}", returnedMessage.getRoutingKey());
            log.error("  返回码: {}", returnedMessage.getReplyCode());
            log.error("  返回信息: {}", returnedMessage.getReplyText());
            log.error("  消息: {}",
                    new String(returnedMessage.getMessage().getBody(), java.nio.charset.StandardCharsets.UTF_8));

            // 可以添加更多的处理逻辑
            handleRoutingFailure(returnedMessage);

        } catch (Exception e) {
            log.error("处理ReturnedMessage异常", e);
        }
    }

    private void handleRoutingFailure(ReturnedMessage returnedMessage) {
        int replyCode = returnedMessage.getReplyCode();

        switch (replyCode) {
            case 312: // NO_ROUTE
                log.error("路由失败: 没有匹配的队列，请检查routingKey配置");
                break;
            case 313: // NO_CONSUMERS
                log.error("路由失败: 队列存在但没有消费者");
                break;
            case 403: // ACCESS_REFUSED
                log.error("路由失败: 权限拒绝，请检查用户权限");
                break;
            case 404: // NOT_FOUND
                log.error("路由失败: 交换机或队列不存在");
                break;
            default:
                log.error("路由失败: 未知错误码 {}", replyCode);
        }

        // 这里可以添加重试、告警、记录到数据库等逻辑
    }

}
