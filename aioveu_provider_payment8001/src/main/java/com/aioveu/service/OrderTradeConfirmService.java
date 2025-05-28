package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Order;
import com.aioveu.entity.OrderTradeConfirm;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/6 17:39
 */
public interface OrderTradeConfirmService extends IService<OrderTradeConfirm> {

    /**
     * 交易分账确认
     * @param order
     * @param payFinishTime
     * @return
     */
    boolean confirm(Order order, Date payFinishTime);


    /**
     * 交易分账取消
     * @param tradeConfirmId
     * @return
     */
    boolean confirmCancel(String tradeConfirmId);



}
