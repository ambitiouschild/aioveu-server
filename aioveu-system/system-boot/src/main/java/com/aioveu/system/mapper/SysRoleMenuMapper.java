package com.aioveu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.system.model.bo.RolePermsBO;
import com.aioveu.system.model.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description: TODO 角色菜单持久层
 * @Author: 雒世松
 * @Date: 2025/6/5 17:16
 * @param
 * @return:
 **/

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 获取角色拥有的菜单ID集合
     *
     * @param roleId
     * @return
     */
    List<Long> listMenuIdsByRoleId(Long roleId);

    /**
     * 获取权限和拥有权限的角色列表
     */
    List<RolePermsBO> getRolePermsList(String roleCode);
}
