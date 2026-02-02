package com.aioveu.refund.aioveu01RefundOrder.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @ClassName: RefundStatusEnum
 * @Description TODO 退款状态枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 15:08
 * @Version 1.0
 *
 *  RefundStatusEnum.PENDING_AUDIT.getValue()
 **/
public enum RefundStatusEnum implements IBaseEnum<Integer> {

    /**
     * 待处理
     */
    PENDING_AUDIT(0, "待处理"),
    /**
     * 审核中
     */
    Under_Review(1, "审核中"),
    /**
     * 审核通过
     */
    Approved(2, "审核通过"),
    /**
     * 审核拒绝
     */
    Rejected_after_review(3, "审核拒绝"),
    /**
     * 退款中
     */
    Refunding(4, "退款中"),
    /**
     * 退款成功
     */
    Refund_successful(5, "退款成功"),

    /**
     * 退款失败
     */
    Refund_failed(5, "退款失败");

    RefundStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private Integer value;

    @Getter
    private String label;

}
