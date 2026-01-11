package com.aioveu.pms.aioveu03CategoryAttribute.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品分类类型（规格，属性）表单对象
 * @Date  2026/1/11 19:40
 * @Param
 * @return
 **/

@Data
@Schema(description = "商品类型（规格，属性）表单对象")
public class PmsCategoryAttributeForm {

    @Serial
    private static final long serialVersionUID = 1L;


    @Schema(description = "主键")
    private Long id;

    @Schema(description="分类ID")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;


    @Schema(description = "属性名称")
    @NotBlank(message = "属性名称不能为空")
    @Size(max=64, message="属性名称长度不能超过64个字符")
    private String name;

    @Schema(description="属性类型（1：规格；2：属性）")
    @NotNull(message = "类型(1:规格;2:属性;)不能为空")
    private Integer type;

    @Schema(description="属性集合")
    @NotEmpty
    private List<Attribute> attributes;

    @Data
    public static class Attribute {

        @Schema(description="属性ID")
        private Long id;

        @Schema(description="属性名称")
        @NotBlank
        private String name;
    }

}
