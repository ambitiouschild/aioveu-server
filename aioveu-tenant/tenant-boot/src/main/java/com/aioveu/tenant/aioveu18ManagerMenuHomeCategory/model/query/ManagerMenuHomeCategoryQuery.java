package com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: ManagerMenuHomeCategoryQuery
 * @Description TODO 管理端app首页分类配置分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 13:38
 * @Version 1.0
 **/
@Schema(description ="管理端app首页分类配置查询对象")
@Getter
@Setter
public class ManagerMenuHomeCategoryQuery extends BasePageQuery {

    @Schema(description = "管理端app分类ID")
    private Long categoryId;
    @Schema(description = "管理端app首页显示的图标URL")
    private String homeIcon;
    @Schema(description = "管理端app首页显示名称")
    private String homeName;
    @Schema(description = "状态：0-隐藏，1-显示")
    private Integer status;
}
