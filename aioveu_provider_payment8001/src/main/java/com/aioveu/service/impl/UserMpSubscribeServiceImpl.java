package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserMpSubscribeDao;
import com.aioveu.entity.UserMpSubscribe;
import com.aioveu.feign.form.WxMpUserForm;
import com.aioveu.service.UserMpSubscribeService;
import com.aioveu.service.UserOpenIdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserMpSubscribeServiceImpl extends ServiceImpl<UserMpSubscribeDao, UserMpSubscribe> implements UserMpSubscribeService {

    @Autowired
    private UserOpenIdService userOpenIdService;

    @Override
    public Boolean subscribe(WxMpUserForm form) {
        UserMpSubscribe us = new UserMpSubscribe();
        BeanUtils.copyProperties(form, us);
        if ("subscribe".equals(form.getEvent()) && form.getEventKey() != null) {
            updateMpOpenId(form, "qrscene_uid_");
        } else if ("SCAN".equals(form.getEvent()) && form.getEventKey() != null) {
            updateMpOpenId(form, "uid_");
        }
        return save(us);
    }

    /**
     * 更新用户公众号的openId
     * @param form
     * @param useridPrefix
     */
    private void updateMpOpenId(WxMpUserForm form, String useridPrefix) {
        if (form.getEventKey().startsWith(useridPrefix)) {
            // 提取前缀后面的部分
            String userId = form.getEventKey().substring(useridPrefix.length());
            userOpenIdService.addOrUpdate(userId, form.getAppId(), form.getOpenId(), form.getUnionId());
        }
    }

    @Override
    public String getOpenIdByUserIdAndAppId(String userId, String appId) {
        String openId = userOpenIdService.getByAppIdAndUserId(userId, appId, false);
        if (StringUtils.isEmpty(openId)) {
            log.warn("用户:{}未关注:{}公众号", userId, appId);
        } else {
            if (subscribeByOpenId(openId)) {
                return openId;
            }
        }
        return null;
    }

    @Override
    public boolean subscribeByOpenId(String openId) {
        UserMpSubscribe userMpSubscribe = getBaseMapper().getSubscribeStatusByOpenId(openId);
        if (userMpSubscribe != null) {
            if ("subscribe".equals(userMpSubscribe.getEvent())) {
                return true;
            } else {
                log.warn("用户:{}已取消关注公众号", openId);
            }
        }
        return false;
    }

    @Override
    public Boolean getIsSubscribe(String userId, String appId) {
        String openId = userOpenIdService.getByAppIdAndUserId(userId, appId, false);
        if (StringUtils.isEmpty(openId)) {
            log.warn("用户:{}未订阅:{}公众号", userId, appId);
        } else {
            return subscribeByOpenId(openId);
        }
        return false;
    }
}
