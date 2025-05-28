package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/7 0007 23:14
 */
@Data
public class AnalysisOrderVO {

    /**
     * 金额
     */
    private BigDecimal amount;

    private String orderId;

    private String username;

    private String userId;

    private String saleUserId;

    private String phone;

    private String name;

    private Date createDate;

    private Date activeTime;

    private Long categoryId;

    private String categoryName;

    private Integer poolType;

    private Long venueId;

    private String venueName;

}
