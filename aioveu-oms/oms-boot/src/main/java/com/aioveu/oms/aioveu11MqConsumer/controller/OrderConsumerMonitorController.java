package com.aioveu.oms.aioveu11MqConsumer.controller;


import com.aioveu.oms.aioveu11MqConsumer.MQMonitorConsumer.OrderConsumerMQMonitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: OrderConsumerMonitorController
 * @Description TODO  监控端点暴露
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 18:00
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/api/monitor/order-consumer")
@RequiredArgsConstructor
public class OrderConsumerMonitorController {

    private final OrderConsumerMQMonitor orderConsumerMQMonitor;

    /**
     * 获取监控指标
     */
    @GetMapping("/metrics")
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        metrics.put("consumer_group", "order-service-payment-consumer");
        metrics.put("topic", "payment_success_topic");
        metrics.put("success_rate", orderConsumerMQMonitor.getSuccessRate());
        metrics.put("current_backlog", orderConsumerMQMonitor.getCurrentBacklog());
        metrics.put("max_delay", orderConsumerMQMonitor.getMaxConsumeDelay());

        return metrics;
    }

    /**
     * 手动触发监控检查
     */
    @PostMapping("/check")
    public String triggerMonitor() {
        orderConsumerMQMonitor.monitorOrderConsumer();
        return "监控检查已触发";
    }
}
