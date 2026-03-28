package com.aioveu.pay.aioveu02PayConfig.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu02PayConfig.model.form.PayConfigForm;
import com.aioveu.pay.aioveu02PayConfig.model.query.PayConfigQuery;
import com.aioveu.pay.aioveu02PayConfig.model.vo.PayConfigVo;
import com.aioveu.pay.aioveu02PayConfig.service.PayConfigService;
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
 * @ClassName: PayConfigController
 * @Description TODO 支付配置主表前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:09
 * @Version 1.0
 **/
@Tag(name = "支付配置主表接口")
@RestController
@RequestMapping("/api/v1/pay-config")
@RequiredArgsConstructor
public class PayConfigController {

    private final PayConfigService payConfigService;

    @Operation(summary = "支付配置主表分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfig:pay-config:list')")
    public PageResult<PayConfigVo> getPayConfigPage(PayConfigQuery queryParams ) {
        IPage<PayConfigVo> result = payConfigService.getPayConfigPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增支付配置主表")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfig:pay-config:create')")
    public Result<Void> savePayConfig(@RequestBody @Valid PayConfigForm formData ) {
        boolean result = payConfigService.savePayConfig(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取支付配置主表表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfig:pay-config:update')")
    public Result<PayConfigForm> getPayConfigForm(
            @Parameter(description = "支付配置主表ID") @PathVariable Long id
    ) {
        PayConfigForm formData = payConfigService.getPayConfigFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改支付配置主表")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfig:pay-config:update')")
    public Result<Void> updatePayConfig(
            @Parameter(description = "支付配置主表ID") @PathVariable Long id,
            @RequestBody @Validated PayConfigForm formData
    ) {
        boolean result = payConfigService.updatePayConfig(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除支付配置主表")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfig:pay-config:delete')")
    public Result<Void> deletePayConfigs(
            @Parameter(description = "支付配置主表ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payConfigService.deletePayConfigs(ids);
        return Result.judge(result);
    }
}
