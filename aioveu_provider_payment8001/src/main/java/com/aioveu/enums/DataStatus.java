package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>数据状态</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum DataStatus {

    DELETE("已删除", 0),
    NORMAL("正常", 1),
    LOW("下架",2);

    /** 描述 */
    private String description;

    /** 状态码 */
    @EnumValue
    private Integer code;

    public static DataStatus of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
