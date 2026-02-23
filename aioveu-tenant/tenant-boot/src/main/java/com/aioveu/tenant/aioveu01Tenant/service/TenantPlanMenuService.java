package com.aioveu.tenant.aioveu01Tenant.service;

import com.aioveu.tenant.aioveu01Tenant.model.entity.TenantPlanMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: TenantPlanMenuService
 * @Description TODO 租户套餐菜单业务接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 22:28
 * @Version 1.0
 **/
public interface TenantPlanMenuService extends IService<TenantPlanMenu> {

    /**
     * 获取套餐菜单ID集合
     *
     * @param planId 套餐ID
     * @return 菜单ID集合
     */
    List<Long> listMenuIdsByPlan(Long planId);

    /**
     * 保存套餐菜单配置
     *
     * @param planId 套餐ID
     * @param menuIds 菜单ID集合
     */
    void savePlanMenus(Long planId, List<Long> menuIds);
}
