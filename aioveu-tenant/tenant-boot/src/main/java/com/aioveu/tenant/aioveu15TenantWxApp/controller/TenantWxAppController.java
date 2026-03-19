package com.aioveu.tenant.aioveu15TenantWxApp.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.tenant.aioveu15TenantWxApp.model.form.TenantWxAppForm;
import com.aioveu.tenant.aioveu15TenantWxApp.model.query.TenantWxAppQuery;
import com.aioveu.tenant.aioveu15TenantWxApp.model.vo.TenantWxAppVo;
import com.aioveu.tenant.aioveu15TenantWxApp.service.TenantWxAppService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: TenantWxAppController
 * @Description TODO 租户与微信小程序关联前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 17:09
 * @Version 1.0
 **/
@Tag(name = "租户与微信小程序关联接口")
@RestController
@RequestMapping("/api/v1/tenant-wx-app")
@RequiredArgsConstructor
public class TenantWxAppController {

    private final TenantWxAppService tenantWxAppService;

    @Operation(summary = "租户与微信小程序关联分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallTenantWxApp:tenant-wx-app:list')")
    public PageResult<TenantWxAppVo> getTenantWxAppPage(TenantWxAppQuery queryParams ) {
        IPage<TenantWxAppVo> result = tenantWxAppService.getTenantWxAppPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增租户与微信小程序关联")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallTenantWxApp:tenant-wx-app:create')")
    public Result<Void> saveTenantWxApp(@RequestBody @Valid TenantWxAppForm formData ) {
        boolean result = tenantWxAppService.saveTenantWxApp(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取租户与微信小程序关联表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallTenantWxApp:tenant-wx-app:update')")
    public Result<TenantWxAppForm> getTenantWxAppForm(
            @Parameter(description = "租户与微信小程序关联ID") @PathVariable Long id
    ) {
        TenantWxAppForm formData = tenantWxAppService.getTenantWxAppFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改租户与微信小程序关联")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallTenantWxApp:tenant-wx-app:update')")
    public Result<Void> updateTenantWxApp(
            @Parameter(description = "租户与微信小程序关联ID") @PathVariable Long id,
            @RequestBody @Validated TenantWxAppForm formData
    ) {
        boolean result = tenantWxAppService.updateTenantWxApp(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除租户与微信小程序关联")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallTenantWxApp:tenant-wx-app:delete')")
    public Result<Void> deleteTenantWxApps(
            @Parameter(description = "租户与微信小程序关联ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = tenantWxAppService.deleteTenantWxApps(ids);
        return Result.judge(result);
    }
}
