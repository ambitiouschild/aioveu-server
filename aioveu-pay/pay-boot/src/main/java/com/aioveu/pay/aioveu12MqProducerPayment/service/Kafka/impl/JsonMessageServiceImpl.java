package com.aioveu.pay.aioveu12MqProducerPayment.service.Kafka.impl;


import com.aioveu.order.model.OmsOrder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: JsonMessageServiceImpl
 * @Description TODO 发送JSON对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 18:48
 * @Version 1.0
 **/
@Service
public class JsonMessageServiceImpl {

    private final KafkaTemplate<String, Object> jsonKafkaTemplate;

    public JsonMessageServiceImpl(KafkaTemplate<String, Object> jsonKafkaTemplate) {
        this.jsonKafkaTemplate = jsonKafkaTemplate;
    }

    /**
     * 发送订单消息
     */
    public void sendOrderMessage(OmsOrder order) {
        Map<String, Object> message = new HashMap<>();
        message.put("orderId", order.getId());
        message.put("userId", order.getMemberId());
        message.put("amount", order.getPaymentAmount());
        message.put("status", order.getStatus());
        message.put("timestamp", System.currentTimeMillis());

        // 将 Long 转换为 String
        String orderIdStr = order.getId().toString();
        jsonKafkaTemplate.send("order-topic", orderIdStr, message);
    }

//    /**
//     * 发送用户消息
//     */
//    public void sendUserMessage(User user) {
//        jsonKafkaTemplate.send("user-topic", user.getId(), user);
//    }
}
