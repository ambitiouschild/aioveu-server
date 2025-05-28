package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Permission;
import com.aioveu.vo.RolePermissionVO;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface PermissionService extends IService<Permission> {

    /**
     * id查找角色权限
     * @param id
     * @return
     */
    RolePermissionVO getRolePermissionById(Long id);

    /**
     * 创建URL权限
     * @param permission
     * @return
     */
    Long create(Permission permission);

    /**
     * web端权限列表
     * @param page
     * @param size
     * @param keyword
     * @param needToken
     * @return
     */
    IPage<Permission> getList(Integer page, Integer size, String keyword, Boolean needToken);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 更新
     * @param permission
     * @return
     */
    boolean updatePermission(Permission permission);

}
