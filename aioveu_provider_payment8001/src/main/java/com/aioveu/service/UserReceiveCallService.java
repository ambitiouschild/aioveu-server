package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserReceiveCall;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserReceiveCallService extends IService<UserReceiveCall> {

    /**
     * 根据id删除数据
     * @param idList
     * @param userId
     * @param nickname
     * @return
     */
    boolean deleteByIds(List<Long> idList, String userId, String nickname);

    /**
     * 根据userId删除数据
     * @param idList
     * @param userId
     * @return
     */
    boolean justDeleteAndUpdateCount(List<Long> idList, String userId);

    /**
     * 自动清理30天未联系的用户
     * @return
     */
    boolean autoRemove30DayUnContact();

    /**
     * 根据id获取 userInfoId
     * @param id
     * @return
     */
    UserReceiveCall getById(Long id);


}
