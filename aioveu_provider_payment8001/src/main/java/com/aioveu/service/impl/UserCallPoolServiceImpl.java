package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserCallPoolDao;
import com.aioveu.entity.UserCallPool;
import com.aioveu.exception.SportException;
import com.aioveu.service.UserCallPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserCallPoolServiceImpl extends ServiceImpl<UserCallPoolDao, UserCallPool> implements UserCallPoolService {

    @Autowired
    UserCallPoolDao userCallPoolDao;

    @Override
    public boolean addCount(String userId, Integer count) {
        QueryWrapper<UserCallPool> userCallPoolQueryWrapper = new QueryWrapper<>();
        userCallPoolQueryWrapper.eq("user_id", userId);
        UserCallPool userCallPool = getOne(userCallPoolQueryWrapper);
        if (userCallPool == null){
            throw new SportException("用户拨打池未配置");
        }
        userCallPool.setPoolSize(userCallPool.getPoolSize() + count);
        return updateById(userCallPool);
    }

    @Override
    public IPage<UserCallPool> selUserCallPoolByCondition(int page, int size, String userId) {
        LambdaQueryWrapper<UserCallPool> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserCallPool::getUserId, userId);
        IPage<UserCallPool> poolIPage = new Page<>(page, size);
        return userCallPoolDao.selectPage(poolIPage, wrapper);
    }

    @Override
    public void modifyUserCallPool(UserCallPool userCallPool) {
        LambdaUpdateWrapper<UserCallPool> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(UserCallPool::getId, userCallPool.getId());
        userCallPoolDao.update(userCallPool, wrapper);
    }

    @Override
    public void deleteUserCallPoolById(Integer id) {
        LambdaQueryWrapper<UserCallPool> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserCallPool::getId, id);
        userCallPoolDao.delete(wrapper);
    }
}
