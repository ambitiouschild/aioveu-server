package com.aioveu.tenant.aioveu04Menu.mapper;

import com.aioveu.tenant.aioveu04Menu.model.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: MenuMapper
 * @Description TODO 菜单访问层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:26
 * @Version 1.0
 **/

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取菜单路由列表
     *
     * @param roleCodes 角色编码集合
     */
    List<Menu> getMenusByRoleCodes(@Param("roleCodes") Set<String> roleCodes);

    /**
     * 获取菜单路由列表（角色菜单 ∩ 租户可用菜单）
     *
     * @param roleCodes 角色编码集合
     * @param roleTenantId 角色归属租户ID
     * @param tenantId 目标租户ID
     */
    List<Menu> getMenusByRoleCodesAndTenant(
            @Param("roleCodes") Set<String> roleCodes,
            @Param("roleTenantId") Long roleTenantId,
            @Param("tenantId") Long tenantId
    );
}
