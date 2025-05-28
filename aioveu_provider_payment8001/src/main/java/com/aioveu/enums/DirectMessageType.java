package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>直连消息类型</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum DirectMessageType {

    FIELD_PLAN_TEMPLATE_CREATE("订场模版创建", 0);

    /** 消息描述*/
    private String description;

    /** 消息编码 */
    @EnumValue
    private Integer code;

    public static DirectMessageType of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
