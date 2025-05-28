package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.CashOrderDao;
import com.aioveu.entity.CashOrder;
import com.aioveu.service.CashOrderService;
import com.aioveu.service.UserService;
import com.aioveu.vo.CashOrderItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class CashOrderServiceImpl extends ServiceImpl<CashOrderDao, CashOrder> implements CashOrderService {

    @Autowired
    private UserService userService;

    @Override
    public boolean create(String userId, String name, BigDecimal amount, String appId) {
        CashOrder cashOrder = new CashOrder();
        cashOrder.setName(name);
        cashOrder.setAmount(amount);
        cashOrder.setAppId(appId);
        cashOrder.setUserId(userId);
        cashOrder.setPayType("bank");
        cashOrder.setStatus(1);
        return save(cashOrder);
    }

    @Override
    public IPage<CashOrderItemVo> orderList(int page, int size, String username) {
        QueryWrapper<CashOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CashOrder::getUserId, userService.getUserIdFromCache(username));

        Page<CashOrder> cashOrderPage = page(new Page<>(page, size), queryWrapper);

        List<CashOrderItemVo> records = cashOrderPage.getRecords().stream().map(item -> {
            CashOrderItemVo companyItemVO = new CashOrderItemVo();
            BeanUtils.copyProperties(item, companyItemVO);
            return companyItemVO;
        }).collect(Collectors.toList());

        IPage<CashOrderItemVo> iPage = new Page<>();
        BeanUtils.copyProperties(cashOrderPage, iPage);
        iPage.setRecords(records);
        return iPage;
    }
}
