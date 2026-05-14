package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BatchRetryResult
 * @Description TODO 批量重试结果
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 21:17
 * @Version 1.0
 **/
@Data
public class BatchRetryResult {

    /** 批次ID */
    private String batchId;

    /** 总消息数 */
    private int totalCount;

    /** 成功数 */
    private int successCount;

    /** 失败数 */
    private int failureCount;

    /** 重试时间 */
    private LocalDateTime retryTime;

    /** 总耗时（毫秒） */
    private Long totalCostTime;

    /** 平均耗时（毫秒） */
    private Double averageCostTime;

    /** 单个重试结果 */
    private Map<String, RetryResult> results = new HashMap<>();

    /** 错误信息（非重试错误） */
    private Map<String, String> errors = new HashMap<>();

    // ========== 工厂方法 ==========

    public static BatchRetryResult create(String batchId) {
        BatchRetryResult result = new BatchRetryResult();
        result.setBatchId(batchId);
        result.setRetryTime(LocalDateTime.now());
        return result;
    }

    public static BatchRetryResult create() {
        return create("batch-" + System.currentTimeMillis());
    }

    // ========== 业务方法 ==========

    /**
     * 添加重试结果
     */
    public void addResult(String messageId, RetryResult retryResult) {
        results.put(messageId, retryResult);
        totalCount++;

        if (retryResult.isSuccess()) {
            successCount++;
        } else {
            failureCount++;
        }

        // 更新耗时统计
        if (retryResult.getCostTime() != null) {
            if (totalCostTime == null) {
                totalCostTime = 0L;
            }
            totalCostTime += retryResult.getCostTime();
        }
    }

    /**
     * 添加错误
     */
    public void addError(String messageId, String error) {
        errors.put(messageId, error);
        totalCount++;
        failureCount++;
    }

    /**
     * 计算平均耗时
     */
    public void calculateAverage() {
        if (totalCostTime != null && successCount > 0) {
            averageCostTime = (double) totalCostTime / successCount;
        }
    }

    /**
     * 是否全部成功
     */
    public boolean isAllSuccess() {
        return failureCount == 0 && errors.isEmpty();
    }

    /**
     * 是否全部失败
     */
    public boolean isAllFailed() {
        return successCount == 0;
    }

    /**
     * 获取成功率
     */
    public double getSuccessRate() {
        if (totalCount == 0) {
            return 0.0;
        }
        return (double) successCount / totalCount * 100;
    }

    /**
     * 获取成功的消息ID列表
     */
    public List<String> getSuccessMessageIds() {
        List<String> successIds = new ArrayList<>();
        for (Map.Entry<String, RetryResult> entry : results.entrySet()) {
            if (entry.getValue().isSuccess()) {
                successIds.add(entry.getKey());
            }
        }
        return successIds;
    }

    /**
     * 获取失败的消息ID列表
     */
    public List<String> getFailedMessageIds() {
        List<String> failedIds = new ArrayList<>(errors.keySet());
        for (Map.Entry<String, RetryResult> entry : results.entrySet()) {
            if (!entry.getValue().isSuccess()) {
                failedIds.add(entry.getKey());
            }
        }
        return failedIds;
    }

    /**
     * 获取结果摘要
     */
    public String getSummary() {
        calculateAverage();
        return String.format(
                "批量重试结果: 批次=%s, 总数=%d, 成功=%d, 失败=%d, 成功率=%.2f%%, 总耗时=%dms, 平均耗时=%.2fms",
                batchId, totalCount, successCount, failureCount, getSuccessRate(),
                totalCostTime != null ? totalCostTime : 0,
                averageCostTime != null ? averageCostTime : 0.0
        );
    }

    /**
     * 获取详细报告
     */
    public String getDetailReport() {
        StringBuilder report = new StringBuilder();
        report.append("批量重试详细报告\n");
        report.append("================\n");
        report.append("批次ID: ").append(batchId).append("\n");
        report.append("重试时间: ").append(retryTime).append("\n");
        report.append("总计: ").append(totalCount).append(" 条消息\n");
        report.append("成功: ").append(successCount).append(" 条\n");
        report.append("失败: ").append(failureCount).append(" 条\n");
        report.append("成功率: ").append(String.format("%.2f", getSuccessRate())).append("%\n");
        report.append("总耗时: ").append(totalCostTime != null ? totalCostTime + "ms" : "N/A").append("\n");
        report.append("平均耗时: ").append(averageCostTime != null ?
                String.format("%.2fms", averageCostTime) : "N/A").append("\n\n");

        if (!errors.isEmpty()) {
            report.append("处理错误:\n");
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                report.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            report.append("\n");
        }

        if (!results.isEmpty()) {
            report.append("重试结果:\n");
            for (Map.Entry<String, RetryResult> entry : results.entrySet()) {
                report.append("  ").append(entry.getKey()).append(": ")
                        .append(entry.getValue().getSummary()).append("\n");
            }
        }

        return report.toString();
    }
}
