package com.aioveu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.aioveu.enums.UserCouponStatus;
import com.aioveu.coupon.serialization.UserCouponSerialize;
import com.aioveu.vo.CouponTemplateSDK;
import com.aioveu.vo.TemplateRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <h1>优惠券(用户领取的优惠券记录)实体表</h1>
 * Created by Qinyi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(autoResultMap = true, value = "sport_user_coupon")
@JsonSerialize(using = UserCouponSerialize.class)
public class UserCoupon implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联优惠券模板的主键(逻辑外键) */
    private Long templateId;

    /** 领取用户 */
    private String userId;

    /** 优惠券码 */
    private String couponCode;

    /**
     * 激活价格
     */
    private BigDecimal activePrice;

    /** 优惠券状态 */
    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    private UserCouponStatus status;

    /** 用户优惠券对应的模板信息 */
    @Transient
    @TableField(typeHandler = FastjsonTypeHandler.class, exist = false)
    private CouponTemplateSDK templateSDK;

    /** 优惠券规则 */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private TemplateRule rule;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;

    private Date updateDate;

    /**
     * 哪个订单领取的
     */
    private String orderId;

    /**
     * 优惠券价值金额
     */
    private BigDecimal amount;

    @TableField(exist = false)
    private String ruleStr;

    @TableField(exist = false)
    /**
     * 会员价可用
     * 为true，正常使用
     * 为false，选择会员卡，使用会员价的时候，不可使用该卡券叠加优惠
     */
    private Integer vipPriceCanUse;

    /**
     * <h2>返回一个无效的 Coupon 对象</h2>
     * */
    public static UserCoupon invalidCoupon() {

        UserCoupon coupon = new UserCoupon();
        coupon.setId(-1L);
        return coupon;
    }

    /**
     * <h2>构造优惠券</h2>
     * */
    public UserCoupon(Long templateId, String userId, String couponCode,
                      UserCouponStatus status) {

        this.templateId = templateId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = status;
    }

}
