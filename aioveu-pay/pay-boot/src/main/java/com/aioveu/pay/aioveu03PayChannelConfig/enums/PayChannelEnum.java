package com.aioveu.pay.aioveu03PayChannelConfig.enums;


import lombok.Getter;

/**
 * @ClassName: PayChannel
 * @Description TODO 支付渠道枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:43
 * @Version 1.0
 *  PayChannelEnum.ALIPAY.getValue()
 **/

public enum PayChannelEnum {

    ALIPAY(1, "支付宝"),
    WECHAT(2, "微信支付"),
    UNIONPAY(3, "银联支付"),
    BALANCE(4, "余额支付");

    PayChannelEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private Integer value;

    @Getter
    private String label;
}
