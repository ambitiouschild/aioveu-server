package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>延迟消息类型</h1>
 * @author: 雒世松
 */
@Getter
@AllArgsConstructor
public enum DelayMessageType {

    GRADE_MINI_CHECK("班级最小人数检查", 0),
    GRADE_FINISH("班级完成", 8),
    FIELD_FINISH("订场完成", 9),
    UNPAY_CANCEL("订场未支付订单取消", 10),
    SELECT("单选", 1),
    MULTI_SELECT("多选", 2),
    JOIN_EXERCISE_FINISH("拼单结算", 11),
    COUNT_EXERCISE_FINISH("按次订单自动完成", 20),
    MULTI_INPUT("多行输入", 3);

    /** 延迟消息描述*/
    private String description;

    /** 延迟消息编码 */
    @EnumValue
    private Integer code;

    public static DelayMessageType of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
