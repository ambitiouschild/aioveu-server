package com.aioveu.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ChangeTypeStatus {
    Income("收入", 0),
    Withdrawal("提现", 1),
    Recharge("充值", 2),
    Payment("支出", 3),
    Refund("退款", 4);

    /** 描述 */
    private String description;

    /** 状态码 */
    @EnumValue
    private Integer code;

    public static ChangeTypeStatus of(Integer code) {

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
