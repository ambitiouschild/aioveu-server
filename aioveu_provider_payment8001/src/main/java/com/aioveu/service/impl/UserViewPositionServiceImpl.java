package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserViewPositionDao;
import com.aioveu.entity.UserViewPosition;
import com.aioveu.service.UserViewPositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserViewPositionServiceImpl extends ServiceImpl<UserViewPositionDao, UserViewPosition> implements UserViewPositionService {


    @Override
    public String getPositionByUserId(String userId) {
        QueryWrapper<UserViewPosition> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserViewPosition::getUserId, userId)
                .isNotNull(UserViewPosition::getAddress)
                .orderByDesc(UserViewPosition::getCreateDate)
                .last("limit 1");
        UserViewPosition userViewPosition = getOne(queryWrapper);
        if (userViewPosition != null) {
            return userViewPosition.getAddress();
        }
        return null;
    }
}
