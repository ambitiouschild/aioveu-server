package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserWechatId;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserWechatIdService extends IService<UserWechatId> {

    /**
     * 通过微信id查找用户id
     * @param wechatId
     * @return
     */
    String getUserIdByWechatId(String wechatId);

    /**
     * 绑定微信号和手机号码
     * @param wechatId
     * @param phone
     * @param verificationCode
     * @return
     */
    String bindWechatId(String wechatId, String phone, String verificationCode);

    /**
     * 用户id查找微信绑定信息
     * @param userId
     * @return
     */
    UserWechatId getByUserId(String userId);



}
