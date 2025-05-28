package com.aioveu.receive;

import com.alibaba.fastjson.JSONObject;
import com.aioveu.config.mq.DelayMqConfig;
import com.aioveu.enums.DelayMessageType;
import com.aioveu.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description 延迟消息接收
 * @author: 雒世松
 * @date: 2025/6/19 0019 22:45
 */
@Slf4j
@Component
public class DelayReceiver {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private GradeEnrollUserService gradeEnrollUserService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JoinExerciseSettleRecordService joinExerciseSettleRecordService;

    @RabbitListener(queues = DelayMqConfig.DELAY_QUEUE)
    public void receive(Map<String, Object> message) {
        log.info("DelayReceiver消费者收到消息  : " + message.toString());
        messageHandle(message);
    }

    private void messageHandle(Map<String, Object> message) {
        String messageStr = JSONObject.toJSONString(message);
        JSONObject messageJson = JSONObject.parseObject(messageStr);
        try {
            if (message.get("type") != null) {
                Integer typeCode = Integer.parseInt(message.get("type") + "");
                DelayMessageType delayMessageType = DelayMessageType.of(typeCode);
                if (delayMessageType.equals(DelayMessageType.GRADE_MINI_CHECK)) {
                    gradeService.miniNumberCheck(Long.parseLong(message.get("gradeId") + ""));
                } else if (delayMessageType.equals(DelayMessageType.GRADE_FINISH)) {
                    gradeEnrollUserService.gradeFinish(Long.parseLong(message.get("gradeId") + ""));
                } else if (delayMessageType.equals(DelayMessageType.FIELD_FINISH)) {
                    codeService.checkFieldOrderCode(null, message.get("consumeCode") + "", false);
                } else if (delayMessageType.equals(DelayMessageType.UNPAY_CANCEL)) {
                    // 订单 支付超时回调 取消订单
                    orderService.cancelUnPayOrder(message.get("orderId") + "");
                } else if (delayMessageType.equals(DelayMessageType.JOIN_EXERCISE_FINISH)) {
                    joinExerciseSettleRecordService.settleJoinOrder(
                            messageJson.getString("exerciseId") ,
                            messageJson.getLong("categoryId"),
                            messageJson.getDate("endTime") ,
                            messageJson.getString("messageId") );
                } else if (delayMessageType.equals(DelayMessageType.COUNT_EXERCISE_FINISH)) {
                    orderService.autoFinishOrder(message.get("orderId") + "");
                }
            } else {
                log.error(String.format("延迟消息类型为空: %s", messageStr));
            }
        } catch (Exception e) {
            log.error(String.format("messageHandle error: %s", messageStr));
            log.error(e.getMessage(), e);
        }
    }

    @RabbitListener(queues = DelayMqConfig.DELAY_TEST_QUEUE)
    public void receiveTestMessage(Map<String, Object> message) {
        log.info("DelayTestReceiver消费者收到消息  : " + message.toString());
        messageHandle(message);
    }

}
