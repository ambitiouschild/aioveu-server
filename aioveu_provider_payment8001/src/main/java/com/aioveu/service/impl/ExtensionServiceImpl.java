package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.constant.PhoneCodeConstant;
import com.aioveu.dao.ExtensionUserDao;
import com.aioveu.dao.UserDao;
import com.aioveu.dto.ExtensionUserDTO;
import com.aioveu.entity.ExtensionShare;
import com.aioveu.entity.ExtensionUser;
import com.aioveu.entity.User;
import com.aioveu.exception.SportException;
import com.aioveu.service.*;
import com.aioveu.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service
public class ExtensionServiceImpl extends ServiceImpl<UserDao, User> implements ExtensionService {

    @Autowired
    UserService userService;

    @Autowired
    ExtensionServiceImpl extensionServiceImpl;

    @Autowired
    UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    private ExtensionShareService extensionShareService;

    @Autowired
    ExtensionUserService extensionUserService;

    @Autowired
    ExtensionUserDao extensionUserDao;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ExtensionUserDTO dataDTO) {
        //验证短信
        extensionServiceImpl.isGendered(dataDTO.getPhone(), dataDTO.getCaptcha());
        //验证密码
        String password = dataDTO.getPassword();
        if (password == null || !Objects.equals(password, dataDTO.getConfirmPassword())) {
            throw new SportException("密码出现错误，请检查后重试");
        }
        String phone = dataDTO.getPhone();
        if (phone != null && phone.length() == 11) {
            LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(User::getPhone, phone);
            User userExist = userDao.selectOne(wrapper);
            System.out.println(userExist+"=====================================================");
            if (userExist != null) {
                String id = userExist.getId();
                LambdaQueryWrapper<ExtensionUser> wrapper1 = Wrappers.lambdaQuery();
                wrapper1.eq(ExtensionUser::getUserId, id);
                Integer integer = extensionUserDao.selectCount(wrapper1);
                if (integer > 0) {
                    throw new SportException("用户已存在");
                }
                userRoleInsert(dataDTO, userExist.getId());
                return;
            }
        }
        User user = new User();
        //用户userName给name字段
        user.setName(dataDTO.getUserName());
        //手机号赋值一份给userName，作为登录的账户
        user.setUsername(phone);

        user.setStatus(3);//审核状态
        user.setPhone(phone);

        user.setPassword(passwordEncoder.encode(dataDTO.getPassword()));
        userDao.insert(user); //用户表新增
        String userId = user.getId();
        userRoleInsert(dataDTO, userId);
    }


    private void userRoleInsert(ExtensionUserDTO dataDTO, String userId) {
        //角色用户表新增
        roleUserService.save(userId, Collections.singletonList("offline_promotion"));
        String extensionId = dataDTO.getExtensionId();

        ExtensionUser extensionUser = new ExtensionUser();
        extensionUser.setName(dataDTO.getUserName());
        extensionUser.setUserId(userId);

        if (extensionId != null) {
            extensionUser.setExtensionId(dataDTO.getExtensionId());
            //推荐人绑定
            ExtensionShare share = new ExtensionShare();
            share.setExtensionId(userId);
            share.setShareId(extensionId);
            extensionShareService.recodeShare(share);
        }
        System.out.println(extensionUser);
        extensionUserService.insterExtensionUser(extensionUser);
    }


    public boolean isGendered(String phone, String captcha) {
        String dataCaptcha = redisTemplate.opsForValue().get(PhoneCodeConstant.SPORT_PHONE_CODE + PhoneCodeConstant.CODE_TYPE_LOGIN + phone);
        if (dataCaptcha == null) {
            throw new SportException("验证码失效");
        }
        if (!dataCaptcha.equals(captcha)) {
            throw new SportException("验证码错误,请重新输入");
        }
        return true;
    }

    @Override
    public void modifyExamine(User user) {
        try {
            LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
            wrapper.set(User::getStatus, user.getStatus()).eq(User::getId, user.getId());
            userDao.update(null, wrapper);
        } catch (Exception e) {
            throw new SportException("未找到该用户，请确认信息");
        }
    }

    @Override
    public IPage<UserVo> selExtensionUser(int page, int size, String username, String id) {
        return userDao.selExtensionUser(new Page(page, size), username, id);
    }

    @Override
    public Integer modifyExtensionUser(User user) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getId, user.getId());
        int update = userDao.update(user, wrapper);
        if (update <= 0) {
            throw new SportException("没有找到该人员信息");
        }
        return update;
    }

    @Override
    public Integer deleteExtensionUser(String id) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getId, id);
        return userDao.delete(wrapper);
    }
}
