package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.CashOrder;
import com.aioveu.vo.CashOrderItemVo;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface CashOrderService extends IService<CashOrder> {

    /**
     * 创建提现订单
     * @param userId
     * @param name
     * @param amount
     * @param appId
     * @return
     */
    boolean create(String userId, String name, BigDecimal amount, String appId);

    /**
     * 提现订单列表
     * @param page
     * @param size
     * @param username
     * @return
     */
    IPage<CashOrderItemVo> orderList(int page, int size, String username);


}
