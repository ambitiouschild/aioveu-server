package com.aioveu.system.aioveu03Role.service;

import com.aioveu.common.model.Option;
import com.aioveu.system.aioveu03Role.model.entity.Role;
import com.aioveu.system.aioveu03Role.model.form.RoleForm;
import com.aioveu.system.aioveu03Role.model.query.RolePageQuery;
import com.aioveu.system.aioveu03Role.model.vo.RolePageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: RoleService
 * @Description TODO  角色业务接口层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:20
 * @Version 1.0
 **/
public interface RoleService extends IService<Role> {

    /**
     * 角色分页列表
     *
     * @param queryParams
     * @return
     */
    Page<RolePageVO> getRolePage(RolePageQuery queryParams);


    /**
     * 角色下拉列表
     *
     * @return
     */
    List<Option<Long>> listRoleOptions();

    /**
     *
     * @param roleForm
     * @return
     */
    boolean saveRole(RoleForm roleForm);

    /**
     * 获取角色表单数据
     *
     * @param roleId 角色ID
     * @return  {@link RoleForm} – 角色表单数据
     */
    RoleForm getRoleForm(Long roleId);

    /**
     * 修改角色状态
     *
     * @param roleId 角色ID
     * @param status 角色状态(1:启用；0:禁用)
     * @return {@link Boolean}
     */
    boolean updateRoleStatus(Long roleId, Integer status);

    /**
     * 批量删除角色
     *
     * @param ids 角色ID，多个使用英文逗号(,)分割
     */
    void deleteRoles(String ids);

    /**
     * 获取角色的菜单ID集合
     *
     * @param roleId 角色ID
     * @return 菜单ID集合(包括按钮权限ID)
     */
    List<Long> getRoleMenuIds(Long roleId);

    /**
     * 修改角色的资源权限
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID集合
     */
    void assignMenusToRole(Long roleId, List<Long> menuIds);

    /**
     * 获取最大范围的数据权限
     *
     * @param roles
     * @return
     */
    Integer getMaximumDataScope(Set<String> roles);
}
