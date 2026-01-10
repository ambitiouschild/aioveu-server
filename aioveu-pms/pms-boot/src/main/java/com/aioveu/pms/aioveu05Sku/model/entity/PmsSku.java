package com.aioveu.pms.aioveu05Sku.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.aioveu.common.base.BaseEntity;
import lombok.Data;

/**
 * @Description: TODO 商品库存单元
 * @Author: 雒世松
 * @Date: 2025/6/5 18:31
 * @param
 * @return:
 **/

@Data
public class PmsSku extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 库存单元编号
     */
    private String skuSn;

    /**
     * SKU 名称
     */
    private String name;

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * 规格ID，多个使用英文逗号(,)分割
     */
    private String specIds;

    /**
     * 商品价格(单位：分)
     */
    private Long price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 锁定库存数量
     */
    private Integer lockedStock;

    /**
     * 商品图片地址
     */
    private String picUrl;
}
