package com.aioveu.common.enums;

import com.aioveu.common.base.IBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
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
    @Getter
    private Integer value;

    @Getter
    private String label;

    StatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
