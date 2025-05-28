package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserTagDao;
import com.aioveu.entity.UserTag;
import com.aioveu.service.UserTagService;
import com.aioveu.utils.SportStreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserTagServiceImpl extends ServiceImpl<UserTagDao, UserTag> implements UserTagService {


    @Override
    public UserTag getByName(String name, Long userInfoId) {
        QueryWrapper<UserTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserTag::getUserInfoId, userInfoId).eq(UserTag::getName, name);
        return getOne(queryWrapper);
    }

    @Override
    public List<UserTag> getByUserInfoId(Long userInfoId) {
        QueryWrapper<UserTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserTag::getUserInfoId, userInfoId);
        List<UserTag> tagList = list(queryWrapper);
        return tagList.stream().filter(SportStreamUtils.distinctByKey(UserTag::getName)).collect(Collectors.toList());
    }
}
