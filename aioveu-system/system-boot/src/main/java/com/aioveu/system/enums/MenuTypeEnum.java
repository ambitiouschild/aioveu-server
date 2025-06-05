package com.aioveu.system.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @Description: TODO 菜单类型枚举
 * @Author: 雒世松
 * @Date: 2025/6/5 17:13
 * @param
 * @return:
 **/


public enum MenuTypeEnum implements IBaseEnum<Integer> {

    NULL(0, null),
    MENU(1, "菜单"),
    CATALOG(2, "目录"),
    EXTLINK(3, "外链"),

    BUTTON(4, "按钮");

    @Getter
    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    private Integer value;

    @Getter
    // @JsonValue //  表示对枚举序列化时返回此字段
    private String label;

    MenuTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }


}
