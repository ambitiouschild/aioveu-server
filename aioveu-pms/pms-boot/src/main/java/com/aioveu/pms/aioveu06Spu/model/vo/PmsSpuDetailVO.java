package com.aioveu.pms.aioveu06Spu.model.vo;

import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import com.aioveu.pms.aioveu07SpuAttribute.model.entity.PmsSpuAttribute;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Description: TODO 【管理端】商品详情视图对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:32
 * @param
 * @return:
 **/

@Data
@Schema(description = "商品详情视图对象")
public class PmsSpuDetailVO {

    /** 主键 */
    private Long id;

    /** 商品名称 */
    private String name;

    /** 商品类型ID */
    private Long categoryId;

    /** 商品品牌ID */
    private Long brandId;

    /** 原价【起】 */
    private Long originPrice;

    /** 现价【起】 */
    private Long price;

    /** 销量 */
    private Long sales;


    @Schema(description="商品主图")
    private String picUrl;

    @Schema(description="商品副图")
    private String[] album;


    /** 商品简介 */
    private String description;

    /** 商品详情 */
    private String detail;


    // 商品属性列表
    private List<PmsSpuAttribute> attrList;

    // 商品规格列表
    private List<PmsSpuAttribute> specList;

    // 商品SKU列表
    private List<PmsSku> skuList;

    /** 商品详情 */
    private String brandName;

}
