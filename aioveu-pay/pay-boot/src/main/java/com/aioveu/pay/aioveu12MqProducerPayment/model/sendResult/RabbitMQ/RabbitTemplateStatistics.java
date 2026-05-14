package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName: RabbitTemplateStatistics
 * @Description TODO RabbitTemplate统计类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 18:44
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitTemplateStatistics {

    private int sendCount;
    private int returnCount;
    private int confirmCount;
    private double successRate;
    private Date lastUpdated;

    public String getStatisticsSummary() {
        return String.format(
                "RabbitTemplate统计: 发送=%d, 返回=%d, 确认=%d, 成功率=%.2f%%",
                sendCount, returnCount, confirmCount, successRate
        );
    }
}
