package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 权限实体类
 */
@Data
@TableName("sport_permission")
public class Permission extends IdNameEntity {

    @NotEmpty(message = "URL不能为空")
    private String url;

    @NotEmpty(message = "请求方法不能为空")
    private String method;

    private Boolean needToken;

}
