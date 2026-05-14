package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SendReport
 * @Description TODO 发送报告类
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
                "发送报告: 总数=%d, 成功=%d(%.2f%%), 平均耗时=%dms, 最慢=%dms\n" +
                        "失败详情: 超时=%d, 路由失败=%d, NACK=%d, 其他=%d\n" +
                        "最慢的%d个消息: %s\n" +
                        "失败的%d个消息: %s",
                totalResults,
                stats.getSuccessCount(),
                stats.getSuccessRate(),
                stats.getAvgCostTime(),
                stats.getMaxCostTime(),
                stats.getTimeoutCount(),
                stats.getRoutingFailedCount(),
                stats.getNackCount(),
                stats.getOtherFailureCount(),
                slowestMessages.size(),
                formatMessageIds(slowestMessages),
                failedMessages.size(),
                formatMessageIds(failedMessages)
        );
    }

    private String formatMessageIds(List<RabbitSendResult> results) {
        if (results.isEmpty()) {
            return "无";
        }
        return results.stream()
                .limit(5)  // 只显示前5个
                .map(r -> String.format("%s(%dms)",
                        r.getMessageId(), r.getCostTime()))
                .collect(Collectors.joining(", "))
                + (results.size() > 5 ? String.format(" 等%d个...", results.size()) : "");
    }


}
