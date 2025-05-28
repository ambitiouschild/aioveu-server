package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description 店铺会员卡
 * @author: 雒世松
 * @date: 2025/11/23 17:06
 */
@Data
public class StoreVipCardVO {

    private Long id;

    private String image;

    private String name;

    /**
     * 最低价格
     */
    private BigDecimal lowerPrice;

    /**
     * 原价
     */
    private BigDecimal price;

    private Integer status;

    private Integer limitNumber;

    /**
     * 报名人数
     */
    private Integer enrollNumber;

    private String store;

    /**
     * 有效期
     */
    private String validDay;

}
