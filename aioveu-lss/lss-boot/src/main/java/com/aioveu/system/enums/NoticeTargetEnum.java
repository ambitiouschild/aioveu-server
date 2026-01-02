package com.aioveu.system.enums;

import com.aioveu.common.base.IBaseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @ClassName: NoticeTargetEnum
 * @Description TODO 通知目标类型枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:31
 * @Version 1.0
 **/

@Getter
@Schema(enumAsRef = true)
public enum NoticeTargetEnum implements IBaseEnum<Integer> {

    ALL(1, "全体"),
    SPECIFIED(2, "指定");


    private final Integer value;

    private final String label;

    NoticeTargetEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
