package com.aioveu.kafka.producer.service;


/**
 * @ClassName: MultiTypeProducerService
 * @Description TODO 使用多种 KafkaTemplate
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 18:36
 * @Version 1.0
 **/

public interface MultiTypeProducerService {

    /**
     * 发送字符串消息
     */
    void sendStringMessage(String topic, String message);

    /**
     * 发送JSON对象
     */
    void sendJsonMessage(String topic, Object data);

    /**
     * 发送带Key的消息
     */
    void sendWithKey(String topic, String key, Object value);
}
