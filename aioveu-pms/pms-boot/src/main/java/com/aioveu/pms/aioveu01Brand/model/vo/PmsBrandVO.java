package com.aioveu.pms.aioveu01Brand.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: PmsBrandVO
 * @Description TODO  商品品牌视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 19:02
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "商品品牌视图对象")
public class PmsBrandVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "品牌名称")
    private String name;
    @Schema(description = "LOGO图片")
    private String logoUrl;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
