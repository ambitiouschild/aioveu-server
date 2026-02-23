package com.aioveu.tenant.aioveu04Menu.enums;

import com.aioveu.common.base.IBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @ClassName: MenuTypeEnum
 * @Description TODO   * 菜单类型枚举（char）
 *                     * C：目录
 *                     * M：菜单
 *                     * B：按钮
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:05
 * @Version 1.0
 **/
@Getter
public enum MenuTypeEnum implements IBaseEnum<String> {

    CATALOG("C", "目录"),
    MENU("M", "菜单"),
    BUTTON("B", "按钮");

    /**
     * 数据库存储值
     */
    @EnumValue
    private final String value;

    /**
     * 友好名称
     */
    private final String label;

    MenuTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
