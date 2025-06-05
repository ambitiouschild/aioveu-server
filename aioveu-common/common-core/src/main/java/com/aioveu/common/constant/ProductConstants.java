package com.aioveu.common.constant;

/**
 * @Description: TODO 商品模块常量
 * @Author: 雒世松
 * @Date: 2025/6/5 15:38
 * @param
 * @return:
 **/

public interface ProductConstants {

    /**
     * 订单锁定的商品列表key前缀
     */
    String ORDER_LOCKED_SKUS_PREFIX = "order:locked:skus:";

    /**
     * 商品分布式锁key前缀
     */
    String SKU_LOCK_PREFIX = "product:sku:lock:";

    /**
     * 临时规格ID前缀
     */
    String SPEC_TEMP_ID_PREFIX = "tid_";

}
