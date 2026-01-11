package com.aioveu.pms.aioveu07SpuAttribute.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: PmsSpuAttributeVO
 * @Description TODO 商品类型（属性/规格）视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 22:04
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "商品类型（属性/规格）视图对象")
public class PmsSpuAttributeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "产品ID")
    private Long spuId;
    @Schema(description = "属性ID")
    private Long attributeId;
    @Schema(description = "属性名称")
    private String name;
    @Schema(description = "属性值")
    private String value;
    @Schema(description = "类型(1:规格;2:属性;)")
    private Integer type;
    @Schema(description = "规格图片")
    private String picUrl;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
