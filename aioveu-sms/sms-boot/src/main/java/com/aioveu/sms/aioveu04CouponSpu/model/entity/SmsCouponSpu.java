package com.aioveu.sms.aioveu04CouponSpu.model.entity;

import com.aioveu.common.core.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: TODO 优惠券适用的具体商品实体对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:47
 * @param
 * @return:
 **/

@TableName(value ="sms_coupon_spu")
@Data
@Accessors(chain = true)
public class SmsCouponSpu extends BaseEntityWithTenantId {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;



    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 商品ID
     */
    private Long spuId;

//    /**
//     * 商品名称
//     */
//    private String spuName;


}