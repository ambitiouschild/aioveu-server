package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: SendStats
 * @Description TODO 发送状态统计信息类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 17:12
 * @Version 1.0
 **/

/**
 * 统计信息类
 * 使用Getter/Setter而不是public字段
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendStats {


    private int totalCount;
    private int successCount;
    private int failureCount;
    private double successRate;
    private long totalCostTime;
    private long avgCostTime;
    private long minCostTime = Long.MAX_VALUE;
    private long maxCostTime;
    private int timeoutCount;
    private int routingFailedCount;
    private int nackCount;
    private int otherFailureCount;


    // 便捷方法
    public void incrementTotalCount() {
        this.totalCount++;
    }

    public void incrementSuccessCount() {
        this.successCount++;
    }

    public void incrementFailureCount() {
        this.failureCount++;
    }

    public void addCostTime(long costTime) {
        this.totalCostTime += costTime;
    }

    public void incrementTimeoutCount() {
        this.timeoutCount++;
    }

    public void incrementRoutingFailedCount() {
        this.routingFailedCount++;
    }

    public void incrementNackCount() {
        this.nackCount++;
    }

    public void incrementOtherFailureCount() {
        this.otherFailureCount++;
    }

    public String toSummaryString() {
        return String.format(
                "发送统计: 总数=%d, 成功=%d(%.2f%%), 平均耗时=%dms, 最慢=%dms, 失败分布[超时=%d,路由失败=%d,NACK=%d,其他=%d]",
                totalCount, successCount, successRate, avgCostTime, maxCostTime,
                timeoutCount, routingFailedCount, nackCount, otherFailureCount
        );
    }
}
