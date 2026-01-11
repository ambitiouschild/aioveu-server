package com.aioveu.pms.aioveu03CategoryAttribute.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: PmsCategoryAttributeVO
 * @Description TODO 商品分类类型（规格，属性）视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 19:46
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "商品分类类型（规格，属性）视图对象")
public class PmsCategoryAttributeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "分类ID")
    private Long categoryId;
    @Schema(description = "属性名称")
    private String name;
    @Schema(description = "类型(1:规格;2:属性;)")
    private Integer type;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}


