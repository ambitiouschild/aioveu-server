package com.aioveu.common.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @ClassName: EnvEnum
 * @Description TODO 环境枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 20:26
 * @Version 1.0
 **/

@Getter
public enum EnvEnum implements IBaseEnum<String> {

    DEV("dev", "开发环境"),
    PROD("prod", "生产环境");

    private final String value;

    private final String label;

    EnvEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
