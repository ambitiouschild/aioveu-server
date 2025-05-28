package com.aioveu.service.impl;

import com.aioveu.config.mq.DelayMqConfig;
import com.aioveu.config.mq.DirectMqConfig;
import com.aioveu.data.sync.FieldSyncMessage;
import com.aioveu.entity.ChargingChange;
import com.aioveu.entity.OperateLog;
import com.aioveu.form.GradeEnrollUserForm;
import com.aioveu.service.MQMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/6/15 18:02
 */
@Service
@Slf4j
public class MQMessageServiceImpl implements MQMessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendDelayMessage(Map<String, Object> msgMap, int delaySecond) {
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (msgMap.get("messageId") == null) {
            msgMap.put("messageId", String.valueOf(UUID.randomUUID()));
        }
        msgMap.put("createTime", createTime);
        rabbitTemplate.convertAndSend(DelayMqConfig.DELAY_EXCHANGE, DelayMqConfig.DELAY_ROUTE_KEY, msgMap, (message) -> {
//            message.getMessageProperties().setHeader("x-delay", delaySecond);
            message.getMessageProperties().setDelay(delaySecond);
            return message;
        });

    }

    @Override
    public void sendDelayMsgByDate(Map<String, Object> msgMap, Date delayDate) {
        Date now = new Date();
        if (delayDate.before(now)) {
            log.error("延迟时间不能小于当前时间");
            return;
        }
        int second = (int) (delayDate.getTime() - now.getTime());
        sendDelayMessage(msgMap, second);
    }

    @Override
    public void changeOrderStatus(String orderId, String userId) {
        //将消息携带绑定键值：DIRECT_ROUTE_ORDER_STATUS_KEY 发送到交换机DIRECT_EXCHANGE
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("orderId", orderId);
        msgMap.put("userId", userId);
        sendSyncMessage(msgMap, DirectMqConfig.DIRECT_ROUTE_ORDER_STATUS_KEY);
    }


    /**
     * 发送场地状态变更同步消息
     * @param fieldSyncMessage
     */
    @Override
    public void sendFieldSyncMessage(FieldSyncMessage fieldSyncMessage) {
        rabbitTemplate.convertAndSend(DirectMqConfig.DIRECT_EXCHANGE, DirectMqConfig.DIRECT_ROUTE_FIELD_SYNC_KEY, fieldSyncMessage);
    }

    @Override
    public void sendCreateFieldPlanMessage(Map<String, Object> msgMap) {
        sendSyncMessage(msgMap, DirectMqConfig.CREATE_FIELD_PLAN_SYNC_KEY);
    }

    /**
     * 发送异步消息通用逻辑
     * @param msgMap
     * @param routeKey 路由key
     */
    private void sendSyncMessage(Map<String, Object> msgMap, String routeKey) {
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (msgMap.get("messageId") == null) {
            msgMap.put("messageId", String.valueOf(UUID.randomUUID()));
        }
        msgMap.put("createTime", createTime);
        rabbitTemplate.convertAndSend(DirectMqConfig.DIRECT_EXCHANGE, routeKey, msgMap);
    }

    @Override
    public void sendNoticeMessage(Map<String, Object> msgMap, String msgOptionCode, Long storeId) {
        msgMap.put("msgCode", msgOptionCode);
        msgMap.put("storeId", storeId);
        sendSyncMessage(msgMap, DirectMqConfig.SEND_NOTICE_KEY);
    }

    @Override
    public void sendOperateLogMessage(OperateLog operateLog) {
        rabbitTemplate.convertAndSend(DirectMqConfig.DIRECT_EXCHANGE, DirectMqConfig.SEND_OPERATE_LOG_KEY, operateLog);
    }

    @Override
    public void sendUserAttributeModifyMessage(String userPhone, String userId, Integer type, Long storeId, String nickName) {
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("phone", userPhone);
        msgMap.put("userId", userId);
        msgMap.put("type", type);
        msgMap.put("storeId", storeId);
        msgMap.put("nickName", nickName);
        sendSyncMessage(msgMap, DirectMqConfig.USER_ATTRIBUTION_MODIFY_KEY);
    }

    @Override
    public void gradeEnroll(GradeEnrollUserForm form) {
        rabbitTemplate.convertAndSend(DirectMqConfig.DIRECT_EXCHANGE, DirectMqConfig.GRADE_ENROLL_KEY, form);
    }

    @Override
    public void sendChargingChange(ChargingChange chargingChange) {
        rabbitTemplate.convertAndSend(DirectMqConfig.DIRECT_EXCHANGE, DirectMqConfig.STORE_CHARGING_CHANGE_KEY, chargingChange);
    }
}
