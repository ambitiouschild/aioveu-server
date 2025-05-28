package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>产品线枚举</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum ProductLine {

    EXERCISE("活动券", 1),
    PRODUCT("商品券", 2),
    FIELD("订场优惠券", 3);

    /** 产品线描述 */
    private String description;

    /** 产品线编码 */
    @EnumValue
    private Integer code;

    public static ProductLine of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
