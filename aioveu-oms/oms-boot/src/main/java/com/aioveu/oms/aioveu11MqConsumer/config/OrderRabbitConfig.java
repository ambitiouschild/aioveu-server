package com.aioveu.oms.aioveu11MqConsumer.config;

import com.aioveu.common.rabbitmq.constant.PaymentMqConstant;
import com.aioveu.oms.aioveu11MqConsumer.constant.OrderMqConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO 消息序列化配置
 * @Author: 雒世松
 * @Date: 2025/6/5 15:55
 * @param
 * @return:
 **/

//@Configuration
@Slf4j
@RequiredArgsConstructor
public class OrderRabbitConfig {


    private final OrderMqConstant mqConstant;


    /*
    *
    *     TODO      方案 1（最推荐）：让 Spring 自动创建队列
    *                       ✅ Spring 启动时会自动创建队列
                            ✅ 再也不会 404
    * */
    @Bean
    public Queue paymentSuccessQueue() {

        //主队列绑定 DLQ（关键）
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", mqConstant.dlxExchange());
        args.put("x-dead-letter-routing-key", mqConstant.rkDlq());

            return new Queue(mqConstant.queueSuccess(), true, false, false, args);
    }

    /**
     * 支付失败队列
     */
    @Bean
    public Queue paymentFailedQueue() {

        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange",mqConstant.dlxExchange());
        args.put("x-dead-letter-routing-key", mqConstant.rkDlq());

        return new Queue(mqConstant.queueFailed(), true, false, false, args);
    }

    @Bean
    public Queue dlqQueue() {
        return new Queue(mqConstant.queueDlq(), true);
    }


    /* ================= 交换机 ================= */

    /**
     * ✅ Topic 交换机（关键）
     */
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(mqConstant.exchangePayment(), true, false);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(mqConstant.dlxExchange());
    }

    /* ================= 绑定 ================= */

    @Bean
    public Binding paymentSuccessBinding() {
        return BindingBuilder
                .bind(paymentSuccessQueue())
                .to(paymentExchange())
                .with(mqConstant.rkSuccess());
    }

    @Bean
    public Binding paymentFailedBinding() {
        return BindingBuilder
                .bind(paymentFailedQueue())
                .to(paymentExchange())
                .with(mqConstant.rkFailed());
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(dlqQueue())
                .to(dlxExchange())
                .with(mqConstant.rkDlq());
    }

}
