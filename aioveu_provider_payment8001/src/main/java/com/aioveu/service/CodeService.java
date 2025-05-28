package com.aioveu.service;

import com.aioveu.vo.CheckOrderVO;
import com.aioveu.vo.FileVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/6 17:39
 */
public interface CodeService {

    /**
     * 核销消费码
     * @param userId
     * @param code
     * @param businessCheck
     * @return
     */
    CheckOrderVO checkOrderCode(String userId, String code, boolean businessCheck);


    /**
     * 核销 核销码
     * @param userId
     * @param code
     * @return
     */
    CheckOrderVO checkCode(String userId, String code);

    /**
     * 核销 优惠券码
     * @param userId
     * @param code
     * @param businessCheck
     * @return
     */
    CheckOrderVO checkCouponCode(String userId, String code, boolean businessCheck);

    @Transactional(rollbackFor = Exception.class)
    CheckOrderVO checkFieldOrderCode(String userId, String code, boolean businessCheck);

    /**
     * 获取小程序页面二维码
     * @param id
     * @return
     */
    String exercisePageCode(Long id);

    /**
     * 问卷详情页二维码
     * @param id
     * @return
     */
    String formEnrollPageCode(Long id);

    /**
     * 用户订单详情自己核销
     * @param orderId
     * @param username
     * @return
     */
    Boolean checkCodeMine(String orderId, String username);

    /**
     * 帮激活订单
     * @param orderId
     * @return
     */
    Boolean helpActiveOrder(String orderId);

    /**
     * 获取灵活课包小程序页面二维码
     * @param id
     * @return
     */
    FileVO createCustomExercisePageCode(Long id);

    /**
     * 获取小程序页面二维码
     * @param scene scene 场景值
     * @param key redisKey
     * @param page 小程序页面路径
     * @param fileName 文件名
     * @param appId
     * @param needCache 是否需要缓存
     * @return
     */
    String miniPageCode(String scene, String key, String page, String fileName, String appId, boolean needCache);

    /**
     * 获取主题二维码
     * @param id
     * @return
     */
    String topicPageCode(Long id);

    /**
     * 获取店铺到店二维码
     * @param id
     * @return
     */
    String toStoreCode(Long id);

    /**
     * 关注公众号绑定用户二维码
     * @param userId
     * @param appId
     * @return
     */
    String bindMpQrcode(String userId, String appId);

}
