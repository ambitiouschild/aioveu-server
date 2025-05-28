package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.RoleMenuPermission;
import com.aioveu.vo.RoleMenuPermissionVO;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface RoleMenuPermissionService extends IService<RoleMenuPermission> {

    /**
     * 根据角色对应菜单的权限
     * @param roleCode
     * @return
     */
    Map<String, List<RoleMenuPermissionVO>> getByRoleCode(String roleCode);

    /**
     * 根据角色查找权限对应Map
     * @param roleCode
     * @return
     */
    Map<Long, List<RoleMenuPermissionVO>> getPermissionMenuMap(String roleCode);

    /**
     * 通过角色code删除角色对应权限
     * @param roleCode
     * @return
     */
    boolean deleteByRoleCode(String roleCode);

    /**
     * 通过权限id删除所有角色对应的权限
     * @param permissionId
     * @return
     */
    boolean deleteByPermissionId(Long permissionId);

    /**
     * 通过菜单编号删除所有对应权限
     * @param menuCode
     * @return
     */
    boolean deleteByMenuCode(String menuCode);

    /**
     * 通过菜单编号和权限id删除
     * @param menuCode
     * @param permissionId
     * @return
     */
    boolean deleteByMenuCodeAndPermissionId(String menuCode, Long permissionId);

    /**
     * 根据id同步到缓存
     * @param ids
     * @return
     */
    boolean syncRolePermission2Cache(List<Long> ids);


}
