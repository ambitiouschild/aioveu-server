package com.aioveu.common.rabbitmq.producer.monitor;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @ClassName: ProducerMetrics
 * @Description TODO 生产者监控指标
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/16 15:46
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProducerMetrics {

    // 发送数量统计
    private Long totalSendCount;
    private Long successSendCount;
    private Long failedSendCount;

    // 成功率
    private Double successRate;

    // 响应时间（毫秒）
    private Long minResponseTime;
    private Long maxResponseTime;
    private Double avgResponseTime;

    // 按Topic的统计
    private Map<String, ProducerMetricsCollector.TopicMetrics> topicMetrics;

    // 时间戳
    private Long currentTime;
}
