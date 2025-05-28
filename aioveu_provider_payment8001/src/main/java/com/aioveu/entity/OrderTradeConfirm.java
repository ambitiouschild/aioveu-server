package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/5/11 10:41
 */
@TableName("sport_order_trade_confirm")
@Data
public class OrderTradeConfirm extends StringIdNameEntity {

    private String orderId;

    private Long companyId;

    private Long storeId;

    /**
     * 用户付款金额
     */
    private BigDecimal actualAmount;

    /**
     * 商户实收金额
     */
    private BigDecimal storeActualAmount;

    private BigDecimal splitAmount;

    private BigDecimal platformAmount;

    private String payAppId;

    private Date payTime;

    private String remark;

    // status 1 待发起交易分账确认 2 已发起交易分账确认 9 交易分账确认已取消 20 分账确认发起失败 24 分账确认取消失败

}
