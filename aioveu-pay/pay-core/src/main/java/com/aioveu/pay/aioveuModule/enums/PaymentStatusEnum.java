package com.aioveu.pay.aioveuModule.enums;


import lombok.Getter;

/**
 * @ClassName: PaymentStatus
 * @Description TODO 支付状态枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:42
 * @Version 1.0
 *
 *  PaymentStatusEnum.PENDING.getValue()
 **/

public enum PaymentStatusEnum {

    PENDING(0, "待支付"),
    PROCESSING(1, "支付中"),
    SUCCESS(2, "支付成功"),
    FAILED(3, "支付失败"),
    CLOSED(4, "已关闭"),
    REFUNDED(5, "已退款");

    PaymentStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private Integer value;

    @Getter
    private String label;
}
