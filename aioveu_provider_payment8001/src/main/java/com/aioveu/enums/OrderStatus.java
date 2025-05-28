package com.aioveu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>订单状态</h1>
 * Created by 雒世松12
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {

    DELETE("已删除", 0),
    UN_PAY("待支付", 1),
    PAY("已支付, 待使用", 2),
    USED("已使用, 待评价", 3),
    USING("使用中", 4),
    ORDER_FINISH( "已评价, 完成", 6),
    REFUND("已退款 已取消", 9),
    ORDER_REFUNDING( "退款中", 12),
    ORDER_UN_PAY_CANCEL( "用户未支付取消的订单", 16),
    SYSTEM_ORDER_CANCEL( "系统取消的订单", 18);

    /** 有效期描述 */
    private String description;

    /** 有效期编码 */
    private Integer code;

    public static OrderStatus of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
