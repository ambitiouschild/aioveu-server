package com.aioveu.system.aioveu03Role.mapper;

import com.aioveu.system.aioveu03Role.model.entity.RoleMenu;
import com.aioveu.system.aioveu03Role.model.vo.RolePermsBO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: RoleMenuMapper
 * @Description TODO  角色菜单访问层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:02
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
     * @param roles
     * @return
     */
    Set<String> listRolePerms(Set<String> roles);
}
