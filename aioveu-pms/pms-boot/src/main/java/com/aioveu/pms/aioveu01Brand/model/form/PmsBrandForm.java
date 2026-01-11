package com.aioveu.pms.aioveu01Brand.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: PmsBrandForm
 * @Description TODO  商品品牌表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 19:00
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "商品品牌表单对象")
public class PmsBrandForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "品牌名称")
    @NotBlank(message = "品牌名称不能为空")
    @Size(max=64, message="品牌名称长度不能超过64个字符")
    private String name;

    @Schema(description = "LOGO图片")
    @NotBlank(message = "LOGO图片不能为空")
    @Size(max=255, message="LOGO图片长度不能超过255个字符")
    private String logoUrl;

    @Schema(description = "排序")
    private Integer sort;
}
