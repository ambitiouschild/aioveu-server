package com.aioveu.common.constant;

/**
 * @Description: TODO 订单常量
 * @Author: 雒世松
 * @Date: 2025/6/5 15:38
 * @param
 * @return:
 **/

public interface OrderConstants {

    /**
     * 会员购物车缓存KEY前缀
     */
    String MEMBER_CART_PREFIX = "MEMBER:CART:";

    /**
     * 订单防重提交锁KEY前缀
     */
    String ORDER_RESUBMIT_LOCK_PREFIX = "ORDER:RESUBMIT_LOCK:";


    /**
     * 订单锁前缀
     *
     */
    String ORDER_LOCK_PREFIX = "ORDER:LOCK:";

}
