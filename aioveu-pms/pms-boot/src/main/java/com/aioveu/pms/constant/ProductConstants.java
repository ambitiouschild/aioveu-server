package com.aioveu.pms.constant;

/**
 * @Description: TODO 商品常量
 * @Author: 雒世松
 * @Date: 2025/6/5 18:27
 * @param
 * @return:
 **/

public interface ProductConstants {

    /**
     * 锁定的商品列表缓存键前缀
     */
    String LOCKED_SKUS_PREFIX = "product:locked_skus:";

    /**
     * 商品分布式锁缓存键前缀
     */
    String SKU_LOCK_PREFIX = "product:sku_lock:";

    /**
     * 临时规格ID前缀
     */
    String SPEC_TEMP_ID_PREFIX = "tid_";

}
