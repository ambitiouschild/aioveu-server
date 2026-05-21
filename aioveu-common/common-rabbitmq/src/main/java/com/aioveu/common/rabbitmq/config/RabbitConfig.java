package com.aioveu.common.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO 消息序列化配置
 * @Author: 雒世松
 * @Date: 2025/6/5 15:55
 * @param
 * @return:
 **/

@Configuration
@Slf4j
public class RabbitConfig {


    public static final String QUEUE_PAYMENT_SUCCESS = "order.payment.success.queue";
    public static final String EXCHANGE_PAYMENT = "order.payment.exchange";
    public static final String ROUTING_KEY_PAYMENT_SUCCESS = "order.payment.success";


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
        return new Queue(QUEUE_PAYMENT_SUCCESS, true);
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(EXCHANGE_PAYMENT);
    }

    @Bean
    public Binding paymentSuccessBinding() {
        return BindingBuilder
                .bind(paymentSuccessQueue())
                .to(paymentExchange())
                .with(ROUTING_KEY_PAYMENT_SUCCESS);
    }
}
