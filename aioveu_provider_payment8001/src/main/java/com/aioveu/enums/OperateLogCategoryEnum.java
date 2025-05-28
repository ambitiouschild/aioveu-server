package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>操作日志类型</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum OperateLogCategoryEnum {

    FIELD("场地操作", "FIELD_OPERATE"),
    GRADE("教务操作", "GRADE_OPERATE"),
    LOGIN("登录操作", "LOGIN_OPERATE");

    /** 描述 */
    private String description;

    /** 状态码 */
    @EnumValue
    private String code;

    public static OperateLogCategoryEnum of(String msgCode) {
        Objects.requireNonNull(msgCode);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(msgCode))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(msgCode + " not exists!"));
    }
}
