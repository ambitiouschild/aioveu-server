package com.aioveu.sms.aioveu05CouponSpuCategory.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 优惠券适用的具体分类实体对象
 * @Date  2026/1/12 13:06
 * @Param
 * @return
 **/

@TableName(value ="sms_coupon_spu_category")
@Data
@Accessors(chain = true)
public class SmsCouponSpuCategory implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
     * 商品分类ID
     */
    private Long categoryId;


}