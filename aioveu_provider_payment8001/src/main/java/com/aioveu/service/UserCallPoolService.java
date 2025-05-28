package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserCallPool;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserCallPoolService extends IService<UserCallPool> {

    /**
     * 增加数量
     *
     * @param userId
     * @param count
     * @return
     */
    boolean addCount(String userId, Integer count);

    /**
     * 根据条件查询
     * @param page
     * @param size
     * @param userId
     * @return
     */
    IPage<UserCallPool> selUserCallPoolByCondition(int page, int size, String userId);

    void modifyUserCallPool(UserCallPool userCallPool);

    /**
     * 根据Id删除信息
     * @param id
     * @return
     */
    void deleteUserCallPoolById(Integer id);
}
