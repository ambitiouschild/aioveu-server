package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.Permission;
import com.aioveu.vo.RolePermissionVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface PermissionDao extends BaseMapper<Permission> {

    /**
     * id查找角色权限对象
     * @param id
     * @return
     */
    RolePermissionVO getById(Long id);

    /**
     * 通过菜单权限id查找对应角色权限列表
     * @param idList
     * @return
     */
    List<RolePermissionVO> getByMenuPermissionIdList(List<Long> idList);

    /**
     * 通过角色菜单id查找对应角色权限列表
     * @param idList
     * @return
     */
    List<RolePermissionVO> getByRoleMenuIdList(List<Long> idList);


}
