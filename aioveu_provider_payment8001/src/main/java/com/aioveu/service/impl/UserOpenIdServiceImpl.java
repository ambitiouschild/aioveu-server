package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.constant.SportConstant;
import com.aioveu.dao.UserOpenIdDao;
import com.aioveu.entity.UserOpenId;
import com.aioveu.exception.SportException;
import com.aioveu.service.UserMpSubscribeService;
import com.aioveu.service.UserOpenIdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserOpenIdServiceImpl extends ServiceImpl<UserOpenIdDao, UserOpenId> implements UserOpenIdService {

    @Autowired
    private UserMpSubscribeService userMpSubscribeService;

    @Override
    public Boolean addOrUpdate(String userId, String appId, String openId, String unionId) {
        if (StringUtils.isEmpty(openId) || "undefined".equals(openId) || "null".equals(openId)) {
            throw new SportException("openId错误" + openId);
        }
        if (StringUtils.isEmpty(appId) || "undefined".equals(appId) || "null".equals(appId)) {
            throw new SportException("appId错误" + appId);
        }
        QueryWrapper<UserOpenId> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserOpenId::getAppId, appId).eq(UserOpenId::getUserId, userId);
        UserOpenId userOpenId = getOne(queryWrapper);
        if (userOpenId == null) {
            if (!SportConstant.QU_SHU_WECHAT_MP.equals(appId)) {
                // 趣数服务号绑定不做校验
                String oldPhone = this.baseMapper.getPhoneByOpenId(openId);
                if (StringUtils.isNotEmpty(oldPhone)) {
                    log.error("该微信已被" + oldPhone + "手机号码注册, 请使用" + oldPhone + "手机号登录绑定");
                    throw new SportException("该微信已被" + oldPhone + "手机号码注册, 请使用" + oldPhone + "手机号登录绑定");
                }
            }
            QueryWrapper<UserOpenId> qw = new QueryWrapper<>();
            qw.lambda().eq(UserOpenId::getOpenId, openId);
            remove(qw);
            userOpenId = new UserOpenId();
            userOpenId.setUserId(userId);
            userOpenId.setOpenId(openId);
            userOpenId.setAppId(appId);
            userOpenId.setUnionId(unionId);
            return save(userOpenId);
        } else {
            UserOpenId u = new UserOpenId();
            u.setId(userOpenId.getId());
            u.setOpenId(openId);
            u.setUnionId(unionId);
            u.setAppId(appId);
            u.setUpdateDate(new Date());
            return updateById(u);
        }
    }

    @Override
    public String getByAppIdAndUserId(String userId, String appId, boolean require) {
        QueryWrapper<UserOpenId> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserOpenId::getAppId, appId).eq(UserOpenId::getUserId, userId);
        UserOpenId userOpenId = getOne(queryWrapper);
        if (userOpenId != null) {
            String openId = userOpenId.getOpenId();
            if (StringUtils.isEmpty(openId) || "undefined".equals(openId) || "null".equals(openId)) {
                log.info("openId存值错误:" + userId);
            } else {
                return userOpenId.getOpenId();
            }
        }
        if (require) {
            log.error("用户:{}, 程序:{}对应的openId不存在", userId, appId);
            throw new SportException("OpenId参数异常");
        }
        return null;
    }

    @Override
    public String getMpOpenIdByStoreIdAndUserId(Long storeId, String userId, boolean subscribe) {
        String openId = getBaseMapper().getStoreMpUserOpenId(storeId, userId);
        if (StringUtils.isNotEmpty(openId)) {
            if (subscribe) {
                if (userMpSubscribeService.subscribeByOpenId(openId)) {
                    return openId;
                }
            } else {
                return openId;
            }
        }
        return null;
    }
}
