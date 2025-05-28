package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/5/26 11:07
 */
@Data
public class UserBalanceChangeItemVO extends IdNameVO {

    private String userVipCardName;

    private String vipCode;

    private Date createDate;

    private Integer changeType;

    private BigDecimal amount;

    private String description;

    private BigDecimal balance;

    private FieldOrderVO fieldOrderVO;
}
