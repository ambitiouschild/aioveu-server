package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description 角色表
 * @author: 雒世松
 * @date: 2025/2/3 0003 14:37
 */
@Data
@TableName("sport_role")
public class Role extends IdNameEntity {

    @NotBlank(message = "code can not be null!")
    private String code;

    /**
     * 角色类型，1 内置角色 2 系统角色 3 第三方角色
     */
    private Integer type;

    private Long storeId;

    private Long companyId;
}
