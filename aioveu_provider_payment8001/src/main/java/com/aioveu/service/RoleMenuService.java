package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.RoleMenu;
import com.aioveu.entity.RoleMiniAppMenu;
import com.aioveu.vo.RoleMenuVO;

import java.util.List;
import java.util.Map;

/**
 * @Author： yao
 * @Date： 2024/10/28 14:28
 * @Describe：
 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 根据门店id，角色code，获取对应的菜单
     * @param roleCode
     * @return
     */
    List<RoleMenu> getListByRoleCode(String roleCode);

    /**
     * 通过角色获取菜单
     * @param roleList
     * @return
     */
    List<RoleMiniAppMenu> getRoleMenus(List<String> roleList);

    /**
     * 根据角色编号删除菜单
     * @param roleCode
     * @return
     */
    boolean deleteByRoleCode(String roleCode);

    /**
     * 通过菜单查找对应角色
     * @param menuCode
     * @return
     */
    List<RoleMenu> getByMenuCode(String menuCode);

    /**
     * 通过角色编号和父级菜单编号获取菜单
     * @param roleCode
     * @param type
     * @return
     */
    Map<String, Object> getRoleMenusByRoleCodeAndType(String roleCode, Integer type);

    /**
     * 获取小程序角色管理编辑的菜单
     * @param roleCode
     * @return
     */
    List<RoleMenuVO> getMiniAppMenuByRoleCode(String roleCode);

}
