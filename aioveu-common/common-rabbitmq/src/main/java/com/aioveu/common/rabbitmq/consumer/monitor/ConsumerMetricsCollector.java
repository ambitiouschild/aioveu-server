package com.aioveu.common.rabbitmq.consumer.monitor;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName: ConsumerMetricsCollector
 * @Description TODO 消费端监控指标收集
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 17:44
 * @Version 1.0
 **/
/**
 * 消费端监控指标收集器
 * 使用Micrometer收集RabbitMQ消费端的关键指标
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerMetricsCollector {

    private final MeterRegistry meterRegistry;

    // 统计指标缓存
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicLong totalCostTime = new AtomicLong(0);

    // 标签（用于维度区分）
    private final Tags defaultTags = Tags.of(
            "application", "rabbitmq-consumer",
            "environment", "production"
    );

    /**
     * 记录消费结果
     *
     * @param success 是否成功
     * @param costTime 消费耗时（毫秒）
     */
    public void recordConsumeResult(boolean success, long costTime) {
        try {
            // 1. 记录消费成功率指标
            if (success) {
                successCount.incrementAndGet();
            } else {
                failureCount.incrementAndGet();
            }

            // 记录Gauge指标
            Gauge.builder("mq.consumer.consume.success_rate",
                            () -> calculateSuccessRate())
                    .tags(defaultTags)
                    .description("消息消费成功率")
                    .baseUnit("percent")
                    .register(meterRegistry);

            // 2. 记录消费延迟指标
            if (costTime > 0) {
                totalCostTime.addAndGet(costTime);

                Gauge.builder("mq.consumer.consume.latency",
                                () -> calculateAverageLatency())
                        .tags(defaultTags)
                        .description("平均消费延迟")
                        .baseUnit("milliseconds")
                        .register(meterRegistry);

                // 记录具体耗时
                meterRegistry.timer("mq.consumer.consume.duration",
                                defaultTags.and("success", String.valueOf(success)))
                        .record(costTime, java.util.concurrent.TimeUnit.MILLISECONDS);
            }

            // 3. 记录消费次数指标
            Counter.builder("mq.consumer.consume.total")
                    .tags(defaultTags.and("success", String.valueOf(success)))
                    .description("消息消费总次数")
                    .register(meterRegistry)
                    .increment();

            // 4. 记录QPS（每秒处理量）
            Gauge.builder("mq.consumer.consume.qps",
                            this::calculateCurrentQPS)
                    .tags(defaultTags)
                    .description("每秒处理消息数")
                    .register(meterRegistry);

        } catch (Exception e) {
            // 监控组件不应该影响主业务流程
            log.error("记录消费指标异常", e);
        }
    }

    /**
     * 计算成功率
     */
    private double calculateSuccessRate() {
        int total = successCount.get() + failureCount.get();
        if (total == 0) {
            return 1.0;  // 100%
        }
        return (double) successCount.get() / total;
    }

    /**
     * 计算平均延迟
     */
    private double calculateAverageLatency() {
        int totalSuccess = successCount.get();
        if (totalSuccess == 0) {
            return 0.0;
        }
        return (double) totalCostTime.get() / totalSuccess;
    }

    /**
     * 计算当前QPS（需要定时调用）
     */
    private double calculateCurrentQPS() {
        // 这里可以加入更复杂的QPS计算逻辑
        // 简化版本：返回最近一分钟的平均值
        return 0.0;
    }

    /**
     * 获取监控指标摘要
     */
    public ConsumerMetricsSummary getSummary() {
        int total = successCount.get() + failureCount.get();
        double successRate = calculateSuccessRate();
        double avgLatency = calculateAverageLatency();

        return ConsumerMetricsSummary.builder()
                .totalConsumed(total)
                .successCount(successCount.get())
                .failureCount(failureCount.get())
                .successRate(successRate)
                .averageLatencyMs(avgLatency)
                .build();
    }

    /**
     * 重置统计（用于测试或定时清零）
     */
    public void reset() {
        successCount.set(0);
        failureCount.set(0);
        totalCostTime.set(0);
        log.info("消费者指标已重置");
    }

    /**
     * 指标摘要DTO
     */
    @lombok.Data
    @lombok.Builder
    public static class ConsumerMetricsSummary {
        private int totalConsumed;
        private int successCount;
        private int failureCount;
        private double successRate;
        private double averageLatencyMs;
    }
}
