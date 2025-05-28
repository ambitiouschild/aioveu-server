package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_order_detail")
@Data
public class OrderDetail extends StringIdEntity {

    private String orderId;

    /**
     * 分类id
     */
    private Long categoryId;

    private String productId;

    private String productName;

    /**
     * 价格
     */
    private BigDecimal productPrice;

    private Integer productQuantity;

    private String productImage;

    private String userId;

    private String phone;

    /**
     * 订单明细核销码
     */
    private String consumeCode;

    private Long storeId;

    private Long companyId;

    private Date startTime;

    private Date endTime;

    /**
     * 激活时间
     */
    private Date activeTime;

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
     * 支付方式
     */
    private String payType;

}
