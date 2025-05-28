package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@TableName("sport_invoice_request")
@Data
public class InvoiceRequest extends IdEntity {
    private Long companyId;
    private String invoiceHeader;
    private String invoiceTaxNumber;
    private String invoiceContent;
    private BigDecimal invoiceAmount;
    private String userId;
    private String issuerUserId;
    private Date issuerDate;
    private String receivingEmail;
    private String remark;
    private Integer status;
    private Date createDate;
    private Date updateDate;

    @TableField(exist = false)
    private List<InvoiceRequestDetail> invoiceRequestDetailList;

    @TableField(exist = false)
    private String userPhone;

    @TableField(exist = false)
    private String issuerUserName;
}

