package com.aioveu.pms.aioveu02Category.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: PmsCategoryForm
 * @Description TODO  商品分类表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 17:30
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "商品分类表单对象")
public class PmsCategoryForm  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "商品分类名称")
    @NotBlank(message = "商品分类名称不能为空")
    @Size(max=64, message="商品分类名称长度不能超过64个字符")
    private String name;

    @Schema(description = "父级ID")
    @NotNull(message = "父级ID不能为空")
    private Long parentId;

    @Schema(description = "层级")
    private Integer level;

    @Schema(description = "图标地址")
    @Size(max=255, message="图标地址长度不能超过255个字符")
    private String iconUrl;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "显示状态:( 0:隐藏 1:显示)")
    private Integer visible;
}
