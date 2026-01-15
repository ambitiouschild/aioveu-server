package com.aioveu.pms.aioveu06Spu.model.vo;

import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Schema( description = "【管理端】商品视图对象")
public class PmsSpuVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品类型ID")
    private Long categoryId;

    @Schema(description = "商品品牌ID")
    private Long brandId;

    @Schema(description = "原价【起】")
    private Long originPrice;

    @Schema(description = "现价【起】")
    private Long price;

    @Schema(description = "销量")
    private Integer sales;

    @Schema(description = "商品主图")
    private String picUrl;

    @Schema(description = "商品图册")
    private String[] album;

    //您查询结果中出现 <<BLOB>>是因为数据库中的某些字段存储的是二进制大对象（Binary Large Object），而查询工具或客户端在显示时将其简化为 <<BLOB>>而不是实际内容

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "商品简介")
    private String description;

    @Schema(description = "商品详情")
    private String detail;

    @Schema(description = "商品状态(0:下架 1:上架)")
    private Integer status;

    @Schema(description = "分类名")
    private String categoryName;

    @Schema(description = "品牌名")
    private String brandName;

    @Schema(description = "库存列表")
    private List<PmsSku> skuList;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
