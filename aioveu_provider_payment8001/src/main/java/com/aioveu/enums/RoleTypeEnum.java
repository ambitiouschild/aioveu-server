package com.aioveu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 角色类型枚举
 * @Author： yao
 * @Date： 2024/10/28 14:56
 * @Describe：
 */
@Getter
@AllArgsConstructor
public enum RoleTypeEnum {
    /**
     * 默认商户通用的角色，可直接使用，但不可编辑
     * 管理员、销售、教练、校长为系统角色
     */
    INTERNAL_ROLE("内置角色", 1),
    /**
     * 自己系统内部用的，或者商户看不到的
     */
    SYSTEM_ROLE("系统角色", 2),
    /**
     * 表示公司或商铺自己建的角色
     */
    THIRD_ROLE("第三方角色", 3);

    /** 描述 */
    private String description;

    /** 编码 */
    private Integer code;

    public static RoleTypeEnum of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
