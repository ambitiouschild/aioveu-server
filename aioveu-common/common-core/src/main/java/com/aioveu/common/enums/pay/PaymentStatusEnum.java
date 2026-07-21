package com.aioveu.common.enums.pay;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Description: TODO 支付状态枚举
 *                           * 支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败 4-已关闭 5-已退款   支付状态永远用枚举，不用布尔值
 * @Author: 雒世松
 * @Date: 2026/5/11 20:44
 * @param
 * @return:
 **/

//⚠️ Jackson 默认序列化：enum.name() 前端返回name
//json.writeField("paymentStatus", PaymentStatusEnum.PAID.name());
public enum PaymentStatusEnum {

    UNPAID(0, "UNPAID", "待支付"),
    PAYING(10, "PAYING", "支付中"),
    PAID(20, "PAID", "支付成功"),
    REFUNDING(30, "REFUNDING", "退款中"),
    REFUNDED(31, "REFUNDED", "已退款"),
    FAILED(40, "FAILED", "支付失败"),
    CLOSED(50, "CLOSED", "已关闭"),
    UNKNOWN(60, "UNKNOWN", "未知")

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

    /**
     * ✅ 判断是否为终态
     * 终态：支付成功 / 支付失败 / 已关闭 / 已退款
     */
    public static boolean isTerminal(PaymentStatusEnum status) {
        if (status == null) {
            return false;
        }
        return status == PAID
                || status == FAILED
                || status == CLOSED
                || status == REFUNDED;
    }

    /**
     * ✅ 是否可进行支付操作
     */
    public static boolean canPay(PaymentStatusEnum status) {
        return status == UNPAID || status == PAYING;
    }

    /**
     * ✅ 是否可关闭
     */
    public static boolean canClose(PaymentStatusEnum status) {
        return status == UNPAID || status == PAYING;
    }



}
