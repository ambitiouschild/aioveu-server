package com.aioveu.sms.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO sms_coupon_history
 * @TableName sms_coupon_history
 * @Author: 雒世松
 * @Date: 2025/6/5 18:46
 * @param
 * @return:
 **/

@TableName(value ="sms_coupon_history")
@Data
public class SmsCouponHistory implements Serializable {
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
    private Byte getType;

    /**
     * 状态(0：未使用；1：已使用；2：已过期)
     */
    private Byte status;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}