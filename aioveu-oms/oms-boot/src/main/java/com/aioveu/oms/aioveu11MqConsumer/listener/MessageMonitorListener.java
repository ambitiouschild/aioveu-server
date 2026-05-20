package com.aioveu.oms.aioveu11MqConsumer.listener;


/**
 * @ClassName: MessageMonitorListener
 * @Description TODO 监控事件监听器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 20:38
 * @Version 1.0
 **/

import com.aioveu.event.model.vo.MessageSentEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 监控事件监听器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageMonitorListener {

    private final MeterRegistry meterRegistry;

    @Async
    @EventListener
    public void handleMessageSent(MessageSentEvent event) {
        try {
            // 1. 记录监控指标
            recordMetrics(event);

            // 2. 记录成功日志
            logSuccessMessage(event);

            // 3. 更新仪表盘
            updateDashboard(event);

        } catch (Exception e) {
            log.error("处理消息发送成功事件失败", e);
        }
    }

    /**
     * 记录监控指标
     */
    private void recordMetrics(MessageSentEvent event) {
        // 计数器：发送成功次数
        Counter successCounter = Counter.builder("rabbitmq.message.sent.total")
                .tag("tenant", event.getTenantId() != null ? event.getTenantId() : "unknown")
                .tag("messageType", event.getMessageType() != null ? event.getMessageType() : "unknown")
                .tag("exchange", event.getExchange() != null ? event.getExchange() : "unknown")
                .description("消息发送成功总数")
                .register(meterRegistry);
        successCounter.increment();

        // 计时器：发送耗时
        if (event.getCostTime() != null) {
            Timer.builder("rabbitmq.message.send.duration")
                    .tag("tenant", event.getTenantId() != null ? event.getTenantId() : "unknown")
                    .tag("messageType", event.getMessageType() != null ? event.getMessageType() : "unknown")
                    .description("消息发送耗时")
                    .register(meterRegistry)
                    .record(event.getCostTime(), java.util.concurrent.TimeUnit.MILLISECONDS);
        }

        // 记录消息大小
        if (event.getMessageSize() != null) {
            meterRegistry.summary("rabbitmq.message.size")
                    .tag("tenant", event.getTenantId() != null ? event.getTenantId() : "unknown")
                    .record(event.getMessageSize());
        }
    }

    private void logSuccessMessage(MessageSentEvent event) {
        if (log.isInfoEnabled()) {
            log.info("消息发送成功监控: messageId={}, tenant={}, type={}, exchange={}, cost={}ms, size={}bytes",
                    event.getMessageId(), event.getTenantId(), event.getMessageType(),
                    event.getExchange(), event.getCostTime(), event.getMessageSize());
        }
    }

    private void updateDashboard(MessageSentEvent event) {
        // 这里可以更新实时仪表盘
        // 例如：更新Redis中的实时统计
    }
}
