package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 消息类型
 * @description
 */
@Getter
@AllArgsConstructor
public enum MessageType{

    SystemMessage("系统消息", 1),
    OperateMessage("经营提醒", 2);

    /** 描述 */
    private String description;

    /** 消息码 */
    @EnumValue
    private Integer code;

    public static MessageType of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }

    @Override
    public String toString() {
        return this.getCode().toString();
    }

    // status 1 未读 2 已读

}
