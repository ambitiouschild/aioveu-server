package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/1 13:33
 */
@Data
public class OrderDetailVO {

    private String orderId;

    private Long parentCategoryId;

    private Long categoryId;

    private String categoryCode;

    private String productName;

    private Long productId;

    /**
     * 充值金额(订单订金不够支付)
     */
    private BigDecimal rechargeAmount;

    /**
     * 订单支付总金额
     */
    private BigDecimal amount;

    /**
     * 优惠金额
     */
    private BigDecimal couponAmount;

    /**
     * 实际消费金额
     */
    private BigDecimal actualAmount;

    /**
     * 实际退款金额
     */
    private BigDecimal refundAmount;

    private BigDecimal consumeAmount;

    private Integer status;

    private Integer orderDetailStatus;

    /**
     * 退款状态
     */
    private Integer refundStatus;

    /**
     * 退款时间
     */
    private Date refundTime;

    private Date createDate;

    private Date startTime;

    private Date endTime;

    private String qrCode;

    private String consumeCode;

    private String path;

    private String userName;

    private String phone;

    private List<ConsumeVO> consumeList;

    /**
     * 协议地址
     */
    private String agreementUrl;

    private List<FieldOrderDetailVO> fieldOrderDetailVOList;
    private List<UserBalanceChangeItemVO> userBalanceChangeItemVOList;

    private String storeId;

    private String storeName;

    private String storeAddress;

    private Double longitude;

    private Double latitude;

}
