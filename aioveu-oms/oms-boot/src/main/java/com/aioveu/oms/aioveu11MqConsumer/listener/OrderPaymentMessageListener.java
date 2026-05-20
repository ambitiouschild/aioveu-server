package com.aioveu.oms.aioveu11MqConsumer.listener;


import com.aioveu.oms.aioveu11MqConsumer.service.MqConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: OrderPaymentMessageListener
 * @Description TODO 订单支付消息监听器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 21:34
 * @Version 1.0
 **/

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentMessageListener implements MessageListenerOrderly {

    private final MqConsumerService mqConsumerService;

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        for (MessageExt msg : msgs) {
            try {
                String topic = msg.getTopic();
                String tags = msg.getTags();
                String keys = msg.getKeys();
                String body = new String(msg.getBody());

                log.info("收到支付成功消息: topic={}, tags={}, keys={}", topic, tags, keys);


                // 调用业务处理
                boolean success = mqConsumerService.handlePaymentSuccess(body);

                if (!success) {
                    log.warn("处理消息失败，稍后重试: msgId={}", msg.getMsgId());
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }

            } catch (Exception e) {
                log.error("处理消息异常: msgId={}", msg.getMsgId(), e);
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }

        return ConsumeOrderlyStatus.SUCCESS;
    }
}
