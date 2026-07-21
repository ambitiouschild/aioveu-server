package com.aioveu.common.enums.pay;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;


/**
 * @Description: TODO 支付场景枚举（关键抽象）
 * @Author: 雒世松
 * @Date: 2026/7/20 19:52
 * @param
 * @return:
 **/

public enum PaymentSceneEnum {


    ORDER(1, "ORDER", "商品订单"),
    MEMBERSHIP(2, "MEMBERSHIP", "会员开通"),
    RECHARGE(3, "RECHARGE", "账户充值"),
    ACTIVITY(4, "ACTIVITY", "活动订单");

    ;

    PaymentSceneEnum(int code, String value, String label) {
        this.code = code;
        this.value = value;
        this.label = label;
    }

    @JsonValue   // ✅ 关键：告诉 Jackson，JSON 用这个
    @EnumValue
    @Getter
    private int code;     // OMS 用

    @Getter
    private String value;    // PAY 用

    @Getter
    private String label;


    public static PaymentSceneEnum fromCode(Integer code) {
        for (PaymentSceneEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的支付场景：" + code);
    }

}
