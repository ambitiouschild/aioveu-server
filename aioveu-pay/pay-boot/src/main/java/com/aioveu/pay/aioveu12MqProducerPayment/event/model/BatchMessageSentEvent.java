package com.aioveu.pay.aioveu12MqProducerPayment.event.model;


import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitBatchSendResult;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * @ClassName: BatchMessageSentEvent
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/17 18:29
 * @Version 1.0
 **/
@Data
public class BatchMessageSentEvent extends ApplicationEvent {

    //ApplicationEvent的子类构造器必须接收一个 source参数作为第一个参数。这是 Spring 事件框架的要求。

    private String batchId;
    private int totalCount;
    private int successCount;
    private int failedCount;
    private long costTime;
    private LocalDateTime sendTime;
    private boolean success;
    private Long tenantId;

    public BatchMessageSentEvent(Object source, String batchId, int totalCount,
                                 int successCount, int failedCount, long costTime,
                                 LocalDateTime sendTime, boolean success, Long tenantId) {
        super(source);
        this.batchId = batchId;
        this.totalCount = totalCount;
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.costTime = costTime;
        this.sendTime = sendTime;
        this.success = success;
        this.tenantId = tenantId;
    }

    public static BatchMessageSentEvent fromResult(Object source, RabbitBatchSendResult result) {
        return new BatchMessageSentEvent(
                source,
                result.getBatchId(),
                result.getTotalCount(),
                result.getSuccessCount(),
                result.getFailedCount(),
                result.getCostTime(),
                LocalDateTime.now(),
                result.isAllSuccess(),
                result.getTenantId()
        );
    }
}
