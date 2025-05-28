package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.CompanyStoreUser;
import com.aioveu.vo.CompanyStoreUserVo;
import com.aioveu.vo.StoreSimpleVO;
import com.aioveu.vo.UserPhoneVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface CompanyStoreUserDao extends BaseMapper<CompanyStoreUser> {

    /**
     * 根据用户id获取店铺列表
     * @param userId
     * @return
     */
    List<StoreSimpleVO> getStoreByUserId(String userId);

    /**
     * 查询店铺的用户
     * @param storeId
     * @return
     */
    String getStoreAdminUser(Long storeId);

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

}
