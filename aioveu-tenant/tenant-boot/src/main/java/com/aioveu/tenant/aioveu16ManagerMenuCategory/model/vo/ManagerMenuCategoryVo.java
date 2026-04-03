package com.aioveu.tenant.aioveu16ManagerMenuCategory.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: ManagerMenuCategoryVo
 * @Description TODO 管理端菜单分类（多租户）视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:10
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "管理端菜单分类（多租户）视图对象")
public class ManagerMenuCategoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "分类标题")
    private String title;
    @Schema(description = "分类图标")
    private String icon;
    @Schema(description = "分类描述")
    private String description;
    @Schema(description = "排序序号")
    private Integer sort;
    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;
    @Schema(description = "分类类型：workbench-工作台，sidebar-侧边栏")
    private String type;
    @Schema(description = "可见范围：0-所有用户，1-租户管理员，2-普通用户")
    private Integer visibleRange;
    @Schema(description = "是否可编辑：0-系统内置，1-可编辑")
    private Integer isEditable;
    @Schema(description = "创建人ID")
    private Long createBy;
    @Schema(description = "更新人ID")
    private Long updateBy;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
