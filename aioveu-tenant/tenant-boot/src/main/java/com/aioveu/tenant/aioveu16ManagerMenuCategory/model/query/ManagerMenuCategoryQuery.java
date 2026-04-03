package com.aioveu.tenant.aioveu16ManagerMenuCategory.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: ManagerMenuCategoryQuery
 * @Description TODO 管理端菜单分类（多租户）分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:10
 * @Version 1.0
 **/
@Schema(description ="管理端菜单分类（多租户）查询对象")
@Getter
@Setter
public class ManagerMenuCategoryQuery extends BasePageQuery {

    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "分类标题")
    private String title;
    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;
    @Schema(description = "分类类型：workbench-工作台，sidebar-侧边栏")
    private String type;
    @Schema(description = "是否可编辑：0-系统内置，1-可编辑")
    private Integer isEditable;
}
