package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.UserOpenId;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface UserOpenIdDao extends BaseMapper<UserOpenId> {

    /**
     * 通过openId查找手机号
     * @param openId
     * @return
     */
    String getPhoneByOpenId(String openId);

    /**
     * 获取店铺对应公众号的用户openId
     * @param storeId
     * @param userId
     * @return
     */
    String getStoreMpUserOpenId(Long storeId, String userId);

}
