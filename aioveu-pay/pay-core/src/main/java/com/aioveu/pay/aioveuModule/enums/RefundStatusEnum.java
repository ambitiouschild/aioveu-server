package com.aioveu.pay.aioveuModule.enums;


import lombok.Getter;

/**
 * @ClassName: RefundStatusEnum
 * @Description TODO 退款状态枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 21:58
 * @Version 1.0
 *  RefundStatusEnum.PROCESSING.getValue()
 **/

public enum RefundStatusEnum {

    PROCESSING(0, "处理中"),
    SUCCESS(1, "退款成功"),
    FAILED(2, "退款失败");

    RefundStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private Integer value;

    @Getter
    private String label;
}
