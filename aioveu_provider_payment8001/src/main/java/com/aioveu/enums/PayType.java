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
public enum PayType {

    Wechat("微信", "wx"),
    NONE("无需支付", "no"),
    MIX("混合付款，微信和会员卡都使用", "mix"),
    HF("汇付支付", "hf"),
    VIPCard("会员卡", "vip");

    /**
     * 描述
     */
    private String description;

    /**
     * 状态码
     */
    @EnumValue
    private String code;

    public static PayType of(String code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }

    @Override
    public String toString() {
        return this.getCode();
    }
}
