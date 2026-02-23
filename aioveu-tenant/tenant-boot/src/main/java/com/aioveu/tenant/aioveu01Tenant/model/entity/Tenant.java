package com.aioveu.tenant.aioveu01Tenant.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: Tenant
 * @Description TODO 租户实体
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:23
 * @Version 1.0
 **/
@Data
@TableName(value = "sys_tenant")
public class Tenant extends BaseEntity {

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户编码（唯一）
     */
    private String code;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 租户域名（用于域名识别）
     */
    private String domain;

    /**
     * 租户Logo
     */
    private String logo;

    /**
     * 套餐ID
     */
    @TableField(value = "plan_id")
    private Long planId;

    /**
     * 状态(1-正常 0-禁用)
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 过期时间（NULL表示永不过期）
     */
    private LocalDateTime expireTime;
}
