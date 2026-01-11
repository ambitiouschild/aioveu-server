package com.aioveu.pms.aioveu06Spu.model.entity;

import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import com.baomidou.mybatisplus.annotation.TableField;
import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: TODO 商品实体对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:31
 * @param
 * @return:
 **/

@Data
@Accessors(chain = true)
@TableName("pms_spu")
public class PmsSpu extends BaseEntity {

//    @TableId(type = IdType.AUTO)
//    private Long id;

    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品类型ID
     */
    private Long categoryId;

    /**
     * 商品品牌ID
     */
    private Long brandId;

    /**
     * 原价【起】
     */
    private Long originPrice;

    /**
     * 现价【起】
     */
    private Long price;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 商品主图
     */
    private String picUrl;

    /**
     * 商品图册
     */
    private String[] album;

    /**
     * 单位
     */
    private String unit;

    /**
     * 商品简介
     */
    private String description;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 商品状态(0:下架 1:上架)
     */
    private Integer status;

    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private String brandName;

    @TableField(exist = false)
    private List<PmsSku> skuList;
}
