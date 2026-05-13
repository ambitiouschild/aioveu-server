package com.aioveu.oms.aioveu11MqConsumer.controller;


import org.springframework.stereotype.Service;

/**
 * @ClassName: ConsumerMetricsCollector
 * @Description TODO 消费端监控指标收集
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 17:44
 * @Version 1.0
 **/
@Service
public class ConsumerMetricsCollector {

    public void recordConsumeResult(boolean success, long costTime) {
        // 记录消费成功率
        metrics.recordGauge("mq.consumer.consume.success_rate", success ? 1 : 0);

        // 记录消费延迟
        metrics.recordGauge("mq.consumer.consume.latency", costTime);

        // 记录消费次数
        metrics.recordCounter("mq.consumer.consume.total", 1);
    }
}
