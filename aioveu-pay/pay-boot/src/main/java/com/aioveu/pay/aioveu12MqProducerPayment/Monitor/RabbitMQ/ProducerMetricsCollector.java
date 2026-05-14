package com.aioveu.pay.aioveu12MqProducerPayment.Monitor.RabbitMQ;


import cn.hutool.core.date.StopWatch;
import com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ.impl.MQMetricsService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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

    private final MQMetricsService metricsService;



    /**
     * 生产端监控指标收集（Micrometer 版本）
     */

    private final MeterRegistry meterRegistry;

    // 计数器
    private Counter totalSendCounter;
    private Counter successSendCounter;
    private Counter failedSendCounter;
    private Timer sendTimer;


    public void recordSendResult(boolean success, long costTime) {
        try {
            // 记录发送成功率
            metricsService.recordGauge("mq.producer.send.success_rate",
                    success ? 1.0 : 0.0,
                    "result", success ? "success" : "failed");

            // 记录发送延迟
            metricsService.recordGauge("mq.producer.send.latency",
                    (double) costTime);

            // 记录发送次数
            metricsService.recordCounter("mq.producer.send.total", 1,
                    "result", success ? "success" : "failed");

            log.debug("记录发送指标: success={}, cost={}ms", success, costTime);

        } catch (Exception e) {
            log.error("记录发送指标异常", e);
        }
    }

    /**
     * 记录发送结果
     */
    public void recordSendResult(boolean success, long costTime, String topic) {
        try {
            // 初始化计数器
            if (totalSendCounter == null) {
                initCounters();
            }

            // 记录总发送次数
            totalSendCounter.increment();

            // 记录成功/失败次数
            if (success) {
                successSendCounter.increment();
            } else {
                failedSendCounter.increment();
            }

            // 记录发送耗时
            sendTimer.record(costTime, TimeUnit.MILLISECONDS);

            // 记录成功率（Gauge）
            meterRegistry.gauge("mq.producer.send.success_rate",
                    getSuccessRate());

            // 记录延迟
            meterRegistry.gauge("mq.producer.send.latency_avg_ms",
                    sendTimer.mean(TimeUnit.MILLISECONDS));

            // 记录到日志
            log.debug("记录发送指标: topic={}, success={}, cost={}ms, successRate={:.2f}%",
                    topic, success, costTime, getSuccessRate() * 100);

        } catch (Exception e) {
            log.error("记录发送指标异常", e);
        }
    }

    /**
     * 记录发送开始
     */
    public StopWatch startSend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        return stopWatch;
    }

    /**
     * 记录发送结束
     */
    public void endSend(StopWatch stopWatch, boolean success, String topic) {
        if (stopWatch != null && stopWatch.isRunning()) {
            stopWatch.stop();
            long costTime = stopWatch.getTotalTimeMillis();
            recordSendResult(success, costTime, topic);
        }
    }

    /**
     * 初始化计数器
     */
    private void initCounters() {
        totalSendCounter = Counter.builder("mq.producer.send.total")
                .description("消息发送总数")
                .register(meterRegistry);

        successSendCounter = Counter.builder("mq.producer.send.success")
                .description("发送成功总数")
                .register(meterRegistry);

        failedSendCounter = Counter.builder("mq.producer.send.failed")
                .description("发送失败总数")
                .register(meterRegistry);

        sendTimer = Timer.builder("mq.producer.send.duration")
                .description("消息发送耗时")
                .register(meterRegistry);
    }

    /**
     * 获取成功率
     */
    private double getSuccessRate() {
        double total = totalSendCounter.count();
        double success = successSendCounter.count();
        return total > 0 ? (success / total) : 0.0;
    }
}
