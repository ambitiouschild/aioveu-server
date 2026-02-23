package com.aioveu.tenant.aioveu01Tenant.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: TenantPlanPageVO
 * @Description TODO 租户套餐分页对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:58
 * @Version 1.0
 **/
@Data
@Schema(description = "租户套餐分页对象")
public class TenantPlanPageVO {

    @Schema(description = "套餐ID")
    private Long id;

    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "套餐编码")
    private String code;

    @Schema(description = "状态(1-启用 0-停用)")
    private Integer status;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
