package com.aioveu.pay.aioveu06PayFlow.enums;


import lombok.Getter;

/**
 * @ClassName: PayChannel
 * @Description TODO 流水类型枚举
 *                               * 流水类型：PAYMENT-支付 REFUND-退款 SETTLEMENT-结算 ADJUST-调账
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/5/29 19:18
 * @Version 1.0
 *  FlowTypeEnum.PAYMENT.getValue()
 **/

public enum FlowTypeEnum {

    PAYMENT("PAYMENT", "支付"),
    REFUND("REFUND", "退款"),
    SETTLEMENT("SETTLEMENT", "结算"),
    ADJUST("ADJUST", "调账");

    FlowTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private String value;

    @Getter
    private String label;
}
