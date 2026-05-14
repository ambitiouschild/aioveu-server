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


public enum ErrorCategory implements IBaseEnum<Integer> {

    SUCCESS(0, "成功"),
    TIMEOUT(1, "超时"),
    ROUTING(2, "路由失败"),
    BROKER(3, "Broker拒绝"),
    NETWORK(4, "网络问题"),
    OTHER(5, "其他"),
    ;

    ErrorCategory(Integer value, String label) {
        this.value = value;
        this.label = label;
    }


    @Getter
    private Integer value;

    @Getter
    private String label;
}
