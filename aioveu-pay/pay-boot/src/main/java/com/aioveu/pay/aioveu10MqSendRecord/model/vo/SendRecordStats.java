package com.aioveu.pay.aioveu10MqSendRecord.model.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @ClassName: SendRecordStats
 * @Description TODO SendRecordStats
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 19:24
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendRecordStats {

    /**
     * 统计开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 统计结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 总发送数
     */
    private Long totalCount;

    /**
     * 发送成功数
     */
    private Long successCount;

    /**
     * 发送失败数
     */
    private Long failedCount;

    /**
     * 发送成功率
     */
    private Double successRate;

    /**
     * 发送失败率
     */
    private Double failureRate;

    /**
     * 未确认数
     */
    private Long unconfirmedCount;

    /**
     * 已确认数
     */
    private Long confirmedCount;

    /**
     * 确认率
     */
    private Double confirmedRate;

    /**
     * 重试总次数
     */
    private Long retryTotalCount;

    /**
     * 平均重试次数
     */
    private Double avgRetryCount;

    /**
     * 最大重试次数
     */
    private Integer maxRetryCount;

    /**
     * 正在重试中的消息数
     */
    private Long retryingCount;

    /**
     * 进入死信队列的消息数
     */
    private Long deadLetterCount;

    /**
     * 按Topic统计
     */
    private Object topicStats;

    /**
     * 按状态统计
     */
    private Object statusStats;

    /**
     * 按时间区间统计
     */
    private Object timeRangeStats;

    /**
     * 统计时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime statsTime;

    /**
     * 计算失败率
     */
    public void calculateRates() {
        if (totalCount != null && totalCount > 0) {
            if (successCount != null) {
                this.successRate = (successCount * 100.0) / totalCount;
            }
            if (failedCount != null) {
                this.failureRate = (failedCount * 100.0) / totalCount;
            }
            if (confirmedCount != null && successCount != null && successCount > 0) {
                this.confirmedRate = (confirmedCount * 100.0) / successCount;
            }
            if (retryTotalCount != null && failedCount != null && failedCount > 0) {
                this.avgRetryCount = (retryTotalCount * 1.0) / failedCount;
            }
        } else {
            this.successRate = 0.0;
            this.failureRate = 0.0;
            this.confirmedRate = 0.0;
            this.avgRetryCount = 0.0;
        }
    }

    /**
     * 添加统计信息
     */
    public SendRecordStats addStats(SendRecordStats other) {
        if (other == null) {
            return this;
        }

        if (other.totalCount != null) {
            this.totalCount = (this.totalCount == null ? 0 : this.totalCount) + other.totalCount;
        }

        if (other.successCount != null) {
            this.successCount = (this.successCount == null ? 0 : this.successCount) + other.successCount;
        }

        if (other.failedCount != null) {
            this.failedCount = (this.failedCount == null ? 0 : this.failedCount) + other.failedCount;
        }

        if (other.unconfirmedCount != null) {
            this.unconfirmedCount = (this.unconfirmedCount == null ? 0 : this.unconfirmedCount) + other.unconfirmedCount;
        }

        if (other.confirmedCount != null) {
            this.confirmedCount = (this.confirmedCount == null ? 0 : this.confirmedCount) + other.confirmedCount;
        }

        if (other.retryTotalCount != null) {
            this.retryTotalCount = (this.retryTotalCount == null ? 0 : this.retryTotalCount) + other.retryTotalCount;
        }

        if (other.retryingCount != null) {
            this.retryingCount = (this.retryingCount == null ? 0 : this.retryingCount) + other.retryingCount;
        }

        if (other.deadLetterCount != null) {
            this.deadLetterCount = (this.deadLetterCount == null ? 0 : this.deadLetterCount) + other.deadLetterCount;
        }

        // 更新最大重试次数
        if (other.maxRetryCount != null && (this.maxRetryCount == null || other.maxRetryCount > this.maxRetryCount)) {
            this.maxRetryCount = other.maxRetryCount;
        }

        // 重新计算比率
        calculateRates();

        return this;
    }

    /**
     * 转换为Map格式
     */
    public java.util.Map<String, Object> toMap() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();

        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("totalCount", totalCount != null ? totalCount : 0);
        map.put("successCount", successCount != null ? successCount : 0);
        map.put("failedCount", failedCount != null ? failedCount : 0);
        map.put("unconfirmedCount", unconfirmedCount != null ? unconfirmedCount : 0);
        map.put("confirmedCount", confirmedCount != null ? confirmedCount : 0);
        map.put("retryTotalCount", retryTotalCount != null ? retryTotalCount : 0);
        map.put("retryingCount", retryingCount != null ? retryingCount : 0);
        map.put("deadLetterCount", deadLetterCount != null ? deadLetterCount : 0);
        map.put("maxRetryCount", maxRetryCount != null ? maxRetryCount : 0);
        map.put("successRate", successRate != null ? String.format("%.2f%%", successRate) : "0.00%");
        map.put("failureRate", failureRate != null ? String.format("%.2f%%", failureRate) : "0.00%");
        map.put("confirmedRate", confirmedRate != null ? String.format("%.2f%%", confirmedRate) : "0.00%");
        map.put("avgRetryCount", avgRetryCount != null ? String.format("%.2f", avgRetryCount) : "0.00");
        map.put("statsTime", statsTime != null ? statsTime.toString() : LocalDateTime.now().toString());

        return map;
    }

    /**
     * 创建空统计对象
     */
    public static SendRecordStats empty() {
        return SendRecordStats.builder()
                .totalCount(0L)
                .successCount(0L)
                .failedCount(0L)
                .unconfirmedCount(0L)
                .confirmedCount(0L)
                .retryTotalCount(0L)
                .retryingCount(0L)
                .deadLetterCount(0L)
                .maxRetryCount(0)
                .build();
    }

    /**
     * 创建汇总统计对象
     */
    public static SendRecordStats summary(SendRecordStats... statsArray) {
        SendRecordStats summary = empty();
        for (SendRecordStats stats : statsArray) {
            summary.addStats(stats);
        }
        return summary;
    }


    /**
     * Topic统计
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class TopicStats {
        private String topic;
        private Long totalCount;
        private Long successCount;
        private Long failedCount;
        private Double successRate;
    }

    /**
     * 状态统计
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusStats {
        private Integer status;
        private String statusDesc;
        private Long count;
        private Double rate;
    }

    /**
     * 时间区间统计
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeRangeStats {
        private String timeRange;  // 如: "00:00-01:00"
        private Long count;
        private Double successRate;
    }

}
