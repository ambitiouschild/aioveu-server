package com.aioveu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <h1>获取优惠券请求对象定义</h1>
 * Created by Qinyi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcquireTemplateRequest {

    /** 用户 id */
    private String userId;

    private String orderId;

    /** 优惠券模板信息 */
    private CouponTemplateSDK templateSDK;

    /**
     * 优惠券价值金额
     */
    private BigDecimal singleCouponAmount;

    /**
     * 是否限制领取
     */
    private boolean limit;
}
