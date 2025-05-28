package com.aioveu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description <h2>商品信息</h2>
 * @author: 雒世松
 * @date: 2025/1/30 0030 22:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfo {

    /**
     * 商品类型:
     */
    private Integer type;

    /**
     * 商品原始价格
     */
    private Double price;

    /**
     * 商品数量
     */
    private Integer count;

    //TODO 商品名称，使用信息...

}
