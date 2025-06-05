package com.aioveu.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.system.model.entity.SysRoleMenu;

import java.util.List;

/**
 * @Description: TODO 角色菜单业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 17:30
 * @param
 * @return:
 **/

public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 获取角色拥有的菜单ID集合
     *
     * @param roleId
     * @return
     */
    List<Long> listMenuIdsByRoleId(Long roleId);


    /**
     * 刷新权限缓存(所有角色)
     */
    void refreshRolePermsCache();

    /**
     * 刷新权限缓存(指定角色)
     *
     * @param roleCode 角色编码
     */
    void refreshRolePermsCache(String roleCode);

    /**
     * 刷新权限缓存(修改角色编码时调用)
     *
     * @param oldRoleCode 旧角色编码
     * @param newRoleCode 新角色编码
     */
    void refreshRolePermsCache(String oldRoleCode, String newRoleCode);

}
