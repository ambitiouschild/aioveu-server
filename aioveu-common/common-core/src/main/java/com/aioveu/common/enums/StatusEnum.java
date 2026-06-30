package com.aioveu.common.enums;

import com.aioveu.common.base.IBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Description: TODO 状态枚举
 * @Author: 雒世松
 * @Date: 2025/6/5 15:39
 * @param
 * @return:
 **/

public enum StatusEnum {

    ENABLE(1, "启用"),
    DISABLE (0, "禁用");


    @EnumValue                      // 关键！
    @JsonValue          // ✅ 序列化用
    @Getter
    private Integer value;

    @Getter
    private String label;

    StatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @JsonCreator        // ✅ 反序列化用（关键！）
    public static StatusEnum fromText(String text) {
        if (text == null) {
            return DISABLE;
        }
        return StatusEnum.valueOf(text.toUpperCase());
    }
}
