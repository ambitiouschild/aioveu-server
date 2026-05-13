package com.aioveu.oms.aioveu11MqConsumer.MQMonitorConsumer;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.protocol.admin.TopicOffset;
import org.apache.rocketmq.remoting.protocol.admin.TopicStatsTable;
import org.apache.rocketmq.remoting.protocol.body.ConsumerConnection;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName: OrderConsumerMQMonitor
 * @Description TODO OrderConsumerMonitor 订单微服务消费端监控
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 17:48
 * @Version 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumerMQMonitor {

    private final DefaultMQAdminExt mqAdminExt;
    private final MeterRegistry meterRegistry;

    @Value("${spring.application.name:order-service}")
    private String applicationName;

    @Value("${rocketmq.topics.payment-success:payment_success_topic}")
    private String paymentSuccessTopic;

    @Value("${rocketmq.consumer-group:order-service-payment-consumer}")
    private String consumerGroup;

    @Value("${order.consumer.monitor.backlog-threshold:1000}")
    private long backlogThreshold;

    @Value("${order.consumer.monitor.delay-threshold:10000}")
    private long delayThreshold;

    @Value("${order.consumer.monitor.failure-rate-threshold:5.0}")
    private double failureRateThreshold;

    @Value("${order.consumer.monitor.enabled:true}")
    private boolean monitorEnabled;

    // 业务指标
    private final AtomicLong totalConsumed = new AtomicLong(0);
    private final AtomicLong successConsumed = new AtomicLong(0);
    private final AtomicLong failedConsumed = new AtomicLong(0);
    private final AtomicLong retryCount = new AtomicLong(0);
    private final AtomicLong deadLetterCount = new AtomicLong(0);
    private final AtomicLong avgProcessTime = new AtomicLong(0);

    // 消费延迟统计
    private final Map<MessageQueue, Long> consumeDelays = new ConcurrentHashMap<>();

    // 最后告警时间
    private final Map<String, Long> lastAlertTime = new ConcurrentHashMap<>();
    private static final long ALERT_INTERVAL_MS = 5 * 60 * 1000; // 5分钟

    // 监控指标
    private Counter consumeSuccessCounter;
    private Counter consumeFailureCounter;
    private Counter retryCounter;
    private Counter deadLetterCounter;
    private Timer consumeTimer;

    @PostConstruct
    public void init() {
        if (!monitorEnabled) {
            return;
        }

        // 初始化指标
        consumeSuccessCounter = Counter.builder("order.consumer.messages.total")
                .description("消费消息总数")
                .tags("status", "success")
                .register(meterRegistry);

        consumeFailureCounter = Counter.builder("order.consumer.messages.total")
                .description("消费消息总数")
                .tags("status", "failed")
                .register(meterRegistry);

        retryCounter = Counter.builder("order.consumer.retry.total")
                .description("消息重试总数")
                .register(meterRegistry);

        deadLetterCounter = Counter.builder("order.consumer.deadletter.total")
                .description("死信消息总数")
                .register(meterRegistry);

        consumeTimer = Timer.builder("order.consumer.process.time")
                .description("消息处理耗时")
                .register(meterRegistry);

        // 注册Gauge指标
        Gauge.builder("order.consumer.success.rate", this, monitor ->
                        monitor.getSuccessRate())
                .description("消费成功率")
                .register(meterRegistry);

        Gauge.builder("order.consumer.backlog", this, monitor ->
                        monitor.getCurrentBacklog())
                .description("消息积压数")
                .register(meterRegistry);

        Gauge.builder("order.consumer.delay", this, monitor ->
                        monitor.getMaxConsumeDelay())
                .description("最大消费延迟")
                .register(meterRegistry);

        log.info("订单消费端监控初始化完成");
    }

    /**
     * 监控任务 - 每分钟执行一次
     */
    @Scheduled(fixedDelay = 60000)
    public void monitorOrderConsumer() {
        if (!monitorEnabled) {
            return;
        }

        try {
            log.debug("【订单消费端】开始执行监控任务...");

            // 1. 检查消费者连接状态
            checkConsumerConnection();

            // 2. 检查消息积压
            checkMessageBacklog();

            // 3. 检查消费延迟
            checkConsumeDelay();

            // 4. 检查死信队列
            checkDeadLetterQueue();

            // 5. 检查消费成功率
            checkConsumeSuccessRate();

            // 6. 记录业务指标
            recordBusinessMetrics();

            // 7. 生成监控报告
            generateMonitorReport();

            log.debug("【订单消费端】监控任务执行完成");

        } catch (Exception e) {
            log.error("【订单消费端】监控任务异常", e);
        }
    }

    /**
     * 检查消费者连接状态
     */
    private void checkConsumerConnection() {
        try {
            ConsumerConnection connection = mqAdminExt.examineConsumerConnectionInfo(consumerGroup);

            if (connection == null) {
                sendAlert("消费者连接告警",
                        String.format("消费者组 %s 无连接", consumerGroup),
                        "CONSUMER_NO_CONNECTION");
                return;
            }

            int connectionCount = connection.getConnectionSet().size();

            // 记录指标
            meterRegistry.gauge("order.consumer.connection.count", connectionCount);

            if (connectionCount == 0) {
                sendAlert("消费者连接告警",
                        String.format("消费者组 %s 无活跃连接", consumerGroup),
                        "CONSUMER_NO_CONNECTION");
            } else if (connectionCount > 1) {
                // 多个消费者实例，记录实例数
                log.debug("消费者组 {} 有 {} 个连接", consumerGroup, connectionCount);
            }

        } catch (Exception e) {
            log.warn("检查消费者连接失败: {}", consumerGroup, e);
        }
    }

    /**
     * 检查消息积压
     */
    private void checkMessageBacklog() {
        try {
            TopicStatsTable stats = mqAdminExt.examineTopicStats(paymentSuccessTopic);

            long totalBacklog = 0;
            Map<String, Long> queueBacklogs = new HashMap<>();

            for (Map.Entry<MessageQueue, TopicOffset> entry : stats.getOffsetTable().entrySet()) {
                MessageQueue mq = entry.getKey();
                TopicOffset offset = entry.getValue();

                // 计算队列积压
                long backlog = offset.getMaxOffset() - offset.getMinOffset();
                totalBacklog += backlog;
                queueBacklogs.put(mq.getBrokerName() + "-" + mq.getQueueId(), backlog);
            }

            // 记录总积压
            meterRegistry.gauge("order.consumer.topic.backlog", totalBacklog);

            // 记录每个队列的积压
            for (Map.Entry<String, Long> entry : queueBacklogs.entrySet()) {
                meterRegistry.gauge("order.consumer.queue.backlog", entry.getValue(),
                        "queue", entry.getKey());
            }

            // 检查积压阈值
            if (totalBacklog > backlogThreshold) {
                sendAlert("消费积压告警",
                        String.format("Topic: %s, 积压消息数: %d (阈值: %d)",
                                paymentSuccessTopic, totalBacklog, backlogThreshold),
                        "BACKLOG_HIGH");
            }

        } catch (Exception e) {
            log.warn("检查消息积压失败: {}", paymentSuccessTopic, e);
        }
    }

    /**
     * 检查消费延迟
     */
    private void checkConsumeDelay() {
        try {
            // 获取消费者偏移量
            long consumerOffset = getConsumerOffset();

            // 获取Topic最大偏移量
            long maxOffset = getTopicMaxOffset();

            if (maxOffset > 0) {
                long delay = maxOffset - consumerOffset;

                // 记录指标
                meterRegistry.gauge("order.consumer.message.delay", delay);

                // 检查延迟阈值
                if (delay > delayThreshold) {
                    sendAlert("消费延迟告警",
                            String.format("消费者组: %s, Topic: %s, 延迟消息数: %d (阈值: %d)",
                                    consumerGroup, paymentSuccessTopic, delay, delayThreshold),
                            "DELAY_HIGH");
                }
            }

        } catch (Exception e) {
            log.warn("检查消费延迟失败", e);
        }
    }

    /**
     * 检查死信队列
     */
    private void checkDeadLetterQueue() {
        try {
            String dlqTopic = "%DLQ%" + consumerGroup;

            // 检查死信队列是否存在
            try {
                TopicStatsTable dlqStats = mqAdminExt.examineTopicStats(dlqTopic);
                long dlqCount = dlqStats.getOffsetTable().values().stream()
                        .mapToLong(offset -> offset.getMaxOffset() - offset.getMinOffset())
                        .sum();

                // 记录指标
                deadLetterCount.set(dlqCount);
                meterRegistry.gauge("order.consumer.deadletter.count", dlqCount);

                // 有死信就告警
                if (dlqCount > 0) {
                    sendAlert("死信队列告警",
                            String.format("消费者组: %s, 死信消息数: %d", consumerGroup, dlqCount),
                            "DLQ_NOT_EMPTY");
                }

            } catch (Exception e) {
                // 死信队列不存在是正常的
                if (!e.getMessage().contains("CODE: 206")) {
                    log.debug("死信队列 {} 不存在", dlqTopic);
                }
            }

        } catch (Exception e) {
            log.warn("检查死信队列失败", e);
        }
    }

    /**
     * 检查消费成功率
     */
    private void checkConsumeSuccessRate() {
        long total = totalConsumed.get();
        long success = successConsumed.get();

        if (total > 0) {
            double successRate = (success * 100.0) / total;
            double failureRate = 100.0 - successRate;

            // 记录指标
            meterRegistry.gauge("order.consumer.success.rate.percent", successRate);
            meterRegistry.gauge("order.consumer.failure.rate.percent", failureRate);

            // 检查失败率阈值
            if (failureRate > failureRateThreshold) {
                sendAlert("消费失败率告警",
                        String.format("消费者组: %s, 失败率: %.2f%% (阈值: %.1f%%)",
                                consumerGroup, failureRate, failureRateThreshold),
                        "FAILURE_RATE_HIGH");
            }
        }
    }

    /**
     * 记录业务处理结果
     */
    public void recordConsumeResult(String orderNo, String messageId,
                                    boolean success, long processTimeMs,
                                    String errorMsg) {
        if (!monitorEnabled) {
            return;
        }

        try {
            // 更新计数
            totalConsumed.incrementAndGet();

            if (success) {
                successConsumed.incrementAndGet();
                consumeSuccessCounter.increment();
            } else {
                failedConsumed.incrementAndGet();
                consumeFailureCounter.increment();
            }

            // 记录处理时间
            consumeTimer.record(processTimeMs, TimeUnit.MILLISECONDS);

            // 计算平均处理时间
            long currentAvg = avgProcessTime.get();
            long newAvg = (currentAvg + processTimeMs) / 2;
            avgProcessTime.set(newAvg);

            // 记录到指标
            meterRegistry.gauge("order.consumer.process.time.avg", newAvg);

        } catch (Exception e) {
            log.error("记录消费结果异常", e);
        }
    }

    /**
     * 记录重试
     */
    public void recordRetry(String messageId, int retryCount) {
        if (!monitorEnabled) {
            return;
        }

        this.retryCount.incrementAndGet();
        retryCounter.increment();

        // 记录重试分布
        meterRegistry.counter("order.consumer.retry.count",
                        "retry_times", String.valueOf(retryCount))
                .increment();
    }

    /**
     * 记录死信消息
     */
    public void recordDeadLetter(String messageId, String orderNo, String reason) {
        if (!monitorEnabled) {
            return;
        }

        deadLetterCount.incrementAndGet();
        deadLetterCounter.increment();

        // 记录死信原因
        meterRegistry.counter("order.consumer.deadletter.reason",
                        "reason", reason)
                .increment();
    }

    /**
     * 记录业务指标
     */
    private void recordBusinessMetrics() {
        try {
            // 记录活跃消费者数
            meterRegistry.gauge("order.consumer.active",
                    getActiveConsumerCount());

            // 记录处理速率（消息/分钟）
            long total = totalConsumed.get();
            meterRegistry.gauge("order.consumer.rate.per.minute",
                    getConsumeRatePerMinute());

        } catch (Exception e) {
            log.warn("记录业务指标异常", e);
        }
    }

    /**
     * 生成监控报告
     */
    private void generateMonitorReport() {
        long total = totalConsumed.get();
        long success = successConsumed.get();
        long failed = failedConsumed.get();
        double successRate = total > 0 ? (success * 100.0 / total) : 100.0;

        // 记录到日志
        log.info("""
            【订单消费端监控报告】
            消费者组: {}
            总消费消息数: {}
            成功消费数: {}
            失败消费数: {}
            成功率: {:.2f}%
            平均处理时间: {}ms
            重试次数: {}
            死信消息: {}
            当前积压: {}
            最大延迟: {}
            """,
                consumerGroup, total, success, failed, successRate,
                avgProcessTime.get(), retryCount.get(), deadLetterCount.get(),
                getCurrentBacklog(), getMaxConsumeDelay());
    }

    /**
     * 发送告警（带防重）
     */
    private void sendAlert(String title, String message, String alertKey) {
        long now = System.currentTimeMillis();
        Long lastTime = lastAlertTime.get(alertKey);

        // 检查是否在防重间隔内
        if (lastTime != null && (now - lastTime) < ALERT_INTERVAL_MS) {
            log.debug("告警防重: {}, 上次告警时间: {}", alertKey, new Date(lastTime));
            return;
        }

        try {
            // 这里调用你的告警服务
            // alertService.sendAlert(title, message);
            log.warn("【告警】{} - {}", title, message);
            lastAlertTime.put(alertKey, now);
        } catch (Exception e) {
            log.error("发送告警失败", e);
        }
    }

    /**
     * 获取消费者偏移量
     */
    private long getConsumerOffset() throws Exception {
        try {
            // 获取所有队列的消费者偏移量
            return mqAdminExt.minOffset(paymentSuccessTopic, consumerGroup, 0);
        } catch (Exception e) {
            log.warn("获取消费者偏移量失败", e);
            return 0;
        }
    }

    /**
     * 获取Topic最大偏移量
     */
    private long getTopicMaxOffset() throws Exception {
        try {
            TopicStatsTable stats = mqAdminExt.examineTopicStats(paymentSuccessTopic);
            return stats.getOffsetTable().values().stream()
                    .mapToLong(TopicOffset::getMaxOffset)
                    .max()
                    .orElse(0);
        } catch (Exception e) {
            log.warn("获取Topic最大偏移量失败", e);
            return 0;
        }
    }

    /**
     * 获取活跃消费者数
     */
    private int getActiveConsumerCount() {
        try {
            ConsumerConnection connection = mqAdminExt.examineConsumerConnectionInfo(consumerGroup);
            return connection != null ? connection.getConnectionSet().size() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取消费成功率
     */
    public double getSuccessRate() {
        long total = totalConsumed.get();
        long success = successConsumed.get();
        return total > 0 ? (success * 100.0 / total) : 100.0;
    }

    /**
     * 获取当前积压
     */
    public long getCurrentBacklog() {
        try {
            return getTopicMaxOffset() - getConsumerOffset();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取最大消费延迟
     */
    public long getMaxConsumeDelay() {
        return getCurrentBacklog();  // 简化处理
    }

    /**
     * 获取消费速率（消息/分钟）
     */
    public long getConsumeRatePerMinute() {
        // 这里可以记录时间窗口内的消费数
        // 简化实现，返回平均速率
        long total = totalConsumed.get();
        // 假设应用启动了10分钟
        return total / 10;
    }
}
