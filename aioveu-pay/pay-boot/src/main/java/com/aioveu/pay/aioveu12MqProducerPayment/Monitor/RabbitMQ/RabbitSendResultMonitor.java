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
            stats.totalCount++;

            if (result.isSuccess()) {
                stats.successCount++;
                stats.totalCostTime += result.getCostTime();

                if (result.getCostTime() < stats.minCostTime) {
                    stats.minCostTime = result.getCostTime();
                }
                if (result.getCostTime() > stats.maxCostTime) {
                    stats.maxCostTime = result.getCostTime();
                }
            } else {
                stats.failureCount++;

                // 按失败原因统计
                if (result.getSendStatus() == SendStatus.TIMEOUT) {
                    stats.timeoutCount++;
                } else if (result.getSendStatus() == SendStatus.ROUTING_FAILED) {
                    stats.routingFailedCount++;
                } else if (result.getSendStatus() == SendStatus.CONFIRM_NACK) {
                    stats.nackCount++;
                } else {
                    stats.otherFailureCount++;
                }
            }
        }

        if (stats.successCount > 0) {
            stats.avgCostTime = stats.totalCostTime / stats.successCount;
        }
        if (stats.totalCount > 0) {
            stats.successRate = (double) stats.successCount / stats.totalCount * 100;
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

        // 找出最慢的10个
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
