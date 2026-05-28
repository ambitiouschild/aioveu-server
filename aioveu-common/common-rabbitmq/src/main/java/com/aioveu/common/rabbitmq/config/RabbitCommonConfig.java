package com.aioveu.common.rabbitmq.config;

import com.aioveu.common.rabbitmq.constant.PaymentCommonMqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
public class RabbitCommonConfig {

    /**
     * 消息序列化配置
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(  new Jackson2JsonMessageConverter());
        return factory;
    }

    /**
     * 创建RabbitAdmin Bean
     * RabbitAdmin用于管理RabbitMQ队列、交换机和绑定
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        //确保 RabbitAdmin 一定生效（保险写法）

        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        // 自动声明队列和交换机
        rabbitAdmin.setAutoStartup(true);

        return rabbitAdmin;
    }


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
        args.put("x-dead-letter-exchange", PaymentCommonMqConstant.Exchange.DLX);
        args.put("x-dead-letter-routing-key", PaymentCommonMqConstant.RoutingKey.DLQ);

            return new Queue(PaymentCommonMqConstant.Queue.SUCCESS, true, false, false, args);
    }

    /**
     * 支付失败队列
     */
    @Bean
    public Queue paymentFailedQueue() {

        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", PaymentCommonMqConstant.Exchange.DLX);
        args.put("x-dead-letter-routing-key", PaymentCommonMqConstant.RoutingKey.DLQ);

        return new Queue(PaymentCommonMqConstant.Queue.FAILED, true, false, false, args);
    }

    /**
     * ✅ Topic 交换机（关键）
     */
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PaymentCommonMqConstant.Exchange.PAYMENT, true, false);
    }


    /**
     * 绑定
     */
    @Bean
    public Binding paymentSuccessBinding() {
        return BindingBuilder
                .bind(paymentSuccessQueue())
                .to(paymentExchange())
                .with(PaymentCommonMqConstant.RoutingKey.SUCCESS);
    }

    @Bean
    public Binding paymentFailedBinding() {
        return BindingBuilder
                .bind(paymentFailedQueue())
                .to(paymentExchange())
                .with(PaymentCommonMqConstant.RoutingKey.FAILED);
    }


    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(PaymentCommonMqConstant.Exchange.DLX);
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue dlqQueue() {
        return new Queue(PaymentCommonMqConstant.Queue.DLQ, true);
    }

    /**
     * 绑定死信队列
     */
    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(dlqQueue())
                .to(dlxExchange())
                .with(PaymentCommonMqConstant.RoutingKey.DLQ);
    }

}
