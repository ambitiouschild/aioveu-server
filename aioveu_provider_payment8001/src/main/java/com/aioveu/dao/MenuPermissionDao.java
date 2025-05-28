package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.MenuPermission;
import com.aioveu.entity.Permission;
import com.aioveu.vo.MenuPermissionVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface MenuPermissionDao extends BaseMapper<MenuPermission> {

    /**
     * 通过菜单编号查询对应权限列表
     * @param page
     * @param menuCode
     * @return
     */
    IPage<MenuPermissionVo> getByMenuCode(IPage<Permission> page, String menuCode);

    /**
     * 通过菜单编号和权限id查找菜单权限
     * @param menuPermissions 
     * @return
     */
    List<MenuPermission> getByMenuAndPermission(List<MenuPermission> menuPermissions);


}
