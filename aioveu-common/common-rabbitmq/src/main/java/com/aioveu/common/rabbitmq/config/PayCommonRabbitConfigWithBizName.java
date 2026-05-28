package com.aioveu.common.rabbitmq.config;

import com.aioveu.common.rabbitmq.constant.PayCommonMqConstantWithBizName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO 消息序列化配置
 * @Author: 雒世松
 * @Date: 2025/6/5 15:55
 * @param
 * @return:
 **/

@Configuration
@Slf4j
@RequiredArgsConstructor
public class PayCommonRabbitConfigWithBizName {


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
        args.put("x-dead-letter-exchange", PayCommonMqConstantWithBizName.Exchange.DLX);
        args.put("x-dead-letter-routing-key", PayCommonMqConstantWithBizName.RoutingKey.DLQ);

        return new Queue(PayCommonMqConstantWithBizName.Queue.SUCCESS, true, false, false, args);
    }

    /**
     * 支付失败队列
     */
    @Bean
    public Queue paymentFailedQueue() {

        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", PayCommonMqConstantWithBizName.Exchange.DLX);
        args.put("x-dead-letter-routing-key", PayCommonMqConstantWithBizName.RoutingKey.DLQ);

        return new Queue(PayCommonMqConstantWithBizName.Queue.FAILED, true, false, false, args);
    }

    /**
     * ✅ Topic 交换机（关键）
     */
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PayCommonMqConstantWithBizName.Exchange.PAYMENT, true, false);
    }


    /**
     * 绑定
     */
    @Bean
    public Binding paymentSuccessBinding() {
        return BindingBuilder
                .bind(paymentSuccessQueue())
                .to(paymentExchange())
                .with(PayCommonMqConstantWithBizName.RoutingKey.SUCCESS);
    }

    @Bean
    public Binding paymentFailedBinding() {
        return BindingBuilder
                .bind(paymentFailedQueue())
                .to(paymentExchange())
                .with(PayCommonMqConstantWithBizName.RoutingKey.FAILED);
    }


    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(PayCommonMqConstantWithBizName.Exchange.DLX);
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue dlqQueue() {
        return new Queue(PayCommonMqConstantWithBizName.Queue.DLQ, true);
    }

    /**
     * 绑定死信队列
     */
    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(dlqQueue())
                .to(dlxExchange())
                .with(PayCommonMqConstantWithBizName.RoutingKey.DLQ);
    }
}
