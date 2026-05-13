package com.aioveu.pay.aioveu12MqProducerPayment.service.impl;


import com.aioveu.pay.aioveu12MqProducerPayment.service.MQMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName: SimpleMQMetricsService
 * @Description TODO 临时指标服务实现
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 18:09
 * @Version 1.0
 **/
@Slf4j
@Service
public class SimpleMQMetricsService implements MQMetricsService {


    // 存储指标值
    private final ConcurrentHashMap<String, AtomicReference<Double>> gauges = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> counters = new ConcurrentHashMap<>();

    @Override
    public void recordGauge(String metricName, double value, String... tags) {
        try {
            String key = buildKey(metricName, tags);
            gauges.computeIfAbsent(key, k -> new AtomicReference<>(0.0))
                    .set(value);

            // 记录日志
            log.debug("【指标】{} = {} [tags: {}]", metricName, value, formatTags(tags));

        } catch (Exception e) {
            log.error("记录指标异常: {}", metricName, e);
        }
    }

    @Override
    public void recordCounter(String metricName, long increment, String... tags) {
        try {
            String key = buildKey(metricName, tags);
            counters.computeIfAbsent(key, k -> new AtomicLong(0))
                    .addAndGet(increment);

            // 记录日志
            log.debug("【计数器】{} += {} [tags: {}]", metricName, increment, formatTags(tags));

        } catch (Exception e) {
            log.error("记录计数器异常: {}", metricName, e);
        }
    }


    /**
     * 获取指标值
     */
    public double getGauge(String metricName, String... tags) {
        String key = buildKey(metricName, tags);
        AtomicReference<Double> gauge = gauges.get(key);
        return gauge != null ? gauge.get() : 0.0;
    }

    /**
     * 获取计数器值
     */
    public long getCounter(String metricName, String... tags) {
        String key = buildKey(metricName, tags);
        AtomicLong counter = counters.get(key);
        return counter != null ? counter.get() : 0L;
    }

    /**
     * 构建键
     */
    private String buildKey(String metricName, String[] tags) {
        if (tags == null || tags.length == 0) {
            return metricName;
        }

        StringBuilder key = new StringBuilder(metricName);
        for (int i = 0; i < tags.length; i++) {
            key.append(":").append(tags[i]);
        }
        return key.toString();
    }

    /**
     * 格式化标签
     */
    private String formatTags(String[] tags) {
        if (tags == null || tags.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tags.length; i += 2) {
            if (i + 1 < tags.length) {
                sb.append(tags[i]).append("=").append(tags[i + 1]);
                if (i + 2 < tags.length) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }


}
