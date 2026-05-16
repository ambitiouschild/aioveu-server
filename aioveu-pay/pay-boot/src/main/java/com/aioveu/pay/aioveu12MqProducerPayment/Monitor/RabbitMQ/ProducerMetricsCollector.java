package com.aioveu.pay.aioveu12MqProducerPayment.Monitor.RabbitMQ;


import cn.hutool.core.date.StopWatch;
import com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ.impl.MQMetricsService;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @ClassName: ProducerMetricsCollector
 * @Description TODO 生产端监控指标收集
 *                      ProducerMetricsCollector是生产端（支付服务）的监控指标收集器，
 *                      用于收集和记录消息发送的各种指标数据。
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 17:43
 * @Version 1.0
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerMetricsCollector {

    // 基础计数器
    private final LongAdder totalSendCount = new LongAdder();
    private final LongAdder successSendCount = new LongAdder();
    private final LongAdder failedSendCount = new LongAdder();

    // 响应时间统计
    private final LongAccumulator minResponseTimeAccumulator =
            new LongAccumulator(Math::min, Long.MAX_VALUE);
    private final LongAccumulator maxResponseTimeAccumulator =
            new LongAccumulator(Math::max, 0L);
    private final LongAdder totalResponseTime = new LongAdder();

    // 按Topic统计
    private final Map<String, TopicMetrics> topicMetrics = new ConcurrentHashMap<>();

    /**
     * 记录发送结果
     */
    public void recordSendResult(boolean success, long responseTime) {
        recordSendResult(success, responseTime, null);
    }

    /**
     * 记录发送结果（带Topic）
     */
    public void recordSendResult(boolean success, long responseTime, String topic) {
        // 更新全局统计
        totalSendCount.increment();
        if (success) {
            successSendCount.increment();
        } else {
            failedSendCount.increment();
        }

        minResponseTimeAccumulator.accumulate(responseTime);
        maxResponseTimeAccumulator.accumulate(responseTime);
        totalResponseTime.add(responseTime);

        // 按Topic统计
        if (topic != null) {
            topicMetrics.computeIfAbsent(topic, k -> new TopicMetrics())
                    .record(success, responseTime);
        }
    }

    /**
     * 获取监控指标
     */
    public ProducerMetrics getMetrics() {
        long total = totalSendCount.sum();
        long success = successSendCount.sum();
        long failed = failedSendCount.sum();

        double successRate = total > 0 ? (double) success / total * 100 : 0.0;
        double avgResponseTime = total > 0 ? (double) totalResponseTime.sum() / total : 0.0;

        // 先构建基本对象
        ProducerMetrics metrics = ProducerMetrics.builder()
                .totalSendCount(total)
                .successSendCount(success)
                .failedSendCount(failed)
                .successRate(successRate)
                .minResponseTime(getMinResponseTime())
                .maxResponseTime(getMaxResponseTime())
                .avgResponseTime(avgResponseTime)
                .currentTime(System.currentTimeMillis())
                .build();

        // 通过 setter 方法设置 topicMetrics
        metrics.setTopicMetrics(new HashMap<>(topicMetrics));

        return metrics;
    }

    private long getMinResponseTime() {
        long min = minResponseTimeAccumulator.get();
        return min == Long.MAX_VALUE ? 0 : min;
    }

    private long getMaxResponseTime() {
        return maxResponseTimeAccumulator.get();
    }

    /**
     * 获取指定Topic的指标
     */
    public ProducerMetrics getTopicMetrics(String topic) {
        TopicMetrics metrics = topicMetrics.get(topic);
        if (metrics == null) {
            return null;
        }

        return ProducerMetrics.builder()
                .totalSendCount(metrics.totalSendCount.sum())
                .successSendCount(metrics.successSendCount.sum())
                .failedSendCount(metrics.failedSendCount.sum())
                .successRate(metrics.getSuccessRate())
                .minResponseTime(metrics.getMinResponseTime())
                .maxResponseTime(metrics.getMaxResponseTime())
                .avgResponseTime(metrics.getAvgResponseTime())
                .currentTime(System.currentTimeMillis())
                .build();
    }

    /**
     * 重置所有统计
     */
    public void reset() {
        totalSendCount.reset();
        successSendCount.reset();
        failedSendCount.reset();
        minResponseTimeAccumulator.reset();
        maxResponseTimeAccumulator.reset();
        totalResponseTime.reset();
        topicMetrics.clear();
    }

    /**
     * 重置指定Topic的统计
     */
    public void resetTopic(String topic) {
        TopicMetrics metrics = topicMetrics.get(topic);
        if (metrics != null) {
            metrics.reset();
        }
    }

    /**
     * Topic级别的统计
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TopicMetrics {
        private final LongAdder totalSendCount = new LongAdder();
        private final LongAdder successSendCount = new LongAdder();
        private final LongAdder failedSendCount = new LongAdder();
        private final LongAdder totalResponseTime = new LongAdder();
        private final LongAccumulator minResponseTime =
                new LongAccumulator(Math::min, Long.MAX_VALUE);
        private final LongAccumulator maxResponseTime =
                new LongAccumulator(Math::max, 0L);

        public void record(boolean success, long responseTime) {
            totalSendCount.increment();
            if (success) {
                successSendCount.increment();
            } else {
                failedSendCount.increment();
            }
            minResponseTime.accumulate(responseTime);
            maxResponseTime.accumulate(responseTime);
            totalResponseTime.add(responseTime);
        }

        public double getSuccessRate() {
            long total = totalSendCount.sum();
            return total > 0 ? (double) successSendCount.sum() / total * 100 : 0.0;
        }

        public double getAvgResponseTime() {
            long total = totalSendCount.sum();
            return total > 0 ? (double) totalResponseTime.sum() / total : 0.0;
        }

        public long getMinResponseTime() {
            long min = minResponseTime.get();
            return min == Long.MAX_VALUE ? 0 : min;
        }

        public long getMaxResponseTime() {
            return maxResponseTime.get();
        }

        public void reset() {
            totalSendCount.reset();
            successSendCount.reset();
            failedSendCount.reset();
            totalResponseTime.reset();
            minResponseTime.reset();
            maxResponseTime.reset();
        }
    }
}
