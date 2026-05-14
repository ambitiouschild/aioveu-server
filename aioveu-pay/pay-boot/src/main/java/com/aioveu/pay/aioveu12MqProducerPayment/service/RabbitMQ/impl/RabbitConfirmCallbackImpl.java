package com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ.impl;


import com.aioveu.common.util.DateTimeCalculator;
import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatus;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu12MqProducerPayment.config.RabbitMQ.RabbitTemplateManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: RabbitConfirmCallbackImpl
 * @Description TODO RabbitConfirmCallbackImpl
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 18:51
 * @Version 1.0
 **/
@Slf4j
@Component
public class RabbitConfirmCallbackImpl implements RabbitTemplate.ConfirmCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitTemplateManager rabbitTemplateManager;

    // 存储发送中的消息
    private final Map<String, MqSendRecord> pendingMessages = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // 设置ConfirmCallback
        rabbitTemplate.setConfirmCallback(this);
        log.info("FixedRabbitConfirmCallback已注册到RabbitTemplate");
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 记录统计
        rabbitTemplateManager.recordConfirmCallback();

        String correlationId = correlationData != null ? correlationData.getId() : "unknown";

        if (ack) {
            log.debug("消息确认成功: correlationId={}", correlationId);
            recordSuccess(correlationId);
        } else {
            log.error("消息确认失败: correlationId={}, cause={}", correlationId, cause);
            recordFailure(correlationId, cause);
        }

        // 清理记录
        if (correlationId != null) {
            pendingMessages.remove(correlationId);
        }
    }

    /**
     * 记录发送的消息
     */
    public void recordSend(String correlationId, String exchange, String routingKey) {
        MqSendRecord record = new MqSendRecord();
//        record.setCorrelationId(correlationId);
//        record.setExchange(exchange);
//        record.setRoutingKey(routingKey);
        record.setSendTime(LocalDateTime.now());
        record.setSendStatus(SendStatus.PENDING.getValue());

        pendingMessages.put(correlationId, record);
    }

    /**
     * 获取未确认的消息
     */
    public Map<String, MqSendRecord> getPendingMessages() {
        return new ConcurrentHashMap<>(pendingMessages);
    }

    /**
     * 检查超时的消息
     */
    public void checkTimeoutMessages(long timeoutMillis) {
        LocalDateTime now = LocalDateTime.now();

        pendingMessages.entrySet().removeIf(entry -> {
            MqSendRecord record = entry.getValue();

            if (record == null || record.getSendTime() == null) {
                return true; // 移除无效记录
            }


            // 使用方法1：使用 DateTimeCalculator
            long elapsedTime = DateTimeCalculator.getMillisBetween(record.getSendTime(), now);

            // 或者使用方法2：直接使用 Duration
            // long elapsedTime = Duration.between(record.getSendTime(), now).toMillis();

            if (elapsedTime  > timeoutMillis) {
                log.warn("消息发送超时: correlationId={}, exchange={}, routingKey={}, 等待时间={}ms, 超时阈值={}ms",
//                        record.getCorrelationId(),
//                        record.getExchange(),
//                        record.getRoutingKey(),
                        elapsedTime,
                        timeoutMillis);
                return true;
            }
            return false;
        });
    }

    private void recordSuccess(String correlationId) {
        MqSendRecord record = pendingMessages.get(correlationId);
        if (record != null) {
            record.setSendStatus(SendStatus.SUCCESS.getValue());
            record.setConfirmTime(LocalDateTime.now());
//            record.setConfirmDelay(record.getConfirmTime() - record.getSendTime());
        }
    }

    private void recordFailure(String correlationId, String cause) {
        MqSendRecord record = pendingMessages.get(correlationId);
        if (record != null) {
            record.setSendStatus(SendStatus.FAILED.getValue());
            record.setErrorMsg(cause);
            record.setConfirmTime(LocalDateTime.now());
        }
    }


}
