package com.aioveu.pms.aioveu07SpuAttribute.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品类型（属性/规格）表单对象
 * @Date  2026/1/11 22:02
 * @Param
 * @return
 **/

@Data
@Schema(description = "商品类型（属性/规格）表单对象")
public class PmsSpuAttributeForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private String id;


    @Schema(description = "产品ID")
    @NotNull(message = "产品ID不能为空")
    private Long spuId;

    @Schema(description = "属性ID")
    @NotNull(message = "属性ID不能为空")
    private Long  attributeId;

    @Schema(description = "属性名称")
    @NotBlank(message = "属性名称不能为空")
    @Size(max=64, message="属性名称长度不能超过64个字符")
    private String name;

    @Schema(description = "属性值")
    @NotBlank(message = "属性值不能为空")
    @Size(max=128, message="属性值长度不能超过128个字符")
    private String value;

    @Schema(description = "类型(1:规格;2:属性;)")
    @NotNull(message = "类型(1:规格;2:属性;)不能为空")
    private Integer type;

    @Schema(description = "规格图片")
    @Size(max=255, message="规格图片长度不能超过255个字符")
    private String picUrl;

}
