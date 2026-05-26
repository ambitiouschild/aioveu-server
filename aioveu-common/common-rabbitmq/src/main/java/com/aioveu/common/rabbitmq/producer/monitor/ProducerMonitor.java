package com.aioveu.common.rabbitmq.producer.monitor;


import com.aioveu.common.rabbitmq.producer.service.MQAlertService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName: ProducerMonitor
 * @Description TODO  生产端业务监控
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 17:41
 * @Version 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class ProducerMonitor {


    private final MQAlertService alertService;

    // 监控指标
    private final AtomicLong totalSendCount = new AtomicLong(0);
    private final AtomicLong successSendCount = new AtomicLong(0);
    private final AtomicLong failedSendCount = new AtomicLong(0);
    private final AtomicLong backlogCount = new AtomicLong(0);
    private final AtomicInteger compensationFailedCount = new AtomicInteger(0);

    // 阈值配置
    @Value("${producer.monitor.success-rate-threshold:95.0}")
    private double successRateThreshold;  // 成功率阈值百分比

    @Value("${producer.monitor.backlog-threshold:1000}")
    private long backlogThreshold;  // 积压阈值

    @Value("${producer.monitor.compensation-failed-threshold:10}")
    private int compensationFailedThreshold;  // 补偿失败阈值

    @Value("${producer.monitor.enabled:true}")
    private boolean monitorEnabled;


    /**
     * 监控任务 - 30秒执行一次
     */
//    @Scheduled(fixedDelay = 30000)
    public void monitorProducer() {

        if (!monitorEnabled) {
            return;
        }


        try {
            log.debug("【生产端监控】开始执行监控任务...");

            // 1. 计算发送成功率
            double sendSuccessRate = calculateSendSuccessRate();

            // 2. 获取积压消息数
            long currentBacklogCount = getCurrentBacklogCount();

            // 3. 获取补偿任务失败次数
            int currentCompensationFailedCount = compensationFailedCount.get();

            // 检查并告警
            checkAndAlert(sendSuccessRate, currentBacklogCount, currentCompensationFailedCount);

            // 记录监控日志
            logMonitorInfo(sendSuccessRate, currentBacklogCount, currentCompensationFailedCount);

            log.debug("【生产端监控】监控任务执行完成");

        } catch (Exception e) {
            log.error("【生产端监控】监控任务异常", e);
        }

    }

    /**
     * 记录发送结果
     */
    public void recordSendResult(boolean success) {
        totalSendCount.incrementAndGet();
        if (success) {
            successSendCount.incrementAndGet();
        } else {
            failedSendCount.incrementAndGet();
        }
    }

    /**
     * 记录积压消息
     */
    public void recordBacklog(long count) {
        backlogCount.set(count);
    }

    /**
     * 记录补偿任务失败
     */
    public void recordCompensationFailure() {
        compensationFailedCount.incrementAndGet();
    }

    /**
     * 重置补偿失败计数
     */
    public void resetCompensationFailure() {
        compensationFailedCount.set(0);
    }

    /**
     * 计算发送成功率
     */
    private double calculateSendSuccessRate() {
        long total = totalSendCount.get();
        long success = successSendCount.get();

        if (total == 0) {
            return 100.0;  // 没有发送记录，默认100%
        }

        return (success * 100.0) / total;
    }

    /**
     * 获取当前积压消息数
     * 这里可以调用你的数据库查询或RocketMQ查询
     */
    private long getCurrentBacklogCount() {
        // 实现1：从数据库查询
        // return mqSendRecordService.countUnconfirmed();

        // 实现2：从RocketMQ查询
        // return calculateMQBacklog();

        // 实现3：使用内存中的值
        return backlogCount.get();
    }

    /**
     * 检查并告警
     */
    private void checkAndAlert(double sendSuccessRate, long backlogCount, int compensationFailedCount) {
        // 1. 检查发送成功率
        if (sendSuccessRate < successRateThreshold) {
            sendAlert("生产端发送成功率低",
                    String.format("发送成功率: %.2f%% (阈值: %.1f%%)",
                            sendSuccessRate, successRateThreshold));
        }

        // 2. 检查积压消息
        if (backlogCount > backlogThreshold) {
            sendAlert("生产端消息积压",
                    String.format("积压消息数: %d (阈值: %d)",
                            backlogCount, backlogThreshold));
        }

        // 3. 检查补偿任务
        if (compensationFailedCount > compensationFailedThreshold) {
            sendAlert("补偿任务失败过多",
                    String.format("补偿失败次数: %d (阈值: %d)",
                            compensationFailedCount, compensationFailedThreshold));
        }
    }

    /**
     * 发送告警
     */
    private void sendAlert(String title, String message) {
        try {
            alertService.sendAlert("【生产端告警】" + title, message);
            log.warn("【生产端告警】{}: {}", title, message);
        } catch (Exception e) {
            log.error("发送告警失败", e);
        }
    }

    /**
     * 记录监控信息
     */
    private void logMonitorInfo(double sendSuccessRate, long backlogCount, int compensationFailedCount) {
        log.info("""
            【生产端监控报告】
            总发送消息数: {}
            成功发送数: {}
            失败发送数: {}
            发送成功率: {:.2f}%
            消息积压数: {}
            补偿失败次数: {}
            """,
                totalSendCount.get(),
                successSendCount.get(),
                failedSendCount.get(),
                sendSuccessRate,
                backlogCount,
                compensationFailedCount);
    }

    /**
     * 获取监控数据
     */
    public ProducerMonitorData getMonitorData() {
        double successRate = calculateSendSuccessRate();
        long backlog = getCurrentBacklogCount();

        return ProducerMonitorData.builder()
                .totalSendCount(totalSendCount.get())
                .successSendCount(successSendCount.get())
                .failedSendCount(failedSendCount.get())
                .sendSuccessRate(successRate)
                .backlogCount(backlog)
                .compensationFailedCount(compensationFailedCount.get())
                .build();
    }

    /**
     * 监控数据DTO
     */
    @Data
    @Builder
    public static class ProducerMonitorData {
        private long totalSendCount;
        private long successSendCount;
        private long failedSendCount;
        private double sendSuccessRate;
        private long backlogCount;
        private int compensationFailedCount;
    }






}
