package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description 系统配置
 * @author: 雒世松
 * @date: Created in 2025/02/19 11:41
 */
@Data
@TableName("sport_sys_config")
public class SysConfig extends IdNameEntity {

    @NotEmpty(message = "配置编号不能为空")
    private String code;

    @NotEmpty(message = "配置值不能为空")
    private String value;

    private String categoryCode;

    private String roleCode;

    private String defaultValue;

    private String fieldType;

    private String configDesc;

}
