package com.aioveu.pay.aioveu13PayCallbackRecord.enums;


/**
 * @ClassName: PayChannelEnum
 * @Description TODO 支付回调记录表，枚举（强烈建议）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/29 18:32
 * @Version 1.0
 **/

public enum PayChannelEnum {

    WECHAT("WECHAT"),
    ALIPAY("ALIPAY"),
    UNION("UNION");

    private final String code;

    PayChannelEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
