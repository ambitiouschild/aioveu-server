package com.aioveu.common.enums.pay;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Description: TODO 支付订单业务状态枚举
 * @Author: 雒世松
 * @Date: 2026/5/11 20:44
 * @param
 * @return:
 **/


public enum PaymentBizTypeEnum {

    ORDER_PAY(1, "ORDER_PAY", "订单支付"),
    VIP_PAY(2, "VIP_PAY", "会员支付"),
    REFUND(3, "REFUND", "退款"),
    RECHARGE(4, "RECHARGE", "充值")

    ;

    PaymentBizTypeEnum(int code, String value, String label) {
        this.code = code;
        this.value = value;
        this.label = label;
    }

    @Getter
    private int code;     // OMS 用

    @EnumValue                      // 关键！
    @Getter
    private String value;    // PAY 用

    @Getter
    private String label;


    public static PaymentBizTypeEnum fromCode(Integer code) {
        for (PaymentBizTypeEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的支付渠道编码：" + code);
    }
}
