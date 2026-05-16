package com.aioveu.pay.aioveu10MqSendRecord.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO 消息发送状态枚举
 * @Author: 雒世松
 * @Date: 2026/5/11 20:44
 * @param
 * @return:
 **/

/*
*
         Integer statusValue = entity.getSendStatus(); // 假设这是从数据库读取的值
        SendStatus status = SendStatus.fromValue(statusValue);
*
* */

public enum SendStatus implements IBaseEnum<Integer> {

    PENDING(0, "未发送"),
    SENDING(1, "发送中"),
    SUCCESS(2, "发送成功（到达Broker并被确认）"),

    FAILED(3, "发送失败"),

    CANCELLED(4, "已取消"),

    TIMEOUT(5, "已超时"),

    DEAD(6, "进入死信队列"),

    ROUTING_FAILED(7, "路由失败（触发ReturnCallback"),         // 路由失败（触发ReturnCallback）
    CONFIRM_TIMEOUT(8, "确认超时"),
    CONFIRM_NACK(9, "Broker返回NACK"),
    UNKNOWN (10, "未知状态"),
    ;



    SendStatus(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private Integer value;

    @Getter
    private String label;

    // 静态映射，提高查找效率
    private static final Map<Integer, SendStatus> VALUE_MAP = new HashMap<>();

    static {
        // 初始化映射
        for (SendStatus status : SendStatus.values()) {
            VALUE_MAP.put(status.getValue(), status);
        }
    }


    /**
     * 通过value查找枚举
     */
    public static SendStatus fromValue(Integer value) {
        if (value == null) {
            return UNKNOWN;
        }
        return VALUE_MAP.getOrDefault(value, UNKNOWN);
    }

    /**
     * 通过value查找枚举（严格模式，找不到抛异常）
     */
    public static SendStatus fromValueStrict(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("value不能为空");
        }
        SendStatus status = VALUE_MAP.get(value);
        if (status == null) {
            throw new IllegalArgumentException("无效的value: " + value);
        }
        return status;
    }

    /**
     * 检查value是否存在
     */
    public static boolean containsValue(Integer value) {
        return VALUE_MAP.containsKey(value);
    }

    /**
     * 获取所有有效的value
     */
    public static List<Integer> getAllValues() {
        return new ArrayList<>(VALUE_MAP.keySet());
    }

}
