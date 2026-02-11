package com.aioveu.pay.aioveu04PayReconciliation.enums;


import lombok.Getter;

/**
 * @ClassName: PayReconciliationStatusEnum
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 13:52
 * @Version 1.0
 **/

public enum PayReconciliationStatusEnum {

    ALIPAY(0, "未对账"),
    RECONCILING(1, "对账中"),
    UNIONPAY(2, "对账完成"),
    NOT_RECONCILED(3, "对账异常");

    PayReconciliationStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private Integer value;

    @Getter
    private String label;
}
