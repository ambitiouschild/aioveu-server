package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/29 17:30
 */
@TableName("sport_order_refund")
@Data
public class OrderRefund extends StringIdEntity {

    private String orderId;

    private String orderDetailId;

    private BigDecimal refundAmount;

    private Integer status;

    private String errorCode;

    private String errorMsg;

    private Date refundFinish;

    private String remark;

    // status 9 已退款完成 12退款中  2退款创建失败

}
