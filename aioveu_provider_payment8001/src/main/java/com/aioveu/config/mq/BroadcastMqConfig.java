package com.aioveu.config.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description MQ的广播队列  fanout模式
 * @author: 雒世松
 * @date: 2025/5/31 0031 21:13
 */
@Configuration
public class BroadcastMqConfig {

    public final static String FANOUT_QUEUE_ONE = "fanout_queue_one";

    public final static String FANOUT_QUEUE_TWO = "fanout_queue_two";

    /**
     * fanout exchange
     */
    public final static String FANOUT_EXCHANGE = "sport_fanout_exchange";

    //  fanout 广播者模式队列
    @Bean
    public Queue fanoutQueueOne() {
        return new Queue(FANOUT_QUEUE_ONE, true);
    }

    @Bean
    public Queue fanoutQueueTwo() {
        return new Queue(FANOUT_QUEUE_TWO, true);
    }

    /**
     * fanout 交换器
     * 扇型交换机，这个交换机没有路由键概念，
     * 就算你绑了路由键也是无视的。 这个交换机在接收到消息后，会直接转发到绑定到它上面的所有队列
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    //广播模式绑定
    @Bean
    public Binding fanoutExchangeBingingOne() {
        return BindingBuilder.bind(fanoutQueueOne()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutExchangeBingingTwo() {
        return BindingBuilder.bind(fanoutQueueTwo()).to(fanoutExchange());
    }


}
