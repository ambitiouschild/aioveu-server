package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum FieldPlanTimeType {

    FREE_DAY("单次", 0),
    WEEK_DAY("每星期", 1),
    MONTH_DAY("每月", 2),
    EVERY_DAY("每天", 3);

    /** 描述 */
    private String description;

    /** 编码 */
    @EnumValue
    private Integer code;

    public static FieldPlanTimeType of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
