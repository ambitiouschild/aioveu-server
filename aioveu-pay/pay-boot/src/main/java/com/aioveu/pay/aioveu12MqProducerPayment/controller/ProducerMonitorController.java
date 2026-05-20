package com.aioveu.pay.aioveu12MqProducerPayment.controller;


import com.aioveu.common.rabbitmq.producer.monitor.ProducerMetrics;
import com.aioveu.common.rabbitmq.producer.monitor.ProducerMetricsCollector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//您有两个同名的 ProducerMetrics类，分别来自不同的包
//import org.apache.kafka.clients.producer.internals.ProducerMetrics;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: ProducerMonitorController
 * @Description TODO 在 Controller 中查看监控数据
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 19:07
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/api/monitor/producer")
@RequiredArgsConstructor
public class ProducerMonitorController {

    private final ProducerMetricsCollector metricsCollector;

    /**
     * 获取生产端监控指标
     */
    @GetMapping("/metrics")
    public ProducerMetrics getMetrics() {
        return metricsCollector.getMetrics();
    }

    /**
     * 测试发送消息
     */
    @PostMapping("/test-send")
    public String testSend() {
        long startTime = System.currentTimeMillis();
        boolean success = false;

        try {
            // 模拟发送逻辑
            success = simulateSend();
            return success ? "发送成功" : "发送失败";

        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            metricsCollector.recordSendResult(success, costTime);
        }
    }

    private boolean simulateSend() {
        // 模拟发送逻辑
        return Math.random() > 0.3;  // 70%成功率
    }
}
