package com.aioveu.oms.aioveu11MqConsumer.consumer;


public class OrderPayConsumer {


    @RabbitListener(queues = "order.pay.success.queue")
    public void onMessage(PayMessage msg, Channel channel, Message message) throws IOException {

        String paymentNo = msg.getPaymentNo();

        try {
            log.info("收到支付成功消息, paymentNo={}", paymentNo);

            // 幂等校验
            if (alreadyProcessed(paymentNo)) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }

            // 发货 / 开通权益
            handleOrderPaid(paymentNo);

            // 手动 ACK
            channel.basicAck(message.getMessageProperties().getDeliveryTag();

        }
}
