package com.aioveu.receive;

import com.alibaba.fastjson.JSONObject;
import com.aioveu.config.mq.DirectMqConfig;
import com.aioveu.enums.DirectMessageType;
import com.aioveu.service.FieldPlanTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *  订场模版生成通过mq
 * @Author： yao
 * @Date： 2025/1/22 20:50
 * @Describe：
 */
@Slf4j
@Component
@RabbitListener(queues = DirectMqConfig.CREATE_FILE_PLAN_QUEUE)
public class DirectCreateFieldPlanReceiver {

    @Autowired
    private FieldPlanTemplateService fieldPlanTemplateService;

    @RabbitHandler
    public void process(Map<String, Object> message) {
        if (message == null){
            return;
        }
        log.info("DirectMessageReceiver消费者收到消息  : " + message.toString());
        Object type = message.get("type");
        if (type == null){
            return;
        }
        String messageStr = JSONObject.toJSONString(message);
        try {
            JSONObject messageJson = JSONObject.parseObject(messageStr);
            Integer typeCode = messageJson.getInteger("type");
            if (typeCode == DirectMessageType.FIELD_PLAN_TEMPLATE_CREATE.getCode()) {
                fieldPlanTemplateService.createFullFieldPlanById(messageJson.getString("id"));
            }
        }catch (Exception e){
            log.error(String.format("messageHandle error: %s", messageStr));
            log.error(e.getMessage(), e);
        }
    }
}
