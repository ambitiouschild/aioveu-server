package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.RoleUser;
import com.aioveu.vo.UserRoleVo;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/3 0003 18:33
 */
public interface RoleUserService extends IService<RoleUser> {

    /**
     * 根据用户id获取对应的角色列表
     * @param userId
     * @return
     */
    List<String> getByUserId(String userId);
    /**
     * 根据用户id获取对应的角色列表
     * @param userId
     * @return
     */
    List<String> getByUserId(String userId, Long storeId);

    /**
     * 查询 教师 销售 角色
     * @param userId
     * @param roleCodes
     * @return
     */
    boolean save(String userId, List<String> roleCodes);

    /**
     * 保存角色，关联门店和公司
     * @param userId
     * @param roleCodes
     * @param storeId
     * @param companyId
     * @return
     */
    boolean save(String userId, List<String> roleCodes, Long storeId, Long companyId);

    /**
     * 根据用户id删除数据
     * @param userId
     * @return
     */
    boolean delByUserId(String userId);

    /**
     * 根据用户和店铺id删除角色
     * @param userId
     * @param storeId
     * @return
     */
    boolean delByUserIdAndStoreId(String userId, Long storeId);

    /**
     * 通过角色编号查找用户编号信息
     * @param roleCode
     * @return
     */
    List<RoleUser> getByRoleCode(String roleCode);

    /**
     * 查询用户角色
     * @param userId
     * @return
     */
    List<UserRoleVo> getUserRole(String userId);

    /**
     * 添加用户角色
     * @param roleUser
     * @return
     */
    boolean saveUserRole(RoleUser roleUser);

}
