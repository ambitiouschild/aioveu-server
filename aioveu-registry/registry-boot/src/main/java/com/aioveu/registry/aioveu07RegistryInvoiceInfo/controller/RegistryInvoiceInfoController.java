package com.aioveu.registry.aioveu07RegistryInvoiceInfo.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.form.RegistryInvoiceInfoForm;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.query.RegistryInvoiceInfoQuery;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.vo.RegistryInvoiceInfoVo;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.service.RegistryInvoiceInfoService;
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
 * @ClassName: RegistryInvoiceInfoController
 * @Description TODO 发票信息前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:49
 * @Version 1.0
 **/
@Tag(name = "发票信息接口")
@RestController
@RequestMapping("/api/v1/registry-invoice-info")
@RequiredArgsConstructor
public class RegistryInvoiceInfoController {

    private final RegistryInvoiceInfoService registryInvoiceInfoService;

    @Operation(summary = "发票信息分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryInvoiceInfo:registry-invoice-info:list')")
    public PageResult<RegistryInvoiceInfoVo> getRegistryInvoiceInfoPage(RegistryInvoiceInfoQuery queryParams ) {
        IPage<RegistryInvoiceInfoVo> result = registryInvoiceInfoService.getRegistryInvoiceInfoPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增发票信息")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryInvoiceInfo:registry-invoice-info:create')")
    public Result<Void> saveRegistryInvoiceInfo(@RequestBody @Valid RegistryInvoiceInfoForm formData ) {
        boolean result = registryInvoiceInfoService.saveRegistryInvoiceInfo(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取发票信息表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryInvoiceInfo:registry-invoice-info:update')")
    public Result<RegistryInvoiceInfoForm> getRegistryInvoiceInfoForm(
            @Parameter(description = "发票信息ID") @PathVariable Long id
    ) {
        RegistryInvoiceInfoForm formData = registryInvoiceInfoService.getRegistryInvoiceInfoFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改发票信息")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryInvoiceInfo:registry-invoice-info:update')")
    public Result<Void> updateRegistryInvoiceInfo(
            @Parameter(description = "发票信息ID") @PathVariable Long id,
            @RequestBody @Validated RegistryInvoiceInfoForm formData
    ) {
        boolean result = registryInvoiceInfoService.updateRegistryInvoiceInfo(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除发票信息")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryInvoiceInfo:registry-invoice-info:delete')")
    public Result<Void> deleteRegistryInvoiceInfos(
            @Parameter(description = "发票信息ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = registryInvoiceInfoService.deleteRegistryInvoiceInfos(ids);
        return Result.judge(result);
    }
}
