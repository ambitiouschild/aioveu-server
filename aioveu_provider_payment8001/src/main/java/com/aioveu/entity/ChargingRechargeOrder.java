package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 店铺计费充值订单
 * @author: 雒世松
 * @date: Created in 2025/4/23 11:41
 */
@TableName("sport_charging_recharge_order")
@Data
public class ChargingRechargeOrder extends StringIdNameEntity {

    private String chargingCode;

    private Long companyId;

    private Long storeId;

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

    /**
     * 充值次数
     */
    private Integer count;

    private Integer giftCount;

    private String payType;

    /**
     * 微信支付预支付回话标识
     */
    private String prepayId;

    private String nonceStr;

    private String appId;

    private String userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payFinishTime;

    private String remark;

    /**
     * 店铺增值服务项目
     */
    private Long storeChargingOptionId;


    /**
     * 汇付商家ID或者微信支付AppId
     */
    private String payAppId;

}
