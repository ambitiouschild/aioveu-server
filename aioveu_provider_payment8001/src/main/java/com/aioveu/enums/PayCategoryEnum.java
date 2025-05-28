package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>订单支付分类</h1>
 * @author: 雒世松12
 */
@Getter
@AllArgsConstructor
public enum PayCategoryEnum {

    VIP("会员卡储值", "vipPay"),
    FIELD("订场服务", "fieldOrder"),
    PRODUCT_ORDER("签单", "productOrder"),
    RECHARGE("现金充值", "rechargePay"),
    STORE_RECHARGE("商户充值", "storeRecharge"),
    EXERCISE("课程服务", "exercisePay");

    private String description;

    @EnumValue
    private String code;

    public static PayCategoryEnum of(String code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
