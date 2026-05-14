package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: SendReport
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 17:10
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendReport {


    private Date startTime;
    private Date endTime;
    private int totalResults;
    private SendStats stats;
    private List<RabbitSendResult> slowestMessages = new ArrayList<>();
    private List<RabbitSendResult> failedMessages = new ArrayList<>();

    public String toSummary() {
        return String.format(
                "发送报告: 总数=%d, 成功=%d(%.2f%%), 平均耗时=%dms, 最慢=%dms, 失败分布[超时=%d,路由失败=%d,NACK=%d,其他=%d]",
                totalResults, stats.successCount, stats.successRate, stats.avgCostTime,
                stats.maxCostTime, stats.timeoutCount, stats.routingFailedCount,
                stats.nackCount, stats.otherFailureCount
        );
    }


}
