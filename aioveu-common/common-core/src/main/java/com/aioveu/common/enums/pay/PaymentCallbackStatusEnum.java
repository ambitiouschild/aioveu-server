package com.aioveu.common.enums.pay;


import lombok.Getter;

/**
 * @ClassName: PaymentCallbackStatusEnum
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/29 19:00
 * @Version 1.0
 **/
@Getter
public enum PaymentCallbackStatusEnum {

    PENDING(0, "待支付"),
    PROCESSING(1, "支付中"),
    /** 支付成功 */
    SUCCESS(2, "支付成功"),

    /** 支付失败 */
    FAILED(3, "支付失败"),

    /** 已关闭 */
    CLOSED(4, "已关闭");

    private final int code;
    private final String desc;

    PaymentCallbackStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
