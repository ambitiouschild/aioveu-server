package com.aioveu.refund.aioveu04RefundOperationLog.enums;

import lombok.Getter;

/**
 * @ClassName: OperationTypeEnum
 * @Description TODO  操作类型枚举：1-用户申请 2-客服审核 3-财务审核 4-商家处理 5-用户操作 6-系统自动
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 18:02
 * @Version 1.0
 *
 *  OperationTypeEnum.Merchant_processing.getValue()
 **/
public enum OperationTypeEnum {

    /**
     * 用户申请
     */
    User_Application(1, "用户申请"),
    /**
     * 客服审核
     */
    Customer_service_review(2, "客服审核"),
    /**
     * 财务审核
     */
    Financial_Review(3, "财务审核"),
    /**
     * 商家处理
     */
    Merchant_processing(4, "商家处理"),
    /**
     * 用户操作
     */
    user_operation(5, "用户操作"),
    /**
     * 系统自动
     */
    System_automatic(6, "系统自动");


    OperationTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private Integer value;

    @Getter
    private String label;
}
