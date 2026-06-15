package com.aioveu.common.enums.pay;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Description: TODO 消息发送状态枚举
 * @Author: 雒世松
 * @Date: 2026/5/11 20:44
 * @param
 * @return:
 **/


public enum PaymentChannelEnum{

    ALIPAY(1, "ALIPAY", "支付宝"),
    WECHAT(2, "WECHAT", "微信支付"),
    UNIONPAY(3, "UNIONPAY", "银联"),
    BALANCE(4, "BALANCE", "余额");

    ;

    PaymentChannelEnum(int code, String value, String label) {
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


    public static PaymentChannelEnum fromCode(Integer code) {
        for (PaymentChannelEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的支付渠道编码：" + code);
    }
}
