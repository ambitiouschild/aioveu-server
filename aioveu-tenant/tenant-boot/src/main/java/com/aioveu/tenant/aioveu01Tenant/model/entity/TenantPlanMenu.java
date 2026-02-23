package com.aioveu.tenant.aioveu01Tenant.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: TenantPlanMenu
 * @Description TODO 租户套餐菜单关联实体
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:25
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_tenant_plan_menu")
public class TenantPlanMenu {

    /**
     * 套餐ID
     */
    private Long planId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
