package com.aioveu.receive;

import com.aioveu.config.mq.DirectMqConfig;
import com.aioveu.service.UnifiedNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description 异步消息通知接收者
 * @author: 雒世松
 * @date: 2025/02/14 0019 22:45
 */
@Slf4j
@Component
@RabbitListener(queues = DirectMqConfig.SEND_NOTICE_QUEUE)
public class DirectNoticeReceiver {

    @Autowired
    private UnifiedNoticeService unifiedNoticeService;

    @RabbitHandler
    public void process(Map<String, Object> message) {
        log.info("消息通知消费者收到消息  : " + message.toString());
        if (message.get("msgCode") == null) {
            log.error("消息类型为空, 无法判断发送类型");
            return;
        }
        try {
            if (unifiedNoticeService.commonNoticeSend(message)) {
                log.info("消息发送成功:" + message.get("messageId"));
            } else {
                log.warn("消息发送失败:"  + message);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
 
}