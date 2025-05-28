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
public enum IntentionStatus {

    NOT_CALL("未拨打", 0),
    NOT_CONNECT("未接通", 1),
    NO_INTENTION("无明确意向", 2),
    INTENTION("有意向", 3);

    /** 描述 */
    private String description;

    /** 状态码 */
    @EnumValue
    private Integer code;

    public static IntentionStatus of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
