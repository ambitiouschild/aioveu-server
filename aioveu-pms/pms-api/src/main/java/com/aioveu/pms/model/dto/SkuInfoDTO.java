package com.aioveu.pms.model.dto;

import lombok.Data;

/**
 * @Description: TODO 商品库存信息DTO
 * 用于表示商品的库存信息.
 * @Author: 雒世松
 * @Date: 2025/6/5 18:22
 * @param
 * @return:
 **/


@Data
public class SkuInfoDTO {
    /**
     * SKU的唯一标识符
     */
    private Long id;
    /**
     * SKU 编号
     */
    private String skuSn;
    /**
     * SKU 名称
     */
    private String skuName;
    /**
     * SKU 图片地址
     */
    private String picUrl;
    /**
     * SKU 价格
     */
    private Long price;
    /**
     * SKU 库存数量
     */
    private Integer stock;
    /**
     * 所属SPU的名称
     */
    private String spuName;
}
