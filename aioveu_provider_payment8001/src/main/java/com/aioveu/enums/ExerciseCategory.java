package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>活动分类</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum ExerciseCategory {

    EXERCISE("活动", "activity"), // 1000L
    COURSE("课程服务", "course"), // 1001L
    QUESTION("问卷", "form"), // 1010L
    CUSTOM("灵活课包", "custom_course"),
    EXPERIENCE("体验课", "experience_class"),
    OFFLINE_PUSH("地推", "push_exercise"),
    COUPON("优惠活动", "coupon_exercise"),
    JOIN_EXERCISE("拼单", "join_exercise"),
    COUNT("按次活动", "count_exercise"),
    VIP_CARD("会员卡", "vip_card");

    private String description;

    @EnumValue
    private String code;

    public static ExerciseCategory of(String code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
