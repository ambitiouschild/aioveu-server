package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>优惠券分类</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum QuestionType {

    INPUT("输入", 0),
    SELECT("单选", 1),
    MULTI_SELECT("多选", 2),
    MULTI_INPUT("多行输入", 3);

    /** 优惠券描述(分类) */
    private String description;

    /** 优惠券分类编码 */
    // 标记数据库存的值是code
    @EnumValue
    private Integer code;

    public static QuestionType of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
