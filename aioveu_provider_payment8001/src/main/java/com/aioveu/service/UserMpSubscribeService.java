package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserMpSubscribe;
import com.aioveu.feign.form.WxMpUserForm;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserMpSubscribeService extends IService<UserMpSubscribe> {


    /**
     * 公众号关注
     * @param form
     * @return
     */
    Boolean subscribe(@RequestBody WxMpUserForm form);

    /**
     * 获取指定appId的openId
     * @param userId
     * @param appId
     * @return
     */
    String getOpenIdByUserIdAndAppId(String userId, String appId);

    /**
     * 查询公众号是否关注
     * @param openId
     * @return
     */
    boolean subscribeByOpenId(String openId);

    /**
     * 获取userid是否订阅appid
     * @param userId
     * @param appId
     * @return
     */
    Boolean getIsSubscribe(String userId, String appId);


}
