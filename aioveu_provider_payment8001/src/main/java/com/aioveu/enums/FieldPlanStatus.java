package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum FieldPlanStatus {

    Normal("正常", 1),
    Occupy("占用", 4),
    Predetermine("预定", 6),
    Invalid("失效", 8),;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态码
     */
    @EnumValue
    private Integer code;

    public static FieldPlanStatus of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
