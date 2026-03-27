package com.aioveu.registry.aioveu05RegistryCertification.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.registry.aioveu05RegistryCertification.model.form.RegistryCertificationForm;
import com.aioveu.registry.aioveu05RegistryCertification.model.query.RegistryCertificationQuery;
import com.aioveu.registry.aioveu05RegistryCertification.model.vo.RegistryCertificationVo;
import com.aioveu.registry.aioveu05RegistryCertification.service.RegistryCertificationService;
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
 * @ClassName: RegistryCertificationController
 * @Description TODO 认证记录前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:20
 * @Version 1.0
 **/
@Tag(name = "认证记录接口")
@RestController
@RequestMapping("/api/v1/registry-certification")
@RequiredArgsConstructor
public class RegistryCertificationController {

    private final RegistryCertificationService registryCertificationService;

    @Operation(summary = "认证记录分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryCertification:registry-certification:list')")
    public PageResult<RegistryCertificationVo> getRegistryCertificationPage(RegistryCertificationQuery queryParams ) {
        IPage<RegistryCertificationVo> result = registryCertificationService.getRegistryCertificationPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增认证记录")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryCertification:registry-certification:create')")
    public Result<Void> saveRegistryCertification(@RequestBody @Valid RegistryCertificationForm formData ) {
        boolean result = registryCertificationService.saveRegistryCertification(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取认证记录表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryCertification:registry-certification:update')")
    public Result<RegistryCertificationForm> getRegistryCertificationForm(
            @Parameter(description = "认证记录ID") @PathVariable Long id
    ) {
        RegistryCertificationForm formData = registryCertificationService.getRegistryCertificationFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改认证记录")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryCertification:registry-certification:update')")
    public Result<Void> updateRegistryCertification(
            @Parameter(description = "认证记录ID") @PathVariable Long id,
            @RequestBody @Validated RegistryCertificationForm formData
    ) {
        boolean result = registryCertificationService.updateRegistryCertification(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除认证记录")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryCertification:registry-certification:delete')")
    public Result<Void> deleteRegistryCertifications(
            @Parameter(description = "认证记录ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = registryCertificationService.deleteRegistryCertifications(ids);
        return Result.judge(result);
    }
}
