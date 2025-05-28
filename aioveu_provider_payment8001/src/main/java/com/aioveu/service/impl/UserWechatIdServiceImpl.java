package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.constant.PhoneCodeConstant;
import com.aioveu.dao.UserWechatIdDao;
import com.aioveu.entity.User;
import com.aioveu.entity.UserWechatId;
import com.aioveu.service.NoticeService;
import com.aioveu.service.RoleUserService;
import com.aioveu.service.UserService;
import com.aioveu.service.UserWechatIdService;
import com.aioveu.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserWechatIdServiceImpl extends ServiceImpl<UserWechatIdDao, UserWechatId> implements UserWechatIdService {

    @Autowired
    private UserService userService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private RoleUserService roleUserService;

    @Override
    public String getUserIdByWechatId(String wechatId) {
        QueryWrapper<UserWechatId> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserWechatId::getWxId, wechatId);
        UserWechatId userWechatId = getOne(queryWrapper);
        if (userWechatId != null) {
            return userWechatId.getUserId();
        }
        return null;
    }

    @Override
    public String bindWechatId(String wechatId, String phone, String verificationCode) {
        String code = noticeService.getCode(phone, PhoneCodeConstant.CODE_TYPE_REGISTER_BIND);
        if (StringUtils.isBlank(code)) {
            return "验证码已过期，需要用之前的手机号码重新发送验证码";
        }
        if (!code.equals(verificationCode)) {
            return "验证码错误, 请用户检查验证码是否填写正确";
        }
        User user = userService.getByUserPhone(phone);
        if (user != null) {
            return create(wechatId, user, "111", 10L);
        } else {
            user = userService.quickRegisterByPhone(phone, null);
            if (user == null) {
                return "绑定失败, 请稍后重试";
            }
            return create(wechatId, user, "112", 20L);
        }
    }

    @Override
    public UserWechatId getByUserId(String userId) {
        QueryWrapper<UserWechatId> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserWechatId::getUserId, userId);
        return getOne(queryWrapper);
    }

    private String create(String wechatId, User user, String robotId, Long companyId) {
        if (StringUtils.isEmpty(wechatId)) {
            wechatId = "雒世松2011";
        }
        UserWechatId userWechatId = new UserWechatId();
        userWechatId.setWxId(wechatId);
        userWechatId.setUserId(user.getId());
        userWechatId.setRobotId(robotId);
        userWechatId.setCompanyId(companyId);
        if (save(userWechatId)) {
            LoginVal loginVal = DataUtil.getByUser(user, roleUserService.getByUserId(user.getId()));
            OauthUtils.setCurrentUser(loginVal);
            return "绑定成功";
        } else {
            return "绑定失败, 请稍后重试";
        }
    }

}
