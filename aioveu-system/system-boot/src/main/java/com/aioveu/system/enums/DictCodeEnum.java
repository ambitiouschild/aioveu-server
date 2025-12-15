package com.aioveu.system.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @ClassName: DictCodeEnum
 * @Description TODO  字典编码枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/15 16:11
 * @Version 1.0
 **/

@Getter
public enum DictCodeEnum implements IBaseEnum<String> {

    GENDER("gender", "性别"),
    NOTICE_TYPE("notice_type", "通知类型"),
    NOTICE_LEVEL("notice_level", "通知级别");

    private final String value;

    private final String label;

    DictCodeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
