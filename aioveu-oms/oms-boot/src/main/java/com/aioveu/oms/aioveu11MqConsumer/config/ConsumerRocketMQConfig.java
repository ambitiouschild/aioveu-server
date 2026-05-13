package com.aioveu.oms.aioveu11MqConsumer.config;


import com.aioveu.oms.aioveu11MqConsumer.listener.OrderPaymentMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: ConsumerRocketMQConfig
 * @Description TODO    消费者配置、业务处理配置
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 21:40
 * @Version 1.0
 **/

@Slf4j
@Configuration
public class ConsumerRocketMQConfig {


    @Value("${rocketmq.name-server}")
    private String nameServer;




//    @Bean
    public RocketMQTemplate rocketMQTemplate() {
        // 只保留生产者配置
        RocketMQTemplate template = new RocketMQTemplate();

        DefaultMQProducer producer = new DefaultMQProducer("payment-producer-group");
        producer.setNamesrvAddr(nameServer);
        producer.setSendMsgTimeout(3000);

        template.setProducer(producer);
        return template;
    }

    /**
     * 订单支付消费者
     */
    // ❌ 删除这个 bean，因为已经有注解方式了
//    @Bean
    public DefaultMQPushConsumer orderPaymentConsumer(
            @Value("${rocketmq.consumer.order-service-group.topic:payment_success_topic}") String topic,
            @Value("${rocketmq.consumer.order-service-group.tag:wechat_pay || alipay}") String tag,
            @Value("${rocketmq.consumer.order-service-group.consume-thread-max:20}") int consumeThreadMax,
            OrderPaymentMessageListener messageListener) {  // 注入监听器

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("order_service_group");

        try {
            consumer.setNamesrvAddr(nameServer);
            consumer.subscribe(topic, tag);
            consumer.setConsumeThreadMin(5);
            consumer.setConsumeThreadMax(consumeThreadMax);
            consumer.setConsumeMessageBatchMaxSize(1);  // 顺序消息必须设置为1
            consumer.setPullBatchSize(32);

            // 设置消费超时时间（分钟）
            consumer.setConsumeTimeout(15L);

            // 设置重试策略
            consumer.setMaxReconsumeTimes(3);  // 最大重试3次
            consumer.setSuspendCurrentQueueTimeMillis(1000);  // 挂起1秒


            // 使用注入的监听器

            consumer.registerMessageListener(messageListener);

            consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
                try {
                    // 这里应该调用实际的消息处理逻辑
                    // 比如：orderService.handlePaymentMessage(msgs)
                    log.info("收到消息，数量: {}", msgs.size());
                    return ConsumeOrderlyStatus.SUCCESS;
                } catch (Exception e) {
                    log.error("处理消息异常", e);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            });

            // 启动消费者
            consumer.start();
            log.info("RocketMQ 消费者启动成功: group={}, topic={}, tag={}",
                    "order_service_group", topic, tag);

            return consumer;

        } catch (MQClientException e) {
            log.error("RocketMQ 消费者启动失败", e);
            throw new RuntimeException("RocketMQ 消费者启动失败", e);
        }
    }

    /*
    *
    * 错误原因：MessageListenerOrderly是一个函数式接口，需要实现 consumeMessage方法。
    * 你使用了无参构造，但应该提供这个方法的实现。
    *
    * */
    // ❌ 删除这个 bean ,应该保留：PaymentSuccessConsumer（注解方式）
//    @Bean
    public MessageListenerOrderly orderPaymentListener(OrderPaymentMessageListener listener) {
        return listener;  // 注入已实现的 MessageListenerOrderly
    }

}
