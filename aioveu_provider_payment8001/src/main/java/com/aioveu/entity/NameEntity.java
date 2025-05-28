package com.aioveu.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@Data
public abstract class NameEntity extends BaseEntity{

    @NotEmpty(message = "名称不能为空")
    private String name;

}
