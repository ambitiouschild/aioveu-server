package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/10/3 0003 23:52
 */
@Data
public class TopicCheckItemVO extends IdNameVO  {

    private String orderId;

    private String orderDetailId;

    private String storeGift;

    private BigDecimal reward;

    private String introduce;

    private String image;

}
