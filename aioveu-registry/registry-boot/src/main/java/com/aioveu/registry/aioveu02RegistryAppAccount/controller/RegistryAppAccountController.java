package com.aioveu.registry.aioveu02RegistryAppAccount.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.form.RegistryAppAccountForm;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.query.RegistryAppAccountQuery;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.vo.RegistryAppAccountVo;
import com.aioveu.registry.aioveu02RegistryAppAccount.service.RegistryAppAccountService;
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
 * @ClassName: RegistryAppAccountController
 * @Description TODO 小程序账号前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:08
 * @Version 1.0
 **/
@Tag(name = "小程序账号接口")
@RestController
@RequestMapping("/api/v1/registry-app-account")
@RequiredArgsConstructor
public class RegistryAppAccountController {

    private final RegistryAppAccountService registryAppAccountService;

    @Operation(summary = "小程序账号分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAppAccount:registry-app-account:list')")
    public PageResult<RegistryAppAccountVo> getRegistryAppAccountPage(RegistryAppAccountQuery queryParams ) {
        IPage<RegistryAppAccountVo> result = registryAppAccountService.getRegistryAppAccountPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增小程序账号")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAppAccount:registry-app-account:create')")
    public Result<Void> saveRegistryAppAccount(@RequestBody @Valid RegistryAppAccountForm formData ) {
        boolean result = registryAppAccountService.saveRegistryAppAccount(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取小程序账号表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAppAccount:registry-app-account:update')")
    public Result<RegistryAppAccountForm> getRegistryAppAccountForm(
            @Parameter(description = "小程序账号ID") @PathVariable Long id
    ) {
        RegistryAppAccountForm formData = registryAppAccountService.getRegistryAppAccountFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改小程序账号")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAppAccount:registry-app-account:update')")
    public Result<Void> updateRegistryAppAccount(
            @Parameter(description = "小程序账号ID") @PathVariable Long id,
            @RequestBody @Validated RegistryAppAccountForm formData
    ) {
        boolean result = registryAppAccountService.updateRegistryAppAccount(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除小程序账号")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAppAccount:registry-app-account:delete')")
    public Result<Void> deleteRegistryAppAccounts(
            @Parameter(description = "小程序账号ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = registryAppAccountService.deleteRegistryAppAccounts(ids);
        return Result.judge(result);
    }
}
