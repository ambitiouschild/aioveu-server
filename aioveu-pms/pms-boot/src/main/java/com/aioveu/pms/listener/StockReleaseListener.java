package com.aioveu.pms.listener;

import com.rabbitmq.client.Channel;
import com.aioveu.pms.aioveu05Sku.service.PmsSkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @Description: TODO 商品库存释放监听器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:30
 * @param
 * @return:
 **/

@Component
@RequiredArgsConstructor
@Slf4j
public class StockReleaseListener {

    private final PmsSkuService pmsSkuService;

    private static final String STOCK_RELEASE_QUEUE = "stock.release.queue";
    private static final String STOCK_EXCHANGE = "stock.exchange";
    private static final String STOCK_RELEASE_ROUTING_KEY = "stock.release.routing.key";

    @RabbitListener(bindings =
    @QueueBinding(
            value = @Queue(value = STOCK_RELEASE_QUEUE, durable = "true"),
            exchange = @Exchange(value = STOCK_EXCHANGE),
            key = {STOCK_RELEASE_ROUTING_KEY}
    ),
            ackMode = "MANUAL" // 手动ACK
    )
    @RabbitHandler
    public void handleStockRelease(String orderSn, Message message, Channel channel) throws IOException {
        log.info("订单【{}】取消释放库存消息监听", orderSn);
        long deliveryTag = message.getMessageProperties().getDeliveryTag(); // 消息序号
        try {
            pmsSkuService.unlockStock(orderSn);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicReject(deliveryTag, true);
        }
    }
}
