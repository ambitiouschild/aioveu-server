package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 客户资料订单信息
 * @author: 雒世松
 * @date: 2025/2/1 0001 22:14
 */
@Data
public class UserInfoOrderVO {

    private String orderId;

    private Date createDate;

    private BigDecimal amount;

    private String childHead;

    private String childName;

    private String course;

    private String head;

    private Date startTime;

    private String gradeName;



}
