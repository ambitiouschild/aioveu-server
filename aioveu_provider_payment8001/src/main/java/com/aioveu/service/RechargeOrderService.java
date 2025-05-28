package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.RechargeOrder;
import com.aioveu.vo.WxMaPayVO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface RechargeOrderService extends IService<RechargeOrder> {


    /**
     * 创建充值订单
     * @param username
     * @param id
     * @return
     */
    WxMaPayVO createRechargeOrder(String username, Long id);

    /**
     * 更新订单成功
     * @param orderId
     * @param payFinishTime
     * @return
     */
    boolean updateOrder2Success(String orderId, Date payFinishTime);

}
