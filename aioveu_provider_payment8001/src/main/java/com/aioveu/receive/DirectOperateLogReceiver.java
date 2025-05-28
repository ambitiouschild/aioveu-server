package com.aioveu.receive;

import com.aioveu.config.mq.DirectMqConfig;
import com.aioveu.entity.OperateLog;
import com.aioveu.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description 直连交换槽子日志消息接收
 * @author: 雒世松
 * @date: 2025/03/12 0019 22:45
 */
@Slf4j
@Component
@RabbitListener(queues = DirectMqConfig.SEND_OPERATE_LOG_QUEUE)
public class DirectOperateLogReceiver {

    @Autowired
    private OperateLogService operateLogService;
 
    @RabbitHandler
    public void process(OperateLog operateLog) {
        try {
            operateLogService.save(operateLog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
 
}