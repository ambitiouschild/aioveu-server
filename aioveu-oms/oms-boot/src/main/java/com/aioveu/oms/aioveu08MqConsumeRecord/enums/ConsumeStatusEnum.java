package com.aioveu.oms.aioveu08MqConsumeRecord.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @Description: TODO 消息消费状态枚举
 * @Author: 雒世松
 * @Date: 2026/5/11 22:52
 * @param
 * @return:
 **/


public enum ConsumeStatusEnum implements IBaseEnum<Integer> {

    UNCONSUMED(0, "未消费"),
    PROCESSING(1, "消费中"), // APP订单
    SUCCESS(2, "消费成功"),
    FAILED(3, "消费失败"),
    DEAD(4, "进入死信队列"),
    RETRIED(5, "已重试"),
    TIMEOUT(6, "已超时"), // 网页

    ;

    ConsumeStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }


    @Getter
    private Integer value;

    @Getter
    private String label;


    /**
     * 根据 value 获取枚举
     */
    public static ConsumeStatusEnum fromValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ConsumeStatusEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("未知的消费状态：" + value);
    }
}
