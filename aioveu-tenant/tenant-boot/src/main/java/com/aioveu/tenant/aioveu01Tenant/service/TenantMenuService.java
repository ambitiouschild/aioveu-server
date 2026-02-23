package com.aioveu.tenant.aioveu01Tenant.service;

import com.aioveu.tenant.aioveu01Tenant.model.entity.TenantMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: TenantMenuService
 * @Description TODO 租户菜单业务接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 22:28
 * @Version 1.0
 **/
public interface TenantMenuService extends IService<TenantMenu> {

    /**
     * 获取租户可用菜单ID集合
     *
     * @param tenantId 租户ID
     * @return 菜单ID集合
     */
    List<Long> listMenuIdsByTenant(Long tenantId);

    /**
     * 保存租户菜单配置
     *
     * @param tenantId 租户ID
     * @param menuIds 菜单ID集合
     */
    void saveTenantMenus(Long tenantId, List<Long> menuIds);
}
