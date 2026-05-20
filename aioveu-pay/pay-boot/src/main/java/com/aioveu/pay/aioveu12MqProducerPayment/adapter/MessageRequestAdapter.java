package com.aioveu.pay.aioveu12MqProducerPayment.adapter;


import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendRequest;
import org.apache.commons.lang3.StringUtils; // 引入 StringUtils
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.rocketmq.common.message.Message; // 引入 RocketMQ 的 Message
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
/**
 * @ClassName: MessageRequestAdapter
 * @Description TODO  适配器模式（兼容官方SDK）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 17:57
 * @Version 1.0
 **/

/**
 * 适配器：将自定义Request转换为各种MQ的官方Request
 */
@Component
public class MessageRequestAdapter {

    /**
     * 转换为RocketMQ的Message
     */
    public org.apache.rocketmq.common.message.Message toRocketMQMessage(RabbitSendRequest request) {
        // 使用全限定名或确保导入的是 org.apache.rocketmq.common.message.Message
        Message msg = new Message(
                request.getTopic(),
                request.getTag(),
                request.getBodyAsString().getBytes(StandardCharsets.UTF_8)
        );

        // 设置消息键
        if (StringUtils.isNotBlank(request.getMessageKey())) {
            msg.setKeys(request.getMessageKey());
        }

        // 设置延迟
        // 注意：这里假设 RabbitSendRequest 有 getDelayTime() 方法
        if (request.getDelayTime() > 0) {
            int delayLevel = calculateDelayLevel(request.getDelayTime());
            msg.setDelayTimeLevel(delayLevel);
        }

        // 设置属性
        // 注意：这里假设 RabbitSendRequest 有 getProperties() 方法，且返回 Map<String, String>
        if (request.getProperties() != null) {
            request.getProperties().forEach(msg::putUserProperty);
        }

        return msg;
    }

    /**
     * 转换为Kafka的ProducerRecord
     */
    public org.apache.kafka.clients.producer.ProducerRecord<String, String> toKafkaRecord(RabbitSendRequest request) {
        return new ProducerRecord<>(
                request.getTopic(),
                request.getMessageKey(),  // 作为Kafka的key
                request.getBodyAsString()
        );
    }

    /**
     * 转换为RabbitMQ的Message
     */
    public org.springframework.amqp.core.Message toRabbitMQMessage(RabbitSendRequest request) {
        // 使用重命名后的 SpringAmqpMessageProperties
        org.springframework.amqp.core.MessageProperties properties = new org.springframework.amqp.core.MessageProperties();

        // 设置消息头
        // 注意：这里假设 RabbitSendRequest 有 getHeaders() 方法，且返回 Map<String, Object>
        if (request.getHeaders() != null) {
            request.getHeaders().forEach(properties::setHeader);
        }

        // 设置优先级
        // 修复 Operator '!=' cannot be applied to 'int, null'
        int priority = request.getPriority();
        if (priority != 0) {  // 假设0是默认值
            properties.setPriority(priority);
        }

        // 设置TTL
        if (request.getTtl() > 0) {
            properties.setExpiration(String.valueOf(request.getTtl()));
        }

        return new org.springframework.amqp.core.Message(
                request.getBodyAsString().getBytes(StandardCharsets.UTF_8),
                properties
        );
    }

    private int calculateDelayLevel(long delayMillis) {
        // RocketMQ延迟级别转换
        if (delayMillis <= 0) return 0;
        if (delayMillis <= 1000) return 1;  // 1s
        if (delayMillis <= 5000) return 2;  // 5s
        if (delayMillis <= 10000) return 3; // 10s
        // ... 其他级别
        return 4; // 默认30s
    }
}
