package com.aioveu.common.core.enums.pay;

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
    PAGE(5, "PAGE", "网页支付"),
    WAP(6, "WAP", "WAP,手机浏览器（非 App、非微信内）唤起微信 / 支付宝 完成支付,现在一般叫 H5 支付，WAP 是旧称"),
    BALANCE(7, "BALANCE", "余额支付"),
    UNKNOWN(8, "UNKNOWN", "未知");

    ;

    PaymentMethodEnum(int code, String value, String label) {
        this.code = code;
        this.value = value;
        this.label = label;
    }

                // 关键！
    @Getter
    private int code;     // OMS 用  // OMS 用（INT 列）

//    @EnumValue
    //去掉 @EnumValue，用全局 TypeHandler（架构最干净）
    //PAY 服务注册 code TypeHandler
    @Getter
    private String value;    // PAY 用 // PAY 用（VARCHAR 列）

    @Getter
    private String label;

    // ---------- 反向查找 ----------
    public static PaymentMethodEnum fromCode(Integer code) {

        if (code == null) return null;  // ✅ 改这里，别抛异常

        for (PaymentMethodEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的支付渠道编码：" + code);
    }

    public static PaymentMethodEnum fromValue(String value) {
        if (value == null || value.isEmpty()) return null;
        for (PaymentMethodEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return UNKNOWN;
    }
}
