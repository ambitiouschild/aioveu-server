package com.aioveu.receive;

import com.aioveu.config.mq.DirectMqConfig;
import com.aioveu.entity.ChargingChange;
import com.aioveu.service.ChargingChangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description 直连交换槽子 增值服务变动记录
 * @author: 雒世松
 * @date: 2025/03/12 0019 22:45
 */
@Slf4j
@Component
@RabbitListener(queues = DirectMqConfig.STORE_CHARGING_CHANGE_QUEUE)
public class DirectStoreChargingChangeReceiver {

    @Autowired
    private ChargingChangeService chargingChangeService;
 
    @RabbitHandler
    public void process(ChargingChange chargingChange) {
        try {
            chargingChangeService.change(chargingChange);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
 
}