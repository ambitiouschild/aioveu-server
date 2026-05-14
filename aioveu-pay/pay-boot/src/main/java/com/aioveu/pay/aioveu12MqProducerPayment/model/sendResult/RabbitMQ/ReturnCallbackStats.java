package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName: ReturnCallbackStats
 * @Description TODO 回滚统计信息类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 18:21
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnCallbackStats {

    private int totalReturned;
    private int currentQueueSize;
    private Date lastReturnedTime;

    public String getSummary() {
        return String.format(
                "ReturnCallback统计: 总返回数=%d, 当前队列大小=%d, 最后返回时间=%s",
                totalReturned, currentQueueSize, lastReturnedTime
        );
    }

}
