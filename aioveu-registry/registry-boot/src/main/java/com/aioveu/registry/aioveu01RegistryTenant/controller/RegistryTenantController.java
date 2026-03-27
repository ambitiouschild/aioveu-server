package com.aioveu.registry.aioveu01RegistryTenant.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.registry.aioveu01RegistryTenant.model.form.RegistryTenantForm;
import com.aioveu.registry.aioveu01RegistryTenant.model.query.RegistryTenantQuery;
import com.aioveu.registry.aioveu01RegistryTenant.model.vo.RegistryTenantVo;
import com.aioveu.registry.aioveu01RegistryTenant.service.RegistryTenantService;
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
 * @ClassName: RegistryTenantController
 * @Description TODO 租户注册小程序基本信息前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 16:34
 * @Version 1.0
 **/
@Tag(name = "租户注册小程序基本信息接口")
@RestController
@RequestMapping("/api/v1/registry-tenant")
@RequiredArgsConstructor
public class RegistryTenantController {

    private final RegistryTenantService registryTenantService;

    @Operation(summary = "租户注册小程序基本信息分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryTenant:registry-tenant:list')")
    public PageResult<RegistryTenantVo> getRegistryTenantPage(RegistryTenantQuery queryParams ) {
        IPage<RegistryTenantVo> result = registryTenantService.getRegistryTenantPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增租户注册小程序基本信息")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryTenant:registry-tenant:create')")
    public Result<Void> saveRegistryTenant(@RequestBody @Valid RegistryTenantForm formData ) {
        boolean result = registryTenantService.saveRegistryTenant(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取租户注册小程序基本信息表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryTenant:registry-tenant:update')")
    public Result<RegistryTenantForm> getRegistryTenantForm(
            @Parameter(description = "租户注册小程序基本信息ID") @PathVariable Long id
    ) {
        RegistryTenantForm formData = registryTenantService.getRegistryTenantFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改租户注册小程序基本信息")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryTenant:registry-tenant:update')")
    public Result<Void> updateRegistryTenant(
            @Parameter(description = "租户注册小程序基本信息ID") @PathVariable Long id,
            @RequestBody @Validated RegistryTenantForm formData
    ) {
        boolean result = registryTenantService.updateRegistryTenant(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除租户注册小程序基本信息")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryTenant:registry-tenant:delete')")
    public Result<Void> deleteRegistryTenants(
            @Parameter(description = "租户注册小程序基本信息ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = registryTenantService.deleteRegistryTenants(ids);
        return Result.judge(result);
    }
}
