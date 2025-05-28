package com.aioveu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description <h2>结算信息对象定义</h2>
 * 包含：
 *  1. userId
 *  2. 商品信息(列表)
 *  3. 优惠券列表
 *  4. 结算结果金额
 * @author: 雒世松
 * @date: 2025/1/30 0030 23:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementInfo {

    /**
     * 用户 id
     */
    private String userId;

    /**
     * 商品信息
     */
    private List<GoodsInfo> goodsInfos;

    /**
     * 优惠券列表
     */
    private List<CouponAndTemplateInfo> couponAndTemplateInfos;

    /**
     * 是否使结算生效，即核销
     */
    private Boolean employ;

    /**
     * 结果结算金额
     */
    private Double cost;

    /**
     * 优惠金额
     */
    private Double couponCost;

    /**
     * 总金额
     */
    private BigDecimal totalCost;

    /**
     * <h2>优惠券和模板信息</h2>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponAndTemplateInfo {

        /**
         * Coupon 的主键
         */
        private Long id;

        /**
         * 优惠券对应的模板对象
         */
        private CouponTemplateSDK template;

    }


}
