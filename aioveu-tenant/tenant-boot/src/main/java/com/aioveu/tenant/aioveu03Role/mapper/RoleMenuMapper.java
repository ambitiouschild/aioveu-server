package com.aioveu.tenant.aioveu03Role.mapper;

import com.aioveu.tenant.aioveu03Role.model.bo.RolePermsBO;
import com.aioveu.tenant.aioveu03Role.model.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: RoleMenuMapper
 * @Description TODO 角色菜单访问层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:41
 * @Version 1.0
 **/

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 获取角色拥有的菜单ID集合
     *
     * @param roleId 角色ID
     * @return 菜单ID集合
     */
    List<Long> listMenuIdsByRoleId(Long roleId);

    /**
     * 获取权限和拥有权限的角色列表
     */
    List<RolePermsBO> getRolePermsList(String roleCode);


    /**
     * 获取角色权限集合
     *
     * @param roleCodes
     * @return
     */
    Set<String> listRolePerms(Set<String> roleCodes);


    /*
    *  ✅ 你 Service 里已经在用 Map<String, Object> params
        ✅ Map 版本更灵活（buttonType、未来扩展）
        ✅ 认证链路 = 多条件 = Map 更合适
    * */
    List<String> listRolePermsWithTenantId(
            @Param("params") Map<String, Object> params
    );


    /**
     * 按角色分组查询按钮权限（认证链路专用）
     *
     * @param params roleCodes / tenantId / buttonType
     * @return key = roleCode, value = 权限集合
     */
    @MapKey("roleCode")
    Map<String, Set<String>> listRolePermsGroupByRoleWithTenantId(
            @Param("params") Map<String, Object> params
    );


}
