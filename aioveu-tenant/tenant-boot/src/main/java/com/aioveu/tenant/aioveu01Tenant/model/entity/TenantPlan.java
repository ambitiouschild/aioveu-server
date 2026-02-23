package com.aioveu.tenant.aioveu01Tenant.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName: TenantPlan
 * @Description TODO 租户套餐实体
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:24
 * @Version 1.0
 **/
@Data
@TableName("sys_tenant_plan")
public class TenantPlan extends BaseEntity {

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 套餐编码
     */
    private String code;

    /**
     * 状态(1-启用 0-停用)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;
}
