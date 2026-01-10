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

    private Long id;

    private String name;

    private Long categoryId;

    private Long brandId;

    private Long originPrice;

    private Long price;

    @Schema(description="商品主图")
    private String picUrl;

    @Schema(description="商品副图")
    private String[] subPicUrls;

    private String description;

    private String detail;

    private List<PmsSpuAttribute> attrList;

    private List<PmsSpuAttribute> specList;

    private List<PmsSku> skuList;

}
