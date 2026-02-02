package com.aioveu.refund.aioveu04RefundOperationLog.enums;

import lombok.Getter;

/**
 * @ClassName: OperatorTypeEnum
 * @Description TODO 操作人类型枚举：1-用户 2-客服 3-商家 4-系统
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 18:09
 * @Version 1.0
 *
 *  OperatorTypeEnum.merchant.getValue()
 **/
public enum OperatorTypeEnum {

    /**
     * 用户
     */
    user(1, "用户"),
    /**
     * 客服
     */
    customer_service(2, "客服"),
    /**
     * 商家
     */
    merchant(3, "商家"),
    /**
     * 系统
     */
    system(4, "系统");


    OperatorTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private Integer value;

    @Getter
    private String label;
}
