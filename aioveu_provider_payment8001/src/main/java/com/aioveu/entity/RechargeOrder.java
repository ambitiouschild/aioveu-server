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
@TableName("sport_recharge_order")
@Data
public class RechargeOrder extends StringIdNameEntity {

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 实际消费金额
     */
    private BigDecimal actualAmount;

    /**
     * 赠送金额
     */
    private BigDecimal giveAmount;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    private String payType;

    /**
     * 微信支付预支付回话标识
     */
    private String prepayId;

    private String nonceStr;

    private String appId;

    private String wxPayId;

    private String userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payFinishTime;

    private String remark;

    // status 1 待支付 2 已支付

}
