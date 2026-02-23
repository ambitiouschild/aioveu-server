package com.aioveu.tenant.aioveu01Tenant.controller;

import com.aioveu.common.model.Option;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.tenant.aioveu01Tenant.model.form.TenantPlanForm;
import com.aioveu.tenant.aioveu01Tenant.model.query.TenantPlanQuery;
import com.aioveu.tenant.aioveu01Tenant.model.vo.TenantPlanPageVO;
import com.aioveu.tenant.aioveu01Tenant.service.TenantPlanService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: TenantPlanController
 * @Description TODO 租户套餐控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 14:49
 * @Version 1.0
 **/
@Tag(name = "01.租户套餐接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tenant-plans")
public class TenantPlanController {

    private final TenantPlanService tenantPlanService;

    @Operation(summary = "租户套餐分页列表")
    @GetMapping
    @PreAuthorize("@ss.hasPerm('sys:tenant-plan:list')")
    public PageResult<TenantPlanPageVO> getTenantPlanPage(@ParameterObject TenantPlanQuery queryParams) {
        IPage<TenantPlanPageVO> result = tenantPlanService.getTenantPlanPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "租户套餐下拉列表")
    @GetMapping("/options")
    public Result<List<Option<Long>>> listTenantPlanOptions() {
        List<Option<Long>> options = tenantPlanService.listTenantPlanOptions();
        return Result.success(options);
    }

    @Operation(summary = "获取租户套餐表单数据")
    @GetMapping("/{planId}/form")
    @PreAuthorize("@ss.hasPerm('sys:tenant-plan:update')")
    public Result<TenantPlanForm> getTenantPlanForm(
            @Parameter(description = "套餐ID") @PathVariable Long planId
    ) {
        TenantPlanForm formData = tenantPlanService.getTenantPlanForm(planId);
        return Result.success(formData);
    }

    @Operation(summary = "新增租户套餐")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:tenant-plan:create')")
    public Result<?> saveTenantPlan(@Valid @RequestBody TenantPlanForm formData) {
        boolean result = tenantPlanService.saveTenantPlan(formData);
        return Result.judge(result);
    }

    @Operation(summary = "修改租户套餐")
    @PutMapping("/{planId}")
    @PreAuthorize("@ss.hasPerm('sys:tenant-plan:update')")
    public Result<?> updateTenantPlan(
            @Parameter(description = "套餐ID") @PathVariable Long planId,
            @Valid @RequestBody TenantPlanForm formData
    ) {
        boolean result = tenantPlanService.updateTenantPlan(planId, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除租户套餐")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:tenant-plan:delete')")
    public Result<Void> deleteTenantPlans(
            @Parameter(description = "套餐ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        tenantPlanService.deleteTenantPlans(ids);
        return Result.success();
    }

    @Operation(summary = "获取套餐菜单ID集合")
    @GetMapping("/{planId}/menuIds")
    @PreAuthorize("@ss.hasPerm('sys:tenant-plan:assign')")
    public Result<List<Long>> getTenantPlanMenuIds(
            @Parameter(description = "套餐ID") @PathVariable Long planId
    ) {
        List<Long> menuIds = tenantPlanService.getTenantPlanMenuIds(planId);
        return Result.success(menuIds);
    }

    @Operation(summary = "更新套餐菜单")
    @PutMapping("/{planId}/menus")
    @PreAuthorize("@ss.hasPerm('sys:tenant-plan:assign')")
    public Result<Void> updateTenantPlanMenus(
            @Parameter(description = "套餐ID") @PathVariable Long planId,
            @RequestBody List<Long> menuIds
    ) {
        tenantPlanService.updateTenantPlanMenus(planId, menuIds);
        return Result.success();
    }
}
