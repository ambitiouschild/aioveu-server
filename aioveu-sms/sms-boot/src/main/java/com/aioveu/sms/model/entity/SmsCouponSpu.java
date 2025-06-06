package com.aioveu.sms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: TODO sms_coupon_spu
 * @Author: 雒世松
 * @Date: 2025/6/5 18:47
 * @param
 * @return:
 **/

@TableName(value ="sms_coupon_spu")
@Data
@Accessors(chain = true)
public class SmsCouponSpu implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 商品ID
     */
    private Long spuId;

    /**
     * 商品名称
     */
    private String spuName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}