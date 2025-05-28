package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("sport_invoice_request_detail")
@Data
public class InvoiceRequestDetail {
    @TableId(type = IdType.AUTO)
    private Long Id;
    private Long invoiceRequestId;
    private String orderId;
    private BigDecimal orderAmount;
    private String orderName;

    @TableField(exist = false)
    private Date orderCreateDate;
}