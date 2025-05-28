package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>审批类型</h1>
 * @author: yeshb
 */
@Getter
@AllArgsConstructor
public enum AuditType {

    Course_Editor("编辑班级", 1),
    Cancel_Course("取消课程", 2),
    Count_Exercise_Create("按次课程创建", 10);

    /** 描述 */
    private String description;

    /** 状态码 */
    @EnumValue
    private Integer code;

    public static AuditType of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
