package com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: ManagerMenuCategoryItemQuery
 * @Description TODO 管理系统工作台菜单项（多租户支持）分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:25
 * @Version 1.0
 **/
@Schema(description ="管理系统工作台菜单项（多租户支持）查询对象")
@Getter
@Setter
public class ManagerMenuCategoryItemQuery extends BasePageQuery {

    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "分类ID")
    private Long categoryId;
    @Schema(description = "菜单标题")
    private String title;
    @Schema(description = "路由名称")
    private String pathName;
    @Schema(description = "权限标识")
    private String permission;
    @Schema(description = "排序序号")
    private Integer sort;
}
