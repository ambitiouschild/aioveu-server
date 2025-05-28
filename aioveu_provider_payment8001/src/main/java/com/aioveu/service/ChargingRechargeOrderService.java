package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ChargingRechargeOrder;
import com.aioveu.form.ChargingRechargeOrderForm;
import com.aioveu.vo.WxMaPayVO;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ChargingRechargeOrderService extends IService<ChargingRechargeOrder> {


    /**
     * 创建充值订单
     * @param form
     * @return
     */
    WxMaPayVO create(ChargingRechargeOrderForm form);

    /**
     * 充值订单支付成功回调
     * @param orderId
     * @param payFinishTime
     * @return
     */
    boolean updateOrder2Success(String orderId, Date payFinishTime);

    /**
     * 检查支付状态
     * @param orderId
     * @return
     */
    boolean checkPayStatus(String orderId);


}
