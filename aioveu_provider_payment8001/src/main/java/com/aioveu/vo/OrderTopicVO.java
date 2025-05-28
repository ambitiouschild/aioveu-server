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
public class OrderTopicVO {

    private String topicName;

    private String orderId;

    private String orderDetailId;

    private String productName;

    private String address;

    private String storeName;

    private Date createDate;

    private String rewardProduct;

    private BigDecimal reward;

}
