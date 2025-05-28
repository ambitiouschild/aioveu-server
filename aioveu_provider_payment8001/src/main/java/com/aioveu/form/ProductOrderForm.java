package com.aioveu.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:43
 */
@Data
public class ProductOrderForm {

    @NotNull(message = "分类id不能为空")
    private Long categoryId;

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    @NotNull(message = "软件订阅费不能为空")
    private BigDecimal softPrice;

    private String remark;

    @NotEmpty(message = "客户名称不能为空")
    private String customerName;

    @NotEmpty(message = "客户联系电话不能为空")
    private String customerTel;

    @NotEmpty(message = "客户公司名称不能为空")
    private String companyName;

    @NotEmpty(message = "客户邮箱不能为空")
    private String customerEmail;

    @NotEmpty(message = "客户地址不能为空")
    private String customerAddress;

    @NotEmpty(message = "账号手机号不能为空")
    private String adminPhone;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "软件开始时间不能为空")
    private Date startDay;

    private String terms;

}
