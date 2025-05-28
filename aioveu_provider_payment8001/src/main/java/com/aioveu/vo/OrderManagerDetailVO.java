package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/1 13:33
 */
@Data
public class OrderManagerDetailVO {

    private String id;

    private String name;

    private Date createDate;

    private BigDecimal amount;

    private Integer status;

    private String productId;

    private String productName;

    private Integer count;

    private String userId;

    private Long categoryId;

    private List<OrderUserVO> userList;

}
