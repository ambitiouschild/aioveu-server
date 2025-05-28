package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.OrderRefund;
import com.aioveu.feign.vo.WxPayOrderRefundNotifyResultVO;
import com.aioveu.vo.BasicRefundOrderVO;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface OrderRefundService extends IService<OrderRefund> {

    /**
     * 微信退款成功通知
     * @param wxPayOrderRefundNotifyResultVO
     * @return
     */
    boolean refundSuccessWechatNotice(WxPayOrderRefundNotifyResultVO wxPayOrderRefundNotifyResultVO);

    /**
     * 订单ID查找退款信息
     * @param orderId
     * @return
     */
    OrderRefund getByOrderId(String orderId);

    /**
     * 获取退款订单
     * @param storeId
     * @param start
     * @param end
     * @param categoryCode
     * @return
     */
    List<BasicRefundOrderVO> getDateRangeRefundOrder(Long storeId, Date start, Date end, String categoryCode);

    /**
     * 退款成功更新
     * @param refundId
     * @param refundFinishTime
     * @param account
     * @return
     */
    boolean refundSuccess(String refundId, Date refundFinishTime, String account);


}
