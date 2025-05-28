package com.aioveu.service;

import com.aioveu.data.sync.FieldSyncMessage;
import com.aioveu.entity.ChargingChange;
import com.aioveu.entity.OperateLog;
import com.aioveu.form.GradeEnrollUserForm;

import java.util.Date;
import java.util.Map;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/6/15 18:02
 */
public interface MQMessageService {

    /**
     * 延迟发送消息
     * @param msgMap
     * @param delaySecond 延迟时间 (秒)
     */
    void sendDelayMessage(Map<String, Object> msgMap, int delaySecond);

    /**
     * 根据具体日期延迟消息发送
     * @param msgMap
     * @param delayDate
     */
    void sendDelayMsgByDate(Map<String, Object> msgMap, Date delayDate);

    /**
     * 订单状态修改消息
     * @param orderId
     * @param userId
     */
    void changeOrderStatus(String orderId, String userId);

    /**
     * 发送场地同步消息
     * @param fieldSyncMessage
     */
    void sendFieldSyncMessage(FieldSyncMessage fieldSyncMessage);

    /**
     * 发送Direct消息,通用逻辑
     * @param msgMap
     */
    void sendCreateFieldPlanMessage(Map<String, Object> msgMap);

    /**
     * 发送异步消息通知
     * @param msgMap
     * @param msgOptionCode 发送消息的类型
     * @param storeId
     */
    void sendNoticeMessage(Map<String, Object> msgMap, String msgOptionCode, Long storeId);

    /**
     * 发送操作日志
     * @param operateLog
     */
    void sendOperateLogMessage(OperateLog operateLog);

    /**
     * 发送用户归属修改
     * @param userPhone
     * @param userId
     * @param type
     * @param storeId
     * @param nickName
     */
    void sendUserAttributeModifyMessage(String userPhone, String userId, Integer type, Long storeId, String nickName);

    /**
     * 异步约课
     * @param form
     */
    void gradeEnroll(GradeEnrollUserForm form);

    /**
     * 发送增值服务变动记录
     * @param chargingChange
     */
    void sendChargingChange(ChargingChange chargingChange);


}
