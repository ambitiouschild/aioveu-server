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


public enum AckTypeEnum implements IBaseEnum<Integer> {

    ACK(0, "Broker确认收到"),
    NACK(1, "Broker拒绝"),
    UNKNOWN(2, "未知"),
    ;

    AckTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }


    @Getter
    private Integer value;

    @Getter
    private String label;
}
