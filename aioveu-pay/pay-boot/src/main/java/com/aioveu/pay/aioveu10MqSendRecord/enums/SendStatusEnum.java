package com.aioveu.pay.aioveu10MqSendRecord.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @Description: TODO 消息发送状态枚举
 * @Author: 雒世松
 * @Date: 2026/5/11 20:44
 * @param
 * @return:
 **/


public enum SendStatusEnum implements IBaseEnum<Integer> {

    PENDING(0, "未发送"),
    SENDING(1, "发送中"),
    SUCCESS(2, "发送成功"),

    FAILED(3, "发送失败"),

    CANCELLED(4, "已取消"),

    TIMEOUT(5, "已超时"),

    DEAD(6, "进入死信队列"),

    ;

    SendStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }


    @Getter
    private Integer value;

    @Getter
    private String label;
}
