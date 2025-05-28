package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Role;
import com.aioveu.vo.IdNameCodeVO;
import com.aioveu.vo.RoleDetailVO;
import com.aioveu.vo.WebRoleVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface RoleService extends IService<Role> {

    Role getRoleByCode(String code);

    /**
     * 获取所有角色
     * @return
     */
    List<IdNameCodeVO> getAll();

    /**
     * 根据门店id，获取第三方公司创建的角色
     * @param storeId
     * @return
     */
    List<IdNameCodeVO> manageList(String storeId);

    /**
     * 获取商户端用户创建的角色，和内置角色
     * @param storeId
     * @return
     */
    List<Role> getBusinessRoleList(String storeId);

    /**
     * 保存或者更新角色对应的菜单权限
     * @param roleVO
     * @return
     */
    boolean saveOrUpdateRole(RoleDetailVO roleVO);

    RoleDetailVO getDetail(Long id);

    /**
     * web端角色列表
     * @param page
     * @param size
     * @param storeId
     * @param companyId
     * @param type
     * @return
     */
    IPage<WebRoleVO> webList(Integer page, Integer size, Long storeId, Long companyId, Integer type);

    /**
     * 根据code删除角色
     * @param code
     * @return
     */
    boolean deleteByCode(String code);

}
