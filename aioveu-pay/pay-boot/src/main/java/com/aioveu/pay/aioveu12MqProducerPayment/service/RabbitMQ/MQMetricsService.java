package com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ.impl;


/**
 * @ClassName: MQMetricsService
 * @Description TODO  MQMetricsService
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 17:29
 * @Version 1.0
 **/

public interface MQMetricsService {

    /**
     * 记录指标
     */
    void recordGauge(String metricName, double value, String... tags);

    /**
     * 记录计数器
     */
    void recordCounter(String metricName, long increment, String... tags);

}
