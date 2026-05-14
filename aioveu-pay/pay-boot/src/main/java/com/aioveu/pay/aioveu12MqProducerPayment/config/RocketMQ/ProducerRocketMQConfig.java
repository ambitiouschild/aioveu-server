package com.aioveu.pay.aioveu12MqProducerPayment.config.RocketMQ;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: RocketMQConfig
 * @Description TODO RocketMQ生产者配置类  主要职责：生产者配置、回调接口配置
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 19:22
 * @Version 1.0
 **/

@Slf4j
@Configuration
public class ProducerRocketMQConfig {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    /**
     * RocketMQTemplate Bean
     */
    @Bean
    public RocketMQTemplate rocketMQTemplate() {
        RocketMQTemplate template = new RocketMQTemplate();

        try {

            // 支付服务只需要生产者，发送支付成功/失败消息
            DefaultMQProducer producer = new DefaultMQProducer("payment-producer-group");
            producer.setNamesrvAddr(nameServer);
            producer.setSendMsgTimeout(3000);
            producer.setRetryTimesWhenSendFailed(2);
            producer.setRetryTimesWhenSendAsyncFailed(2);
            producer.setVipChannelEnabled(false);  // 重要：VIP通道通常关闭

            // 启动生产者
            producer.start();
            log.info("RocketMQ 生产者启动成功: {}", producer.getProducerGroup());

            template.setProducer(producer);
            return template;

        } catch (MQClientException e) {
            log.error("RocketMQ 生产者启动失败", e);
            throw new RuntimeException("RocketMQ 生产者启动失败", e);
        }
    }


//    @Bean
//    public PaymentCallbackConfig paymentCallbackConfig() {
//        // 支付回调相关的配置
//        return new PaymentCallbackConfig();
//    }


}
