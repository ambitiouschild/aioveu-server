package com.aioveu.config.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @description MQ的主题队列 topic模式
 * @author: 雒世松
 * @date: 2025/5/31 0031 21:13
 */
@Configuration
public class TopicMqConfig {

    public final static String TOPIC_QUEUE_ONE = "sport_topic_queue_one";

    public final static String TOPIC_QUEUE_TWO = "sport_topic_queue_two";

    /**
     * 主题 exchange
     */
    public final static String TOPIC_EXCHANGE = "sport_topic_exchange";

    public final static String TOPIC_ROUTINGKEY_ONE = "topic_common_key";

    /**
     *  *  (星号) 用来表示一个单词 (必须出现的)
     *  #  (井号) 用来表示任意数量（零个或多个）单词
     *  通配的绑定键是跟队列进行绑定的，举个小例子
     * 队列Q1 绑定键为 *.TT.*          队列Q2绑定键为  TT.#
     * 如果一条消息携带的路由键为 A.TT.B，那么队列Q1将会收到；
     * 如果一条消息携带的路由键为TT.AA.BB，那么队列Q2将会收到；
     */
    public final static String TOPIC_ROUTINGKEY_TWO = "*.key";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    // topic 订阅者模式队列
    @Bean
    public Queue topicQueueOne() {
        return new Queue(TOPIC_QUEUE_ONE, true);
    }

    @Bean
    public Queue topicQueueTwo() {
        return new Queue(TOPIC_QUEUE_TWO, true);
    }

    /**
     * topic 交换器
     * 主题交换机，这个交换机其实跟直连交换机流程差不多，但是它的特点就是在它的路由键和绑定键之间是有规则的
     * *  (星号) 用来表示一个单词 (必须出现的)
     * #  (井号) 用来表示任意数量（零个或多个）单词
     * 通配的绑定键是跟队列进行绑定的，举个小例子
     * 队列Q1 绑定键为 *.TT.*          队列Q2绑定键为  TT.#
     * 如果一条消息携带的路由键为 A.TT.B，那么队列Q1将会收到；
     * 如果一条消息携带的路由键为TT.AA.BB，那么队列Q2将会收到；
     *
     * 当一个队列的绑定键为 "#"（井号） 的时候，这个队列将会无视消息的路由键，接收所有的消息。
     * 当 * (星号) 和 # (井号) 这两个特殊字符都未在绑定键中出现的时候，此时主题交换机就拥有的直连交换机的行为。
     * 所以主题交换机也就实现了扇形交换机的功能，和直连交换机的功能
     * @return
     */
    @Bean
    public TopicExchange topExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topExchangeBingingOne() {
        return BindingBuilder.bind(topicQueueOne()).to(topExchange()).with(TOPIC_ROUTINGKEY_ONE);
    }

    @Bean
    public Binding topicExchangeBingingTwo() {
        return BindingBuilder.bind(topicQueueTwo()).to(topExchange()).with(TOPIC_ROUTINGKEY_TWO);
    }
}