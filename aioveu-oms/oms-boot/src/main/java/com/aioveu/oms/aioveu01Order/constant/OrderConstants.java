package com.aioveu.oms.aioveu01Order.constant;

/**
 * @Description: TODO 订单相关常量
 * 该接口定义了与订单相关的常量
 * @Author: 雒世松
 * @Date: 2025/6/5 18:03
 * @param
 * @return:
 **/

public interface OrderConstants {

    /**
     * 会员购物车缓存键前缀
     */
    String MEMBER_CART_PREFIX = "order:cart:";

    /**
     * 订单防重提交令牌缓存键前缀
     */
    String ORDER_TOKEN_PREFIX = "order:token:";

    /**
     * 订单锁缓存键前缀
     */
    String ORDER_LOCK_PREFIX = "order:lock";

}
