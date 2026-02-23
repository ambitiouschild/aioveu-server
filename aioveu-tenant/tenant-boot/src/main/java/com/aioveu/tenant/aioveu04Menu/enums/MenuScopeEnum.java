package com.aioveu.tenant.aioveu04Menu.enums;

import com.aioveu.common.base.IBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @ClassName: MenuScopeEnum
 * @Description TODO 菜单范围枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:04
 * @Version 1.0
 **/

@Getter
public enum MenuScopeEnum implements IBaseEnum<Integer> {

    PLATFORM(1, "平台菜单"),
    TENANT(2, "业务菜单");

    @EnumValue
    private final Integer value;

    private final String label;

    MenuScopeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
