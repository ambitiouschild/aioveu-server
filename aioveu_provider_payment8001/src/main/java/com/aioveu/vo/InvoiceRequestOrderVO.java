package com.aioveu.vo;

import com.aioveu.entity.Agreement;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InvoiceRequestOrderVO {

    private String orderId;

    private String orderName;

    private String categoryCode;

    private BigDecimal amount;

    private String status;

    private Date createDate;
}
