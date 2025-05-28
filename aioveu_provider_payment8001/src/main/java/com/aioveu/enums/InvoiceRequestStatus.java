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
public enum InvoiceRequestStatus {

    Submit("开票中", 1),
    Approved("已开票", 2),
    Reject("开票失败", 3);

    /** 描述 */
    private String description;

    /** 状态码 */
    @EnumValue
    private Integer code;

    public static InvoiceRequestStatus of(Integer code) {

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
