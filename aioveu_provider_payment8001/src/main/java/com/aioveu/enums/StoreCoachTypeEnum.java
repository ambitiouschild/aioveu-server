package com.aioveu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 门店销售、教练类型枚举类
 * @Author： yao
 * @Date： 2024/10/28 13:26
 * @Describe：
 */
@Getter
@AllArgsConstructor
public enum StoreCoachTypeEnum {
    COACH_TYPE("教练", 1),
    SALE_TYPE("销售", 4);

    /** 描述 */
    private String description;

    /** 编码 */
    private Integer code;

    public static StoreCoachTypeEnum of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
