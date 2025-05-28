package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Role;
import com.aioveu.vo.WebRoleVO;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface RoleDao extends BaseMapper<Role> {

    /**
     * web端角色列表
     * @param page
     * @param storeId
     * @param companyId
     * @param type
     * @return
     */
    IPage<WebRoleVO> getWebList(IPage<WebRoleVO> page, Long storeId, Long companyId, Integer type);

}
