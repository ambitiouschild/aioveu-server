package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserOpenId;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserOpenIdService extends IService<UserOpenId> {

    /**
     * 创建
     * @param userId
     * @param appId
     * @param openId
     * @param unionId
     * @return
     */
    Boolean addOrUpdate(String userId, String appId, String openId, String unionId);

    /**
     * 通过userId和appid获取对应的openId
     * @param userId
     * @param appId
     * @param require
     * @return
     */
    String getByAppIdAndUserId(String userId, String appId, boolean require);

    /**
     * 获取店铺的公众号对应的用户的openId
     * @param storeId
     * @param userId
     * @param subscribe
     * @return
     */
    String getMpOpenIdByStoreIdAndUserId(Long storeId, String userId, boolean subscribe);

}
