package com.aioveu.registry.aioveu01RegistryTenant.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RegistryTenant
 * @Description TODO 租户注册小程序基本信息实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 16:28
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("registry_tenant")
public class RegistryTenant extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 租户唯一编码
     */
    private String tenantCode;
    /**
     * 主体类型：1-企业，2-个体工商户，3-政府/媒体，4-其他组织，5-个人
     */
    private String tenantType;
    /**
     * 行业类别/小程序类目
     */
    private String businessType;
    /**
     * 注册国家/地区
     */
    private String countryRegion;
    /**
     * 租户注册状态：0-未注册，1-已注册，2-已认证，3-已备案，4-已禁用
     */
    private Integer tenantRegistryStatus;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
