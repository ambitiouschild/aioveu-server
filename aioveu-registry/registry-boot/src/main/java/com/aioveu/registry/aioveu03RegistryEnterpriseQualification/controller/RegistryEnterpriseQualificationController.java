package com.aioveu.registry.aioveu03RegistryEnterpriseQualification.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.form.RegistryEnterpriseQualificationForm;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.query.RegistryEnterpriseQualificationQuery;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.vo.RegistryEnterpriseQualificationVo;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.service.RegistryEnterpriseQualificationService;
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
 * @ClassName: RegistryEnterpriseQualificationController
 * @Description TODO 企业资质前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:37
 * @Version 1.0
 **/
@Tag(name = "企业资质接口")
@RestController
@RequestMapping("/api/v1/registry-enterprise-qualification")
@RequiredArgsConstructor
public class RegistryEnterpriseQualificationController {

    private final RegistryEnterpriseQualificationService registryEnterpriseQualificationService;

    @Operation(summary = "企业资质分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryEnterpriseQualification:registry-enterprise-qualification:list')")
    public PageResult<RegistryEnterpriseQualificationVo> getRegistryEnterpriseQualificationPage(RegistryEnterpriseQualificationQuery queryParams ) {
        IPage<RegistryEnterpriseQualificationVo> result = registryEnterpriseQualificationService.getRegistryEnterpriseQualificationPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增企业资质")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryEnterpriseQualification:registry-enterprise-qualification:create')")
    public Result<Void> saveRegistryEnterpriseQualification(@RequestBody @Valid RegistryEnterpriseQualificationForm formData ) {
        boolean result = registryEnterpriseQualificationService.saveRegistryEnterpriseQualification(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取企业资质表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryEnterpriseQualification:registry-enterprise-qualification:update')")
    public Result<RegistryEnterpriseQualificationForm> getRegistryEnterpriseQualificationForm(
            @Parameter(description = "企业资质ID") @PathVariable Long id
    ) {
        RegistryEnterpriseQualificationForm formData = registryEnterpriseQualificationService.getRegistryEnterpriseQualificationFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改企业资质")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryEnterpriseQualification:registry-enterprise-qualification:update')")
    public Result<Void> updateRegistryEnterpriseQualification(
            @Parameter(description = "企业资质ID") @PathVariable Long id,
            @RequestBody @Validated RegistryEnterpriseQualificationForm formData
    ) {
        boolean result = registryEnterpriseQualificationService.updateRegistryEnterpriseQualification(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除企业资质")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryEnterpriseQualification:registry-enterprise-qualification:delete')")
    public Result<Void> deleteRegistryEnterpriseQualifications(
            @Parameter(description = "企业资质ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = registryEnterpriseQualificationService.deleteRegistryEnterpriseQualifications(ids);
        return Result.judge(result);
    }
}
