package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: SendStats
 * @Description TODO 发送状态
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 17:12
 * @Version 1.0
 **/
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
}
