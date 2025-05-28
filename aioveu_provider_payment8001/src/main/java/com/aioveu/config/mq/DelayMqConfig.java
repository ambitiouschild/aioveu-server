package com.aioveu.config.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @description MQ的延迟队列
 * @author: 雒世松
 * @date: 2025/5/31 0031 21:13
 */
@Configuration
public class DelayMqConfig {

    /**
     * 延迟队列
     */
    public final static String DELAY_QUEUE = "sport_delay_queue";

    /**
     * 延迟队列 测试环境
     */
    public final static String DELAY_TEST_QUEUE = "sport_delay_test_queue";

    /**
     * 延迟交换机 测试环境
     */
    public final static String DELAY_TEST_EXCHANGE = "sport_delay_test_exchange";

    /**
     * 延迟交换机
     */
    public final static String DELAY_EXCHANGE = "sport_delay_exchange";

    /**
     * 延迟路由key
     */
    public final static String DELAY_ROUTE_KEY = "sport_delay_route_key";

    /**
     * 延迟路由key
     */
    public final static String DELAY_TEST_ROUTE_KEY = "sport_test_delay_route_key";

    /**
     * 创建延迟队列
     * @return
     */
    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(DELAY_QUEUE).build();
    }

    /**
     * 创建延迟队列
     * @return
     */
    @Bean
    public Queue delayTestQueue() {
        return QueueBuilder.durable(DELAY_TEST_QUEUE).build();
    }

    /**
     * 创建延迟交换机
     * @return
     */
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_EXCHANGE, "x-delayed-message", true, false, map);
    }

    /**
     * 创建延迟交换机
     * @return
     */
    @Bean
    public CustomExchange delayTestExchange() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_TEST_EXCHANGE, "x-delayed-message", true, false, map);
    }

    /**
     * 绑定延迟队列和交换机
     * @return
     */
    @Bean
    public Binding delayExchangeBinging() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(DELAY_ROUTE_KEY).noargs();
    }

    /**
     * 绑定延迟队列和交换机
     * @return
     */
    @Bean
    public Binding delayTestExchangeBinging() {
        return BindingBuilder.bind(delayTestQueue()).to(delayTestExchange()).with(DELAY_TEST_ROUTE_KEY).noargs();
    }


}
