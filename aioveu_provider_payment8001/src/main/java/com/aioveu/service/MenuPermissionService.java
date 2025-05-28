package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.MenuPermission;
import com.aioveu.vo.MenuPermissionVo;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface MenuPermissionService extends IService<MenuPermission> {

    /**
     * 根据菜单编号删除权限关联表
     * @param menuCode
     * @return
     */
    boolean deleteByRoleCode(String menuCode);

    /**
     * 根据权限id查找菜单权限关系
     * @param permissionId
     * @return
     */
    List<MenuPermission> findByPermissionId(Long permissionId);

    /**
     * 通过菜单编号查询对应权限列表
     * @param page
     * @param size
     * @param menuCode
     * @return
     */
    IPage<MenuPermissionVo> getByMenuCode(Integer page, Integer size, String menuCode);

    /**
     * 批量添加菜单权限
     * @param menuPermissions
     * @return
     */
    boolean addBatch(List<MenuPermission> menuPermissions);

    /**
     * id删除
     * @param id
     * @return
     */
    boolean deleteById(Long id);


}
