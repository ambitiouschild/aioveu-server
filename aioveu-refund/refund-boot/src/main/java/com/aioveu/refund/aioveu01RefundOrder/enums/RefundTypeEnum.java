package com.aioveu.refund.aioveu01RefundOrder.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @ClassName: RefundTypeEnum
 * @Description TODO 退款类型枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 15:36
 * @Version 1.0
 *
 *  RefundTypeEnum.RETURN_AND_REFUND.getValue()
 **/
public enum RefundTypeEnum implements IBaseEnum<Integer> {

    /**
     * 仅退款
     */
    ONLY_REFUND(1, "仅退款"),
    /**
     * 退货退款
     */
    RETURN_AND_REFUND(2, "退货退款"),
    /**
     * 换货
     */
    EXCHANGE(3, "换货");


    RefundTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private Integer value;

    @Getter
    private String label;

}
