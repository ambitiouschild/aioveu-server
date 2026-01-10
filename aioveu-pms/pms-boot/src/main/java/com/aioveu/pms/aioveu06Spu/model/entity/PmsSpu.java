package com.aioveu.pms.aioveu06Spu.model.entity;

import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import com.baomidou.mybatisplus.annotation.TableField;
import com.aioveu.common.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: TODO 商品实体
 * @Author: 雒世松
 * @Date: 2025/6/5 18:31
 * @param
 * @return:
 **/

@Data
@Accessors(chain = true)
public class PmsSpu extends BaseEntity {

//    @TableId(type = IdType.AUTO)
//    private Long id;

    private String name;

    private Long categoryId;

    private Long brandId;

    private Long originPrice;

    private Long price;

    private Integer sales;

    private String picUrl;

    private String[] album;

    private String unit;

    private String description;

    private String detail;

    private Integer status;

    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private String brandName;

    @TableField(exist = false)
    private List<PmsSku> skuList;
}
