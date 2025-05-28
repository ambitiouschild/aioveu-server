package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>优惠券分类</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum CouponCategory {

    MANJIAN("满减券", "001"),
    ZHEKOU("折扣券", "002"),
    LIJIAN("立减券", "003"),
    DUIHUAN("兑换券", "004"),
    UNLIMIT_TIMES("无限次券", "010");

    /** 优惠券描述(分类) */
    private String description;

    /** 优惠券分类编码 */
    // 标记数据库存的值是code
    @EnumValue
    private String code;

    public static CouponCategory of(String code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
