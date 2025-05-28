package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_order")
@Data
public class Order extends StringIdNameEntity {

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 订单总金额 = 优惠金额 + 消费金额 + 实际消费金额
     */
    private BigDecimal amount;

    /**
     * 订单优惠金额
     */
    private BigDecimal couponAmount;

    /**
     * 消费金额 就是本次消费的金额 包含虚拟卡付款的金额
     */
    private BigDecimal consumeAmount;

    /**
     * 实际消费金额 是实际线上支付的金额，不包含储值卡等虚拟卡的付款金额
     */
    private BigDecimal actualAmount;

    /**
     * 退款金额 发生退款时候的实际退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 可退款金额 还可以退款的金额 为0表示不能退款了
     */
    private BigDecimal canRefundAmount;

    private String productId;

    private String payType;

    /**
     * 微信支付预支付回话标识
     */
    private String prepayId;

    private String nonceStr;

    /**
     * 小程序或者公众号的id
     */
    private String appId;

    /**
     * 汇付商家ID或者微信支付AppId
     */
    private String payAppId;

    private String userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payFinishTime;

    private Date startTime;

    private Date endTime;

    /**
     * 激活时间
     */
    private Date activeTime;

    private Long companyId;

    private Long storeId;

    /**
     * 优惠券id
     */
    private Long userCouponId;

    private Integer count;

    /**
     * 地址id 用于快递发货
     */
    private Long addressId;

    private String address;

    /**
     * 分享用户id
     */
    private String shareUserId;

    private String remark;

    /**
     * 合同地址
     */
    private String agreementUrl;

    /**
     * 销售
     */
    private String saleUserId;

    /**
     * 订单核销码
     */
    private String consumeCode;

    /**
     * 用户会员卡ID
     */
    private Long userVipCardId;

    /**
     * 交易确认分账id 目前用于汇付支付 商户分账
     */
    private String tradeConfirmId;

}
