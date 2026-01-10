package com.aioveu.pms.aioveu06Spu.model.vo;

import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: TODO 【管理端】商品分页视图对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:32
 * @param
 * @return:
 **/

@Data
@Accessors(chain = true)
public class PmsSpuPageVO {

    private Long id;

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

    private String categoryName;

    private String brandName;

    private List<PmsSku> skuList;
}
