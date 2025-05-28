package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.CompanyStoreUser;
import com.aioveu.vo.CompanyStoreUserVo;
import com.aioveu.vo.StoreSimpleVO;
import com.aioveu.vo.UserPhoneVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/3 0003 18:33
 */
public interface CompanyStoreUserService extends IService<CompanyStoreUser> {

    /**
     * 根据用户id获取店铺列表
     * @param userId
     * @return
     */
    List<StoreSimpleVO> getStoreByUserId(String userId);

    /**
     * 是否有店铺权限
     * @param userId
     * @param storeId
     * @param companyId
     * @return
     */
    boolean hasStorePermission(String userId, Long storeId, Long companyId);

    /**
     * 是否有公司权限
     * @param userId
     * @param companyId
     * @return
     */
    boolean hasCompanyPermission(String userId, Long companyId);


    /**
     * 批量创建店铺权限
     * @param userId
     * @param storeIds
     * @return
     */
    boolean batchCreateByStoreId(String userId, List<Long> storeIds);

    /**
     * 根据userId删除角色用户关系表数据
     * @param userId
     * @return
     */
    boolean delUserId(String userId);

    /**
     * 根据门店id，删除改用户绑定的数据
     * @param userId
     * @param storeId
     * @return
     */
    boolean delUserId(String userId, Long storeId);

    /**
     * 根据公司id删除所有该用户绑定的门店数据
     * @param userId
     * @param companyId
     * @return
     */
    boolean delUserIdByCompanyId(String userId, Long companyId);

    /**
     * 根据公司id获取所有该用户绑定的门店数据
     * @param userId
     * @param companyId
     * @return
     */
    List<CompanyStoreUser> getUserIdByCompanyId(String userId, Long companyId);

    /**
     * 获取店铺的管理员用户
     * @param storeId
     * @return
     */
     String getStoreAdminUser(Long storeId);

    /**
     * 校验用户店铺权限
     * @param userId
     * @param storeId
     * @return
     */
     boolean checkUserStore(String userId, Long storeId);

    /**
     * 根据公司id和商户id查询绑定的用户id和手机号
     * @param companyId
     * @param storeId
     * @return
     */
    List<UserPhoneVO> findPhoneAndUserId(Long companyId, Long id, Long storeId);

    /**
     * 获取用户的授权店铺
     * @param userId
     * @return
     */
    List<CompanyStoreUserVo> getListByUserId(String userId);

    /**
     * 新增用户店铺授权
     * @param companyStoreUser
     * @return
     */
    boolean create(CompanyStoreUser companyStoreUser);

}
