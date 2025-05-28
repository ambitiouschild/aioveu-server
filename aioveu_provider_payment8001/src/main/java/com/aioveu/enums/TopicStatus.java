package com.aioveu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>主题状态</h1>
 * Created by 雒世松12
 */
@Getter
@AllArgsConstructor
public enum TopicStatus {

    DELETE("已删除", 0),
    REVIEWED("已审核", 1),
    UN_REVIEWED("待审核", 2);

    /** 有效期描述 */
    private String description;

    /** 有效期编码 */
    private Integer code;

    public static TopicStatus of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
