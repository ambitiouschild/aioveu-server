package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;


/**
 * @ClassName: FailedMessageStats
 * @Description TODO 失败消息统计结果
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/15 19:13
 * @Version 1.0
 **/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

/**
 * 失败消息统计结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailedMessageStats {

    private Long count;  // 总数量
    /**
     * 统计维度
     */
    private StatsDimension dimension;

    /**
     * 维度值（如具体的消息类型、交换机名称等）
     */
    private String dimensionValue;

    /**
     * 统计时间范围
     */
    private StatsPeriod period;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // ========== 基础统计指标 ==========

    /**
     * 总消息数量
     */
    @Builder.Default
    private Long totalCount = 0L;

    /**
     * 失败消息数量
     */
    @Builder.Default
    private Long failedCount = 0L;

    /**
     * 成功消息数量
     */
    @Builder.Default
    private Long successCount = 0L;

    /**
     * 正在处理中的消息数量
     */
    @Builder.Default
    private Long processingCount = 0L;


    /**
     * 今日失败数量
     */
    private Long todayFailed = 0L;

    /**
     * 状态统计 Map<状态, 数量>
     */
    private Map<String, Long> statusStatistics = new HashMap<>();


    /**
     * 租户统计 Map<租户ID, 统计对象>
     */
    private Map<String, FailedMessageStats> tenantStatistics = new HashMap<>();

    // 按时间统计
    private Map<String, Long> timeStatistics = new HashMap<>();

    /**
     * 可重试消息数量
     */
    private Long retryableCount = 0L;

    // ========== 失败率统计 ==========

    /**
     * 失败率
     */
    @Builder.Default
    private BigDecimal failureRate = BigDecimal.ZERO;

    /**
     * 成功率
     */
    @Builder.Default
    private BigDecimal successRate = BigDecimal.ZERO;

    /**
     * 各类失败原因统计
     */
    private Map<String, Long> failureReasonStats;

    // ========== 耗时统计 ==========

    /**
     * 平均耗时（毫秒）
     */
    @Builder.Default
    private Long avgCostTime = 0L;

    /**
     * 最大耗时（毫秒）
     */
    @Builder.Default
    private Long maxCostTime = 0L;

    /**
     * 最小耗时（毫秒）
     */
    @Builder.Default
    private Long minCostTime = 0L;

    /**
     * P95 耗时（毫秒）
     */
    @Builder.Default
    private Long p95CostTime = 0L;

    /**
     * P99 耗时（毫秒）
     */
    @Builder.Default
    private Long p99CostTime = 0L;

    // ========== 重试统计 ==========

    /**
     * 平均重试次数
     */
    @Builder.Default
    private Double avgRetryCount = 0.0;

    /**
     * 最大重试次数
     */
    @Builder.Default
    private Integer maxRetryCount = 0;

    /**
     * 重试成功率
     */
    @Builder.Default
    private BigDecimal retrySuccessRate = BigDecimal.ZERO;

    /**
     * 达到最大重试次数的消息数量
     */
    @Builder.Default
    private Long maxRetryReachedCount = 0L;

    // ========== 趋势统计 ==========

    /**
     * 同比变化率（与上个周期比较）
     */
    @Builder.Default
    private BigDecimal yearOverYearRate = BigDecimal.ZERO;

    /**
     * 环比变化率（与上个时间间隔比较）
     */
    @Builder.Default
    private BigDecimal monthOverMonthRate = BigDecimal.ZERO;

    /**
     * 失败趋势：上升/下降/平稳
     */
    private Trend trend;

    // ========== 业务方法 ==========


    // 可以添加一个工厂方法
    public static FailedMessageStats createWithCount(Long count) {
        FailedMessageStats stats = new FailedMessageStats();
        stats.setCount(count);
        stats.setStatusStatistics(new HashMap<>());
        stats.setTenantStatistics(new HashMap<>());
        return stats;
    }

    /**
     * 计算失败率
     */
    public void calculateFailureRate() {
        if (totalCount > 0) {
            failureRate = BigDecimal.valueOf(failedCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalCount), 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 计算成功率
     */
    public void calculateSuccessRate() {
        if (totalCount > 0) {
            successRate = BigDecimal.valueOf(successCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalCount), 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 计算重试成功率
     */
    public void calculateRetrySuccessRate() {
        if (failedCount > 0) {
            retrySuccessRate = BigDecimal.valueOf(successCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(failedCount), 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 获取失败率等级
     */
    public String getFailureRateLevel() {
        if (failureRate.compareTo(BigDecimal.valueOf(10)) > 0) {
            return "HIGH";      // 高于10%
        } else if (failureRate.compareTo(BigDecimal.valueOf(5)) > 0) {
            return "MEDIUM";    // 5%-10%
        } else if (failureRate.compareTo(BigDecimal.valueOf(1)) > 0) {
            return "LOW";       // 1%-5%
        } else {
            return "NORMAL";    // 低于1%
        }
    }

    /**
     * 获取摘要信息
     */
    public String getSummary() {
        return String.format("统计维度: %s, 总消息: %d, 失败: %d, 失败率: %.2f%%, 平均耗时: %dms",
                dimension != null ? dimension.getName() : "未知",
                totalCount, failedCount, failureRate.doubleValue(), avgCostTime);
    }

    /**
     * 合并另一个统计结果
     */
    public void merge(FailedMessageStats other) {
        if (other == null) return;

        this.totalCount += other.getTotalCount();
        this.failedCount += other.getFailedCount();
        this.successCount += other.getSuccessCount();
        this.processingCount += other.getProcessingCount();
        this.maxRetryReachedCount += other.getMaxRetryReachedCount();

        // 重新计算比率
        calculateFailureRate();
        calculateSuccessRate();
        calculateRetrySuccessRate();
    }

    /**
     * 生成报表数据
     */
    public Map<String, Object> toReportData() {
        return Map.ofEntries(
                entry("dimension", dimension != null ? dimension.getName() : "总计"),
                entry("dimensionValue", dimensionValue != null ? dimensionValue : "ALL"),
                entry("totalCount", totalCount),
                entry("failedCount", failedCount),
                entry("successCount", successCount),
                entry("failureRate", failureRate),
                entry("avgCostTime", avgCostTime),
                entry("avgRetryCount", avgRetryCount),
                entry("failureRateLevel", getFailureRateLevel()),
                entry("period", period != null ? period.getName() : "自定义"),
                entry("startTime", startTime),
                entry("endTime", endTime)
        );
    }

    /**
     * 添加状态统计
     */
    public void addStatusStat(String status, Long count) {
        this.statusStatistics.put(status, count);
    }

    /**
     * 添加租户统计
     */
    public void addTenantStat(String tenantId, FailedMessageStats stat) {
        this.tenantStatistics.put(tenantId, stat);
    }

    /**
     * 获取今日失败率
     */
    public BigDecimal getTodayFailureRate() {
        if (todayFailed > 0) {
            return BigDecimal.valueOf(todayFailed)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(getFailedCount()), 2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }


    // ========== 内部枚举 ==========

    /**
     * 统计维度
     */
    public enum StatsDimension {
        OVERALL("总体"),
        MESSAGE_TYPE("消息类型"),
        TENANT("租户"),
        EXCHANGE("交换机"),
        ROUTING_KEY("路由键"),
        ERROR_CODE("错误码"),
        SEND_STATUS("发送状态"),
        HOUR_OF_DAY("小时"),
        DAY_OF_WEEK("星期");

        private final String name;

        StatsDimension(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 统计周期
     */
    public enum StatsPeriod {
        REAL_TIME("实时"),
        LAST_HOUR("最近1小时"),
        TODAY("今日"),
        YESTERDAY("昨日"),
        THIS_WEEK("本周"),
        LAST_WEEK("上周"),
        THIS_MONTH("本月"),
        LAST_MONTH("上月"),
        THIS_QUARTER("本季度"),
        LAST_QUARTER("上季度"),
        CUSTOM("自定义");

        private final String name;

        StatsPeriod(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 趋势
     */
    public enum Trend {
        UP("上升"),
        DOWN("下降"),
        STABLE("平稳"),
        UNKNOWN("未知");

        private final String description;

        Trend(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

}
