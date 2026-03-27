package com.aioveu.registry.aioveu04RegistryAdministratorInfo.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.form.RegistryAdministratorInfoForm;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.query.RegistryAdministratorInfoQuery;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.vo.RegistryAdministratorInfoVo;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.service.RegistryAdministratorInfoService;
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
 * @ClassName: RegistryAdministratorInfoController
 * @Description TODO 管理员信息前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:51
 * @Version 1.0
 **/
@Tag(name = "管理员信息接口")
@RestController
@RequestMapping("/api/v1/registry-administrator-info")
@RequiredArgsConstructor
public class RegistryAdministratorInfoController {

    private final RegistryAdministratorInfoService registryAdministratorInfoService;

    @Operation(summary = "管理员信息分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAdministratorInfo:registry-administrator-info:list')")
    public PageResult<RegistryAdministratorInfoVo> getRegistryAdministratorInfoPage(RegistryAdministratorInfoQuery queryParams ) {
        IPage<RegistryAdministratorInfoVo> result = registryAdministratorInfoService.getRegistryAdministratorInfoPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增管理员信息")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAdministratorInfo:registry-administrator-info:create')")
    public Result<Void> saveRegistryAdministratorInfo(@RequestBody @Valid RegistryAdministratorInfoForm formData ) {
        boolean result = registryAdministratorInfoService.saveRegistryAdministratorInfo(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取管理员信息表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAdministratorInfo:registry-administrator-info:update')")
    public Result<RegistryAdministratorInfoForm> getRegistryAdministratorInfoForm(
            @Parameter(description = "管理员信息ID") @PathVariable Long id
    ) {
        RegistryAdministratorInfoForm formData = registryAdministratorInfoService.getRegistryAdministratorInfoFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改管理员信息")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAdministratorInfo:registry-administrator-info:update')")
    public Result<Void> updateRegistryAdministratorInfo(
            @Parameter(description = "管理员信息ID") @PathVariable Long id,
            @RequestBody @Validated RegistryAdministratorInfoForm formData
    ) {
        boolean result = registryAdministratorInfoService.updateRegistryAdministratorInfo(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除管理员信息")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAdministratorInfo:registry-administrator-info:delete')")
    public Result<Void> deleteRegistryAdministratorInfos(
            @Parameter(description = "管理员信息ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = registryAdministratorInfoService.deleteRegistryAdministratorInfos(ids);
        return Result.judge(result);
    }
}
