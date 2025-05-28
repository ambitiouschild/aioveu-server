package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 消息类型枚举
 * @Author： yao
 * @Date： 2025/4/29 11:40
 * @Describe：
 */
@Getter
@AllArgsConstructor
public enum NoticeCodeEnum {
    wechat_mp("公众号消息", "wechat_mp"),
    sms("短信", "sms"),
    wechat_mini_app("小程序消息", "wechat_mini_app"),
    system("系统消息", "system");

    /** 描述 */
    private String description;

    /** 状态码 */
    @EnumValue
    private String code;

    public static NoticeCodeEnum of(String msgCode) {
        Objects.requireNonNull(msgCode);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(msgCode))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(msgCode + " not exists!"));
    }
}
