package com.aioveu.auth.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 权限实体类
 */
@Data
@TableName("sport_permission")
public class SysPermission {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotEmpty(message = "URL不能为空")
    private String url;

    private String method;

    private Boolean needToken;

    private Integer status;

    private Date createDate;

    private Date updateDate;
}
