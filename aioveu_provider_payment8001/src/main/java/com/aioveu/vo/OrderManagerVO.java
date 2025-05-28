package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/1 13:33
 */
@Data
public class OrderManagerVO {

    private String id;

    private String name;

    private String categoryName;

    private Date createDate;

    private BigDecimal amount;

    private Integer status;

    private String productId;

    private Integer count;

    private String phones;

    private String username;

    private String saleName;

    private String userId;

    private Long categoryId;
}
