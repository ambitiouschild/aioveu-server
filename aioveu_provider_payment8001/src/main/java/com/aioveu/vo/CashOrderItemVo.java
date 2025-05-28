package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/6 0006 15:23
 */
@Data
public class CashOrderItemVo extends IdNameVO {

    private BigDecimal amount;

    private Integer status;

    private Date createDate;

}
