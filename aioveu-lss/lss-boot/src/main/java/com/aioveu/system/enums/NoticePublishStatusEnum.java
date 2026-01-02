package com.aioveu.system.enums;

import com.aioveu.common.base.IBaseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @ClassName: NoticePublishStatusEnum
 * @Description TODO  通告发布状态枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:31
 * @Version 1.0
 **/

@Getter
@Schema(enumAsRef = true)
public enum NoticePublishStatusEnum implements IBaseEnum<Integer> {

    UNPUBLISHED(0, "未发布"),
    PUBLISHED(1, "已发布"),
    REVOKED(-1, "已撤回");


    private final Integer value;

    private final String label;

    NoticePublishStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
