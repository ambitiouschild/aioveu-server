package com.aioveu.oms.aioveu11MqConsumer.consumer;


import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.rabbitmq.producer.model.payment.PaymentMessage;
import com.aioveu.oms.aioveu01Order.service.app.OrderService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
/**
 * @ClassName: OrderPayConsumer
 * @Description TODO OrderPayConsumer
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/21 16:53
 * @Version 1.0
 **/

/*
* ✅ 优点：
Watch Dog 自动续期
防误删
生产环境最稳
*
* */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPayConsumer {


    private final OrderService orderService;
    @Autowired
    private RedissonClient redisson;

    /**
     * 支付成功消息消费
     */
    @RabbitListener(
            queues = "order.pay.success.queue",
            ackMode = "MANUAL"
    )
    public void onMessage(
            PaymentMessage msg,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
    ) throws IOException {

        String paymentNo = msg.getPaymentNo();  //支付侧唯一键
        String orderNo = msg.getOrderSn(); // ✅ 强烈建议加上 订单侧业务键

        log.info("收到支付成功MQ, paymentNo={}, orderNo={}", paymentNo, orderNo);

        // ==================== 1. 参数校验 ====================
        if (paymentNo == null || orderNo == null) {
            log.warn("支付成功消息参数异常, message={}", msg);
            channel.basicAck(deliveryTag, false);
            return;
        }

        // ==================== 2. 幂等锁（Redis） ====================
        //✅ Redis Lock 保证： 同一 paymentNo 同一时刻只能被一个线程处理
        String lockKey = "lock:pay:success:" + paymentNo;
        RLock lock = redisson.getLock(lockKey);




        try {

            if (!lock.tryLock(0, 10, TimeUnit.SECONDS)) {
                log.warn("支付成功消息重复处理或并发冲突, paymentNo={}", paymentNo);
                // ❗ 这里用 nack + requeue=false，防止死循环
                channel.basicNack(deliveryTag, false, false);
                return;
            }


            log.info("收到支付成功消息, paymentNo={}", paymentNo);

            // ==================== 3. 业务处理 ====================
            orderService.handleOrderPaid(paymentNo, orderNo);

            // ==================== 4. 确认消费 ====================
            channel.basicAck(deliveryTag, false);

        }catch (BusinessException e) {
            // ✅ 业务异常：记录 + 进死信队列
            log.error("支付成功业务处理失败, 数据错误，重试也没用,paymentNo={}, err={}",
                    paymentNo, e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);

        } catch (Exception e) {
            // ✅ 系统异常：可重试
            log.error("支付成功系统异常,数据库抖动，可重试, paymentNo={}", paymentNo, e);
            channel.basicNack(deliveryTag, false, true);

        } finally {
            // ==================== 5. 释放锁 ====================
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }
}
