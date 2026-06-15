package com.aioveu.common.enums.pay;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Description: TODO 支付状态枚举
 *                           * 支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败 4-已关闭 5-已退款   支付状态永远用枚举，不用布尔值
 * @Author: 雒世松
 * @Date: 2026/5/11 20:44
 * @param
 * @return:
 **/


public enum PaymentStatusEnum {

    UNPAID(1, "UNPAID", "待支付"),
    PAYING(2, "PAYING", "支付中"),
    PAID(3, "PAID", "支付成功"),
    FAILED(4, "FAILED", "支付失败"),
    CLOSED(5, "CLOSED", "已关闭"),
    REFUNDING(6, "REFUNDING", "退款中"),
    REFUNDED(7, "REFUNDED", "已退款"),
    UNKNOWN(8, "UNKNOWN", "未知")

    ;

    PaymentStatusEnum(int code, String value, String label) {
        this.code = code;
        this.value = value;
        this.label = label;
    }

    @EnumValue                      // 关键！
    @Getter
    private int code;     // OMS 用


    @Getter
    private String value;    // PAY 用

    @Getter
    private String label;


    public static PaymentStatusEnum fromCode(Integer code) {
        for (PaymentStatusEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的支付渠道编码：" + code);
    }
}
