package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>审批状态</h1>
 * @author: yeshb
 */
@Getter
@AllArgsConstructor
public enum CountExerciseStatus {

    Audit("审核中", 2),
    PASS("通过", 1),
    FAIL("审核不通过", 4);

    /** 描述 */
    private String description;

    /** 状态码 */
    @EnumValue
    private Integer code;

    public static CountExerciseStatus of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }

    @Override
    public String toString() {
        return this.getCode().toString();
    }
}
