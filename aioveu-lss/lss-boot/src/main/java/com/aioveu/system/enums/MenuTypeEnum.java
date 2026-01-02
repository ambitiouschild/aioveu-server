package com.aioveu.system.enums;

import com.aioveu.common.base.IBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @ClassName: MenuTypeEnum
 * @Description TODO  菜单类型枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:30
 * @Version 1.0
 **/

@Getter
public enum MenuTypeEnum implements IBaseEnum<Integer> {

    NULL(0, null),
    MENU(1, "菜单"),
    CATALOG(2, "目录"),
    EXTLINK(3, "外链"),
    BUTTON(4, "按钮");

    //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @EnumValue
    private final Integer value;

    // @JsonValue //  表示对枚举序列化时返回此字段
    private final String label;

    MenuTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
