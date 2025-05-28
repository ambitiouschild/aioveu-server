package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.constant.ResultEnum;
import com.aioveu.dao.UserExtensionAccountDao;
import com.aioveu.entity.UserExtensionAccount;
import com.aioveu.exception.SportException;
import com.aioveu.service.UserBalanceChangeService;
import com.aioveu.service.UserExtensionAccountService;
import com.aioveu.vo.UserExtensionAccountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/7 0007 23:26
 */
@Slf4j
@Service
public class UserExtensionAccountServiceImpl extends ServiceImpl<UserExtensionAccountDao, UserExtensionAccount> implements UserExtensionAccountService {

    @Autowired
    private UserBalanceChangeService userBalanceChangeService;

    /**
     * 查询用户的推广账户
     * @param userId
     * @return
     */
    @Override
    public UserExtensionAccount getByUserId(String userId){
        QueryWrapper<UserExtensionAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserExtensionAccount::getUserId, userId);
        return getOne(queryWrapper);
    }

    @Override
    public IPage<UserExtensionAccountVO> getAll(int page, int size, String keyword) {
        return getBaseMapper().getAllList(new Page<>(page, size), keyword);
    }

    @Override
    public boolean addBalance(String userId, BigDecimal amount, String name, String description, String orderId) {
        UserExtensionAccount ua = getByUserId(userId);
        if (ua == null) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "请联系平台开通推广账户!");
        }
        ua.setBalance(ua.getBalance().add(amount));
        if (updateById(ua) && userBalanceChangeService.addBalanceRecord(userId, amount, orderId, name, description, 1)) {
            return true;
        }
        throw new SportException(userId + "推广余额操作失败！");
    }

    @Override
    public boolean reduceBalance(String userId, BigDecimal amount, String name, String description, String orderId, boolean negative) {
        UserExtensionAccount ua = getByUserId(userId);
        if (ua == null) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "请联系平台开通推广账户!");
        }
        ua.setBalance(ua.getBalance().subtract(amount));
        if (!negative && ua.getBalance().doubleValue() < 0) {
            throw new SportException("当前账户余额不足！");
        }
        if (updateById(ua) && userBalanceChangeService.reduceBalanceRecord(userId, amount, orderId, name, description, 1)) {
            return true;
        }
        throw new SportException(userId + "余额操作失败！");
    }
}
