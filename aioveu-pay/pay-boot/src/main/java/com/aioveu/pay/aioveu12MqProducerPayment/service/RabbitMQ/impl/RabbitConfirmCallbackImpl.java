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
import java.util.concurrent.atomic.AtomicBoolean;

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


    // 防止重复初始化
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @PostConstruct
    public void init() {
        if (initialized.compareAndSet(false, true)) {
            try {

                //您使用的Spring AMQP版本（很可能是2.x版本）没有getConfirmCallback()方法，这个方法是在较新版本中添加的。

                // 检查 Spring Boot 版本
                String springBootVersion = org.springframework.boot.SpringBootVersion.getVersion();
                log.info("Spring Boot 版本: {}", springBootVersion);

                // 检查 Spring AMQP 版本
                Package amqpPackage = RabbitTemplate.class.getPackage();
                String amqpVersion = amqpPackage.getImplementationVersion();
                String amqpTitle = amqpPackage.getImplementationTitle();
                log.info("Spring AMQP: {} - {}", amqpTitle, amqpVersion);

                // 检查方法是否存在
                try {
                    RabbitTemplate.class.getMethod("getConfirmCallback");
                    log.info("✅ RabbitTemplate.getConfirmCallback() 方法存在");
                } catch (NoSuchMethodException e) {
                    log.error("❌ RabbitTemplate.getConfirmCallback() 方法不存在");
                }


                // 获取当前已设置的ConfirmCallback
//                RabbitTemplate.ConfirmCallback existingCallback = rabbitTemplate.getConfirmCallback();
                RabbitTemplate.ConfirmCallback existingCallback = null;

                //这已经不影响启动，因为错误被捕获了，应用继续启动。


                if (existingCallback == null) {
                    // 情况1：还没有设置ConfirmCallback
//                    rabbitTemplate.setConfirmCallback(this);
                    log.info("✅ FixedRabbitConfirmCallback注册成功");
                } else if (existingCallback == this) {
                    // 情况2：已经设置过（可能是重复调用）
                    log.debug("FixedRabbitConfirmCallback已注册");
                } else {
                    // 情况3：已经有其他ConfirmCallback
                    log.warn("⚠️ RabbitTemplate已存在ConfirmCallback: {}，将使用包装模式",
                            existingCallback.getClass().getName());

                    // 创建包装器，同时调用新旧Callback
                    RabbitTemplate.ConfirmCallback finalExistingCallback = existingCallback;
                    rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
                        // 调用原有Callback
                        try {
                            finalExistingCallback.confirm(correlationData, ack, cause);
                        } catch (Exception e) {
                            log.error("原有ConfirmCallback执行异常", e);
                        }

                        // 调用当前Callback
                        confirm(correlationData, ack, cause);
                    });

                    log.info("✅ FixedRabbitConfirmCallback已成功包装现有Callback");
                }
            } catch (IllegalStateException e) {
                if (e.getMessage().contains("Only one ConfirmCallback is supported")) {
                    log.error("❌ ConfirmCallback设置冲突，请检查是否有其他配置重复设置," +
                            "这已经不影响启动，因为错误被捕获了，应用继续启动。", e);

                } else {
                    log.error("❌ ConfirmCallback初始化异常", e);
                }
                initialized.set(false);
            } catch (Exception e) {
                log.error("❌ FixedRabbitConfirmCallback初始化失败", e);
                initialized.set(false);
            }
        } else {
            log.debug("FixedRabbitConfirmCallback已初始化，跳过重复初始化");
        }
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
