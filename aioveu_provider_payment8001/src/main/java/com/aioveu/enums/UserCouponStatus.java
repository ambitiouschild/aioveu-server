package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>用户优惠券的状态</h1>
 * Created by Qinyi.
 */
@Getter
@AllArgsConstructor
public enum UserCouponStatus {

    DELETE("删除的", 0),
    USABLE("可用的", 1),
    UN_ACTIVE("待激活", 2),
    USED("已使用的", 3),
    EXPIRED("过期的(未被使用的)", 4),
    TEMP_USED("临时使用(针对提前约课场景，临时先占用)", 8),
    CONVERT("转换(转换成其他券)", 12),
    FREEZE("冻结", 5);

    /** 优惠券状态描述信息 */
    private String description;

    /** 优惠券状态编码 */
    @EnumValue
    private Integer code;

    /**
     * <h2>根据 code 获取到 CouponStatus</h2>
     * */
    public static UserCouponStatus of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(code + " not exists")
                );
    }
}
