package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.RoleMenu;
import com.aioveu.entity.RoleMiniAppMenu;
import com.aioveu.vo.RoleMenuVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author： yao
 * @Date： 2024/10/28 14:30
 * @Describe：
 */
@Repository
public interface RoleMenuDao extends BaseMapper<RoleMenu> {

    /**
     * 通过角色获取菜单
     * @param roleList
     * @return
     */
    List<RoleMiniAppMenu> getRoleMenus(List<String> roleList);

    /**
     * 根据菜单类型查询角色菜单
     * @param roleCode
     * @param type
     * @return
     */
    List<RoleMenuVO> getRoleMenusByRoleCodeAndParentCode(String roleCode, Integer type);

    /**
     * 根据角色查找对应菜单
     * @param roleList
     * @param type
     * @return
     */
    List<RoleMenuVO> getRoleMenusByRoleCode(List<String> roleList, Integer type);
}
