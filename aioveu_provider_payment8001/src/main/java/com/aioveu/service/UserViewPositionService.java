package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserViewPosition;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserViewPositionService extends IService<UserViewPosition> {

    /**
     * 获取最新的位置信息
     * @param userId
     * @return
     */
    String getPositionByUserId(String userId);


}
