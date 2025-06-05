package com.aioveu.oms.model.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * @Description: TODO 购物车商品项
 * @Author: 雒世松
 * @Date: 2025/6/5 18:09
 * @param
 * @return:
 **/

@Data
public class CartItemDto implements Serializable {

    /**
     * 商品库存ID
     */
    private Long skuId;

    /**
     * 商品数量
     */
    private Integer count;

    /**
     * 是否选中
     */
    private Boolean checked;

}
