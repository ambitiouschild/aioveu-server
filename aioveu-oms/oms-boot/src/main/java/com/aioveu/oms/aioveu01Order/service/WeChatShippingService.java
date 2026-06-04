package com.aioveu.oms.aioveu01Order.service;


import com.fasterxml.jackson.databind.JsonNode;

/**
 * @ClassName: WeChatShippingService
 * @Description TODO 微信发货管理的三个核心接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 17:14
 * @Version 1.0
 **/

public interface WeChatShippingService {


    /**
     * 1. 录入发货信息
     * @param orderSn 自家系统的订单orderSn
     */
    JsonNode uploadShipping(String orderSn);

    /**
     * 2. 提醒用户确认收货（以自有系统为准）
     * @param orderSn 自家系统的订单orderSn
     */
    JsonNode notifyConfirmReceive(String orderSn);

    /**
     * 3. 查询订单状态
     * @param orderSn 自家系统的订单orderSn
     */
    JsonNode queryOrder(String orderSn);

}
