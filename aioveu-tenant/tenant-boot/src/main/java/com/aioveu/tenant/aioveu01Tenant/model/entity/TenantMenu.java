package com.aioveu.tenant.aioveu01Tenant.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: TenantMenu
 * @Description TODO 租户菜单关联表
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:24
 * @Version 1.0
 **/
@TableName("sys_tenant_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantMenu {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
