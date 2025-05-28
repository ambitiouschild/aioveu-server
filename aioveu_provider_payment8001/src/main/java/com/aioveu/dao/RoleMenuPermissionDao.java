package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.RoleMenuPermission;
import com.aioveu.vo.RoleMenuPermissionVO;
import com.aioveu.vo.RolePermissionVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface RoleMenuPermissionDao extends BaseMapper<RoleMenuPermission> {

    /**
     * 根据角色对应菜单的权限
     * @param roleCode
     * @return
     */
    List<RoleMenuPermissionVO> getByRoleCode(String roleCode);

    /**
     * 通过id查找角色权限
     * @param idList
     * @return
     */
    List<RolePermissionVO> getRolePermissionById(List<Long> idList);


}
