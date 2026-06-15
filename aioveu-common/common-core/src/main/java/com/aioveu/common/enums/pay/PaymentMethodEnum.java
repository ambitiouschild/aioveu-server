package com.aioveu.common.enums.pay;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Description: TODO 支付方式枚举
 * @Author: 雒世松
 * @Date: 2026/5/11 20:44
 * @param
 * @return:
 **/


public enum PaymentMethodEnum {

    APP(1, "APP", "APP支付"),
    H5(2, "H5", "H5支付"),
    JSAPI(3, "JSAPI", "小程序/公众号支付"),
    NATIVE(4, "NATIVE", "扫码支付"),
    UNKNOWN(6, "UNKNOWN", "未知");

    ;

    PaymentMethodEnum(int code, String value, String label) {
        this.code = code;
        this.value = value;
        this.label = label;
    }

                // 关键！
    @Getter
    private int code;     // OMS 用

    @EnumValue
    @Getter
    private String value;    // PAY 用

    @Getter
    private String label;


    public static PaymentMethodEnum fromCode(Integer code) {
        for (PaymentMethodEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的支付渠道编码：" + code);
    }
}
