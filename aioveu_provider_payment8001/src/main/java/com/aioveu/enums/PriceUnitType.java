package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 价格时间单位
 */
@Getter
@AllArgsConstructor
public enum PriceUnitType {

    OneHour("按每小时", 60),
    HalfHour("按每半小时", 30);

    /**
     * 描述
     */
    private String description;

    /**
     * 时间单位
     */
    @EnumValue
    private Integer code;

    public static PriceUnitType of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
