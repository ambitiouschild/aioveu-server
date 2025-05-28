package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>增值服务</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum ChargingOptionEnum {

    FIELD_SYNC("订场同步", "FIELD_SYNC"),
    AI_ASSISTANT("智能助理", "AI_ASSISTANT"),
    MP_MSG("服务号消息", "MP_MSG"),
    SYSTEM_OPERATE("系统经营消息", "SYSTEM_OPERATE"),
    MINI_APP_MSG("小程序消息", "MINI_APP_MSG"),
    SMS("短信", "SMS");

    private String description;

    @EnumValue
    private String code;

    public static ChargingOptionEnum of(String code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
