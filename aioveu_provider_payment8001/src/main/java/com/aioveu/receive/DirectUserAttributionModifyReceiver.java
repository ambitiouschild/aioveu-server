package com.aioveu.receive;

import com.aioveu.config.mq.DirectMqConfig;
import com.aioveu.service.UserCallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description 直连交换槽子 用户归属修改
 * @author: 雒世松
 * @date: 2025/03/12 0019 22:45
 */
@Slf4j
@Component
@RabbitListener(queues = DirectMqConfig.USER_ATTRIBUTION_MODIFY_QUEUE)
public class DirectUserAttributionModifyReceiver {

    @Autowired
    private UserCallService userCallService;
 
    @RabbitHandler
    public void process(Map<String, Object> message) {
        log.info("DirectUserAttributionModifyReceiver消费者收到消息  : " + message.toString());
        try {
            String userId = message.get("userId") + "";
            Long storeId = Long.parseLong(message.get("storeId") + "");
            String nickName = message.get("nickName") + "";
            userCallService.autoAddMyPool(message.get("phone") + "", userId, Integer.parseInt(message.get("type") + ""), storeId, nickName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
 
}