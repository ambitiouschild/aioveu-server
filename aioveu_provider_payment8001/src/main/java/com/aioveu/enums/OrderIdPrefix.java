package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>订单ID前缀</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum OrderIdPrefix {

    GENERAL_EXERCISE("普通活动的订单前缀 包括 课程 体验课", "ED"),
    FIELD("订场活动订单前缀", "FD"),
    COUNT("按次活动的订单前缀", "CD");

    private String description;

    @EnumValue
    private String code;

    public static OrderIdPrefix of(String code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
