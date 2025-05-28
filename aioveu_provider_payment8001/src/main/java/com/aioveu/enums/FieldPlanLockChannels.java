package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Author： 锁场平台枚举
 * @Date： 2025/1/4 09:55
 * @Describe：
 */
@Getter
@AllArgsConstructor
public enum FieldPlanLockChannels {

    /**
     * code对应sport_category分类表id值
     */
    Current("趣数体育", "QS"),
    QYD("趣运动", "QYD"),
    DP("美团点评", "DP"),
    LHD("来沪动", "LHD");

    /**
     * 描述
     */
    private String description;

    /**
     * 状态码
     */
    @EnumValue
    private String code;

    public static FieldPlanLockChannels of(String code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
