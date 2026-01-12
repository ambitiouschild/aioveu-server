package com.aioveu.sms.aioveu03CouponHistory.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO 优惠券领取/使用记录实体对象
 * @TableName sms_coupon_history
 * @Author: 雒世松
 * @Date: 2025/6/5 18:46
 * @param
 * @return:
 **/

@TableName(value ="sms_coupon_history")
@Data
public class SmsCouponHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员昵称
     */
    private String memberNickname;

    /**
     * 优惠券码
     */
    private String couponCode;

    /**
     * 获取类型(1：后台增删；2：主动领取)
     */
    private Integer getType;

    /**
     * 状态(0：未使用；1：已使用；2：已过期)
     */
    private Integer status;

    /**
     * 使用时间
     */
    private Date useTime;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderSn;

}