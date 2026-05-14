package com.aioveu.pay.aioveu12MqProducerPayment.Monitor.RabbitMQ;


import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatus;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendResult;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.SendReport;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.SendStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: RabbitSendResultMonitor
 * @Description TODO RabbitSendResult监控工具
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 17:08
 * @Version 1.0
 **/
@Component
@Slf4j
public class RabbitSendResultMonitor {

    /**
     * 分析发送结果统计
     */
    public SendStats analyzeResults(List<RabbitSendResult> results) {
        SendStats stats = new SendStats();

        for (RabbitSendResult result : results) {
            stats.incrementTotalCount();

            if (result.isSuccess()) {
                stats.incrementSuccessCount();
                stats.addCostTime(result.getCostTime());

                if (result.getCostTime() < stats.getMinCostTime()) {
                    stats.setMinCostTime(result.getCostTime());
                }
                if (result.getCostTime() > stats.getMaxCostTime()) {
                    stats.setMaxCostTime(result.getCostTime());
                }
            } else {
                stats.incrementFailureCount();

                // 按失败原因统计
                if (result.getSendStatus() == SendStatus.TIMEOUT) {
                    stats.incrementTimeoutCount();
                } else if (result.getSendStatus() == SendStatus.ROUTING_FAILED) {
                    stats.incrementRoutingFailedCount();
                } else if (result.getSendStatus() == SendStatus.CONFIRM_NACK) {
                    stats.incrementNackCount();
                } else {
                    stats.incrementOtherFailureCount();
                }
            }
        }

        // 计算平均值
        if (stats.getSuccessCount() > 0) {
            long avgCostTime = stats.getTotalCostTime() / stats.getSuccessCount();
            stats.setAvgCostTime(avgCostTime);
        }
        // 计算成功率
        if (stats.getTotalCount() > 0) {
            double successRate = (double) stats.getSuccessCount() / stats.getTotalCount() * 100;
            stats.setSuccessRate(successRate);
        }

        return stats;
    }

    /**
     * 生成发送报告
     */
    public SendReport generateReport(List<RabbitSendResult> results) {
        SendStats stats = analyzeResults(results);

        SendReport report = new SendReport();
        report.setStats(stats);
        report.setStartTime(new Date());
        report.setEndTime(new Date());
        report.setTotalResults(results.size());

        // 找出最慢的10个成功消息
        List<RabbitSendResult> slowest = results.stream()
                .filter(RabbitSendResult::isSuccess)
                .sorted((r1, r2) -> Long.compare(r2.getCostTime(), r1.getCostTime()))
                .limit(10)
                .collect(Collectors.toList());
        report.setSlowestMessages(slowest);

        // 收集所有失败消息
        List<RabbitSendResult> failures = results.stream()
                .filter(r -> !r.isSuccess())
                .collect(Collectors.toList());
        report.setFailedMessages(failures);

        return report;
    }

}
