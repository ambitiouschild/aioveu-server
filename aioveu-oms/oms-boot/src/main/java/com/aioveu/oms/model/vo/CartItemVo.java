package com.aioveu.oms.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @Description: TODO 购物车商品项
 * @Author: 雒世松
 * @Date: 2025/6/5 18:10
 * @param
 * @return:
 **/

@Data
public class CartItemVo implements Serializable {

    /**
     * 商品库存ID
     */
    private Long skuId;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 规格名称
     */
    private String skuName;

    /**
     * 商品图片
     */
    private String imageUrl;

    /**
     * 加购数量·
     */
    private Integer count;

    /**
     * 商品价格
     */
    private Long price;

    /**
     * 是否选中
     */
    private Boolean checked;

    /**
     * 商品库存
     */
    private Integer stock;

}
