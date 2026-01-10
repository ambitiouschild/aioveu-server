package com.aioveu.pms.aioveu06Spu.enums;

import lombok.Getter;

/**
 * @Description: TODO 商品属性类型枚举
 * @Author: 雒世松
 * @Date: 2025/6/5 18:30
 * @param
 * @return:
 **/

public enum AttributeTypeEnum {

    SPEC(1, "规格"),
    ATTR(2, "属性");

    AttributeTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Getter
    private Integer value;

    @Getter
    private String name;

    public static AttributeTypeEnum getByValue(Integer value) {
        AttributeTypeEnum attributeTypeEnum = null;

        for (AttributeTypeEnum item : values()) {
            if (item.getValue().equals(value)) {
                attributeTypeEnum = item;
            }
        }
        return attributeTypeEnum;
    }

}
