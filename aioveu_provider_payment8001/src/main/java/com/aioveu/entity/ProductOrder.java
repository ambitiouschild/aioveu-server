package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品订单表
 */
@TableName("sport_product_order")
@Data
public class ProductOrder extends StringIdNameEntity {

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 订单总金额
     */
    private BigDecimal amount;

    /**
     * 软件订阅费
     */
    private BigDecimal softPrice;

    /**
     * 订单优惠金额
     */
    private BigDecimal couponAmount;

    /**
     * 消费金额
     */
    private BigDecimal consumeAmount;

    /**
     * 实际消费金额
     */
    private BigDecimal actualAmount;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 可退款金额
     */
    private BigDecimal canRefundAmount;

    private String payType;

    /**
     * 电子公章
     */
    private String seal;

    /**
     * 微信支付预支付回话标识
     */
    private String prepayId;

    private String nonceStr;

    private String appId;

    /**
     * 创建销售用户id
     */
    private String userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payFinishTime;

    private String remark;

    private String customerName;

    private String customerTel;

    private String companyName;

    private String customerEmail;

    private String customerAddress;

    private String adminPhone;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDay;

    /**
     * 合同地址
     */
    private String agreementUrl;

    /**
     * 补充条款
     */
    private String terms;

    /**
     * 支付id 支付订单号每次必须唯一 过期之后需要重新生成
     */
    private String payOrderId;

    // status 20 已签署

}
