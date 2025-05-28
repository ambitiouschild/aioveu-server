package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Author： yao
 * @Date： 2025/1/8 14:01
 * @Describe：
 */
@Getter
@AllArgsConstructor
public enum CheckRecordStatus {
    Deleted("已删除", 0),
    Uncheck("待核销", 1),
    Checked("已核销", 2),
    CheckFail("核销失败", 3),
    Overdue("已过期", 4),
    IPFail("动态IP获取失败, 请重试!", 5);

    /** 描述 */
    private String description;

    /** 状态码 */
    @EnumValue
    private Integer code;

    public static CheckRecordStatus of(Integer code) {

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
