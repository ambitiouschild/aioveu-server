package com.aioveu.pay.aioveu03PayChannelConfig.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu03PayChannelConfig.model.form.PayChannelConfigForm;
import com.aioveu.pay.aioveu03PayChannelConfig.model.query.PayChannelConfigQuery;
import com.aioveu.pay.aioveu03PayChannelConfig.model.vo.PayChannelConfigVO;
import com.aioveu.pay.aioveu03PayChannelConfig.service.PayChannelConfigService;
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
 * @ClassName: PayChannelConfigController
 * @Description TODO 支付渠道配置前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:05
 * @Version 1.0
 **/

@Tag(name = "支付渠道配置接口")
@RestController
@RequestMapping("/api/v1/pay-channel-config")
@RequiredArgsConstructor
public class PayChannelConfigController {

    private final PayChannelConfigService payChannelConfigService;

    @Operation(summary = "支付渠道配置分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayChannelConfig:pay-channel-config:query')")
    public PageResult<PayChannelConfigVO> getPayChannelConfigPage(PayChannelConfigQuery queryParams ) {
        IPage<PayChannelConfigVO> result = payChannelConfigService.getPayChannelConfigPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增支付渠道配置")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayChannelConfig:pay-channel-config:add')")
    public Result<Void> savePayChannelConfig(@RequestBody @Valid PayChannelConfigForm formData ) {
        boolean result = payChannelConfigService.savePayChannelConfig(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取支付渠道配置表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayChannelConfig:pay-channel-config:edit')")
    public Result<PayChannelConfigForm> getPayChannelConfigForm(
            @Parameter(description = "支付渠道配置ID") @PathVariable Long id
    ) {
        PayChannelConfigForm formData = payChannelConfigService.getPayChannelConfigFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改支付渠道配置")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayChannelConfig:pay-channel-config:edit')")
    public Result<Void> updatePayChannelConfig(
            @Parameter(description = "支付渠道配置ID") @PathVariable Long id,
            @RequestBody @Validated PayChannelConfigForm formData
    ) {
        boolean result = payChannelConfigService.updatePayChannelConfig(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除支付渠道配置")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayChannelConfig:pay-channel-config:delete')")
    public Result<Void> deletePayChannelConfigs(
            @Parameter(description = "支付渠道配置ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payChannelConfigService.deletePayChannelConfigs(ids);
        return Result.judge(result);
    }
}
