package com.aioveu.pay.aioveu12MqProducerPayment.service.Kafka.impl;


import com.aioveu.pay.aioveu12MqProducerPayment.service.Kafka.MultiTypeProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MultiTypeProducerServiceImpl
 * @Description TODO  使用多种 KafkaTemplate
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 18:33
 * @Version 1.0
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class MultiTypeProducerServiceImpl implements MultiTypeProducerService {


    // 注入字符串类型的Template
    private final KafkaTemplate<String, String> stringKafkaTemplate;

    // 注入JSON类型的Template
    private final KafkaTemplate<String, Object> jsonKafkaTemplate;


    /**
     * 发送字符串消息
     */
    @Override
    public void sendStringMessage(String topic, String message) {
        stringKafkaTemplate.send(topic, message);
    }

    /**
     * 发送JSON对象
     */
    @Override
    public void sendJsonMessage(String topic, Object data) {
        jsonKafkaTemplate.send(topic, data);
    }

    /**
     * 发送带Key的消息
     */
    @Override
    public void sendWithKey(String topic, String key, Object value) {
        jsonKafkaTemplate.send(topic, key, value);
    }
}


