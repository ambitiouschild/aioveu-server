package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.RechargeOptionDao;
import com.aioveu.entity.RechargeOption;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.RechargeOptionService;
import com.aioveu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class RechargeOptionServiceImpl extends ServiceImpl<RechargeOptionDao, RechargeOption> implements RechargeOptionService {

    @Autowired
    private UserService userService;

    @Override
    public List<RechargeOption> getRechargeList() {
        QueryWrapper<RechargeOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RechargeOption::getStatus, DataStatus.NORMAL.getCode())
                .orderByAsc(RechargeOption::getMoney);
        return list(queryWrapper);
    }
}
