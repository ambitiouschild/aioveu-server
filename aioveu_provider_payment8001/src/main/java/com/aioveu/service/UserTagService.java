package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserTag;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserTagService extends IService<UserTag> {

    /**
     * 通过名称查找tag
     * @param name
     * @param userInfoId
     * @return
     */
    UserTag getByName(String name, Long userInfoId);

    /**
     * 获取用户的标签
     * @param userInfoId
     * @return
     */
    List<UserTag> getByUserInfoId(Long userInfoId);



}
