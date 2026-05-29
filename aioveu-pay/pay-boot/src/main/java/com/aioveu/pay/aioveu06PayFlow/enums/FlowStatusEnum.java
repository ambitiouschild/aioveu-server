package com.aioveu.pay.aioveu06PayFlow.enums;


import lombok.Getter;

/**
 * @ClassName: PayChannel
 * @Description TODO 流水状态枚举
 *                               * 流水状态：0-处理中 1-成功 2-失败
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/5/29 19:22
 * @Version 1.0
 *  FlowStatusEnum.PAYMENT.getValue()
 **/

public enum FlowStatusEnum {

    PROCESSING(0, "处理中"),
    SUCCESS(1, "成功"),
    FAILED(2, "失败");

    FlowStatusEnum(int  value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private int value;

    @Getter
    private String label;
}
