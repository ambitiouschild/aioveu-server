package com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ;


import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendResult;

import java.util.List;

/**
 * @ClassName: RabbitSendResultshandle
 * @Description TODO  使用
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 17:30
 * @Version 1.0
 **/

public interface RabbitSendResultshandle {

    /**
     * 正确访问统计信息
     */
    void processResults(List<RabbitSendResult> results);


    /**
     * 批量处理结果
     */
    void handleBatchSendResults(List<RabbitSendResult> results);

    /**
     * 实时监控发送结果
     */
    void realTimeMonitoring();


    /**
     * 性能分析
     */
    void analyzePerformance(List<RabbitSendResult> results);

}
