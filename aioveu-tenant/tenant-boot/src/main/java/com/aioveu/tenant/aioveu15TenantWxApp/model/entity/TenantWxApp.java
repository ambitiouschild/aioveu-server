package com.aioveu.tenant.aioveu15TenantWxApp.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: TenantWxApp
 * @Description TODO 租户与微信小程序关联实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 17:01
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("sys_tenant_wx_app")
public class TenantWxApp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 微信小程序ID
     */
    private String wxAppid;

    /**
     * 微信小程序appname
     */
    private String wxAppname;

    /**
     * 微信小程序注册邮箱
     */
    private String registeredEmail;
    /**
     * 是否为默认小程序
     */
    private Integer isDefault;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
