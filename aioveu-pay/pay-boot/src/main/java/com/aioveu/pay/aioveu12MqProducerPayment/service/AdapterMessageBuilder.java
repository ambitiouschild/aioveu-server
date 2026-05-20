package com.aioveu.pay.aioveu12MqProducerPayment.service;


import com.aioveu.pay.aioveu12MqProducerPayment.enums.MessageQueueTypeEnum;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.Kafka.KafkaSendResult;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendRequest;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendResult;
import com.aioveu.common.rabbitmq.producer.model.vo.RocketMQSendResult;

/**
 * @ClassName: AdapterMessageBuilder
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/16 21:58
 * @Version 1.0
 **/

public interface AdapterMessageBuilder {



    //--------------------------------------------------------------------------

    //AdapterMessageBuilder接口

    // 3. 根据配置选择MQ
    MessageQueueTypeEnum determineQueueType(RabbitSendRequest request);

    RocketMQSendResult sendByRocketMQ(RabbitSendRequest request) throws Exception;


    /**
     * 发送消息到 Kafka
     * @param request 统一消息请求体（复用你的 RabbitSendRequest）
     * @return 发送结果
     */
    KafkaSendResult sendByKafka(RabbitSendRequest request);

    RabbitSendResult sendByRabbitMQ(RabbitSendRequest request) throws Exception;

    /*
     *  发送消息  - 返回各自MQ的结果
     * */
    Object doSend(MessageQueueTypeEnum queueType, RabbitSendRequest request) throws Exception;


    // 私有辅助方法
    void validateRequest(RabbitSendRequest request);

    /**
     * 记录成功日志
     */

    void logSendSuccess(RabbitSendRequest request, Object result, long startTime);

    /**
     * 记录失败日志
     */
    void logSendFailure(RabbitSendRequest request, String messageId, Exception e, long startTime);
}
