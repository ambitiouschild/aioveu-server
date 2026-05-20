package com.aioveu.common.rabbitmq.producer.service.impl;


import com.aioveu.common.rabbitmq.producer.monitor.RabbitSendResultMonitor;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendResult;
import com.aioveu.common.rabbitmq.producer.model.vo.SendReport;
import com.aioveu.common.rabbitmq.producer.model.vo.SendStats;
import com.aioveu.common.rabbitmq.producer.service.RabbitSendResultshandle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: RabbitSendResultshandleImpl
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 17:30
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitSendResultshandleImpl implements RabbitSendResultshandle {


    private final RabbitSendResultMonitor resultMonitor;

    /**
     * 正确访问统计信息
     */
    @Override
    public void processResults(List<RabbitSendResult> results) {
        // 1. 使用analyzeResults方法获取统计
        SendStats stats = resultMonitor.analyzeResults(results);

        // 2. 正确访问统计信息（通过getter方法）
        log.info("发送统计: 总数={}, 成功={}, 失败={}, 成功率={}%",
                stats.getTotalCount(),
                stats.getSuccessCount(),
                stats.getFailureCount(),
                String.format("%.2f", stats.getSuccessRate()));

        log.info("平均耗时: {}ms, 最小耗时: {}ms, 最大耗时: {}ms",
                stats.getAvgCostTime(), stats.getMinCostTime(), stats.getMaxCostTime());

        log.info("失败分布: 超时={}, 路由失败={}, NACK={}, 其他={}",
                stats.getTimeoutCount(), stats.getRoutingFailedCount(),
                stats.getNackCount(), stats.getOtherFailureCount());

        // 3. 使用便捷方法
        log.info(stats.toSummaryString());

        // 4. 生成完整报告
        SendReport report = resultMonitor.generateReport(results);
        log.info("\n=== 发送报告 ===\n{}", report.toSummary());

        // 5. 处理失败消息
        handleFailedMessages(report.getFailedMessages());
    }

    /**
     * 批量处理结果
     */
    @Override
    public void handleBatchSendResults(List<RabbitSendResult> results) {
        // 正确访问统计数据
        int successCount = 0;
        int failureCount = 0;

        for (RabbitSendResult result : results) {
            if (result.isSuccess()) {
                successCount++;
                // 处理成功消息
                onSendSuccess(result);
            } else {
                failureCount++;
                // 处理失败消息
                onSendFailure(result);
            }
        }

        log.info("批量发送完成: 成功={}, 失败={}, 总计={}",
                successCount, failureCount, results.size());
    }

    /**
     * 实时监控发送结果
     */
    @Override
    public void realTimeMonitoring() {
        // 模拟一些发送结果
        List<RabbitSendResult> results = new ArrayList<>();

        // 添加一些成功结果
        results.add(RabbitSendResult.success(
                "msg-001", "corr-001", 150, "order.exchange", "order.create"
        ));
        results.add(RabbitSendResult.success(
                "msg-002", "corr-002", 230, "order.exchange", "order.create"
        ));

        // 添加一些失败结果
        results.add(RabbitSendResult.timeout(
                "msg-003", 5000, "order.exchange", "order.create"
        ));

        // 获取监控报告
        SendReport report = resultMonitor.generateReport(results);

        // 输出监控信息
        System.out.println("=== 实时监控报告 ===");
        System.out.println("发送总数: " + report.getTotalResults());
        System.out.println("成功率: " + report.getStats().getSuccessRate() + "%");
        System.out.println("平均耗时: " + report.getStats().getAvgCostTime() + "ms");

        // 输出失败详情
        if (!report.getFailedMessages().isEmpty()) {
            System.out.println("\n=== 失败消息列表 ===");
            for (RabbitSendResult failed : report.getFailedMessages()) {
                System.out.println("消息ID: " + failed.getMessageId() +
                        ", 状态: " + failed.getSendStatus() +
                        ", 错误: " + failed.getErrorMessage());
            }
        }
    }

    /**
     * 性能分析
     */
    @Override
    public void analyzePerformance(List<RabbitSendResult> results) {
        SendStats stats = resultMonitor.analyzeResults(results);

        // 性能分级
        int excellent = 0;  // < 100ms
        int good = 0;       // 100-500ms
        int average = 0;    // 500-1000ms
        int slow = 0;       // > 1000ms

        for (RabbitSendResult result : results) {
            if (result.isSuccess()) {
                long costTime = result.getCostTime();
                if (costTime < 100) {
                    excellent++;
                } else if (costTime < 500) {
                    good++;
                } else if (costTime < 1000) {
                    average++;
                } else {
                    slow++;
                }
            }
        }

        log.info("性能分析: 优秀(<100ms)={}, 良好(100-500ms)={}, 一般(500-1000ms)={}, 慢(>1000ms)={}",
                excellent, good, average, slow);

        // 找出性能瓶颈
        List<RabbitSendResult> slowMessages = results.stream()
                .filter(r -> r.isSuccess() && r.getCostTime() > 1000)
                .sorted((r1, r2) -> Long.compare(r2.getCostTime(), r1.getCostTime()))
                .limit(10)
                .collect(Collectors.toList());

        if (!slowMessages.isEmpty()) {
            log.warn("发现{}个慢消息(>1000ms):", slowMessages.size());
            for (RabbitSendResult slowMsg : slowMessages) {
                log.warn("  - {}: {}ms, exchange={}, routingKey={}",
                        slowMsg.getMessageId(), slowMsg.getCostTime(),
                        slowMsg.getExchange(), slowMsg.getRoutingKey());
            }
        }
    }

    private void onSendSuccess(RabbitSendResult result) {
        // 处理成功消息
        log.debug("消息发送成功: {}", result.getMessageId());
    }

    private void onSendFailure(RabbitSendResult result) {
        // 处理失败消息
        log.error("消息发送失败: {}, 错误: {}",
                result.getMessageId(), result.getErrorMessage());
    }

    private void handleFailedMessages(List<RabbitSendResult> failedMessages) {
        if (failedMessages.isEmpty()) {
            return;
        }

        log.warn("开始处理{}个失败消息", failedMessages.size());

        for (RabbitSendResult failed : failedMessages) {
            try {
                // 根据失败原因采取不同策略
                switch (failed.getSendStatus()) {
                    case TIMEOUT:
                        handleTimeoutMessage(failed);
                        break;
                    case ROUTING_FAILED:
                        handleRoutingFailedMessage(failed);
                        break;
                    case CONFIRM_NACK:
                        handleNackMessage(failed);
                        break;
                    default:
                        handleOtherFailureMessage(failed);
                }
            } catch (Exception e) {
                log.error("处理失败消息异常: {}", failed.getMessageId(), e);
            }
        }
    }

    private void handleTimeoutMessage(RabbitSendResult result) {
        // 超时消息处理：重试
        log.info("重试超时消息: {}", result.getMessageId());
        // 实现重试逻辑
    }

    private void handleRoutingFailedMessage(RabbitSendResult result) {
        // 路由失败处理：检查路由配置
        log.error("路由失败消息: {}, exchange={}, routingKey={}, replyCode={}",
                result.getMessageId(), result.getReturnedExchange(),
                result.getReturnedRoutingKey(), result.getReplyCode());
        // 发送告警
    }

    private void handleNackMessage(RabbitSendResult result) {
        // NACK处理：检查Broker状态
        log.error("Broker拒绝消息: {}, cause={}",
                result.getMessageId(), result.getAckCause());
    }

    private void handleOtherFailureMessage(RabbitSendResult result) {
        // 其他失败处理
        log.error("其他失败消息: {}, error={}",
                result.getMessageId(), result.getErrorMessage());
    }
}
