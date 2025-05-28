package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>地推奖励类型</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum PushRewardType {

    ORDER_DETAIL("预约奖励, 给地推人员", 0),
    CHECK("到店奖励, 给地推人员", 1),
    TO_STORE("到店礼, 给用户", 2),
    ORDER("下单奖励", 5);

    private String description;

    @EnumValue
    private Integer code;

    public static PushRewardType of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
