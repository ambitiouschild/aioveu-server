package com.aioveu.oms.aioveu01Order.utils;


import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;

/**
 * @ClassName: DoUploadShippingUtils
 * @Description TODO  发货上传工具
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/27 8:22
 * @Version 1.0
 **/

public class DoUploadShippingUtils {


    /**
     * 根据订单属性判定物流类型
     * 主流做法：订单表里加一个字段 logistics_type，或者根据商品类型判定
     */
    public static int determineLogisticsType(OmsOrder order) {
        // 方案A：从订单表字段读（推荐）
        if (order.getLogisticsType() != null) {
            return order.getLogisticsType();
        }

        // 方案B：根据商品是否有实物判定
        // 虚拟商品（卡券、会员等）→ 2
        // 实物商品 → 1
        boolean hasPhysicalProduct = checkHasPhysicalProduct(order.getId());
        return hasPhysicalProduct ? 1 : 2;
    }

    /**
     * 手机号脱敏（微信推荐格式）
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return phone;
        }
        return "+86-" + phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
