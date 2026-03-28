package com.aioveu.pay.aioveu03PayConfigWechat.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu03PayConfigWechat.model.form.PayConfigWechatForm;
import com.aioveu.pay.aioveu03PayConfigWechat.model.query.PayConfigWechatQuery;
import com.aioveu.pay.aioveu03PayConfigWechat.model.vo.PayConfigWechatVo;
import com.aioveu.pay.aioveu03PayConfigWechat.service.PayConfigWechatService;
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
 * @ClassName: PayConfigWechatController
 * @Description TODO 微信支付配置前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:19
 * @Version 1.0
 **/
@Tag(name = "微信支付配置接口")
@RestController
@RequestMapping("/api/v1/pay-config-wechat")
@RequiredArgsConstructor
public class PayConfigWechatController {

    private final PayConfigWechatService payConfigWechatService;

    @Operation(summary = "微信支付配置分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigWechat:pay-config-wechat:list')")
    public PageResult<PayConfigWechatVo> getPayConfigWechatPage(PayConfigWechatQuery queryParams ) {
        IPage<PayConfigWechatVo> result = payConfigWechatService.getPayConfigWechatPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增微信支付配置")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigWechat:pay-config-wechat:create')")
    public Result<Void> savePayConfigWechat(@RequestBody @Valid PayConfigWechatForm formData ) {
        boolean result = payConfigWechatService.savePayConfigWechat(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取微信支付配置表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigWechat:pay-config-wechat:update')")
    public Result<PayConfigWechatForm> getPayConfigWechatForm(
            @Parameter(description = "微信支付配置ID") @PathVariable Long id
    ) {
        PayConfigWechatForm formData = payConfigWechatService.getPayConfigWechatFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改微信支付配置")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigWechat:pay-config-wechat:update')")
    public Result<Void> updatePayConfigWechat(
            @Parameter(description = "微信支付配置ID") @PathVariable Long id,
            @RequestBody @Validated PayConfigWechatForm formData
    ) {
        boolean result = payConfigWechatService.updatePayConfigWechat(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除微信支付配置")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigWechat:pay-config-wechat:delete')")
    public Result<Void> deletePayConfigWechats(
            @Parameter(description = "微信支付配置ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payConfigWechatService.deletePayConfigWechats(ids);
        return Result.judge(result);
    }
}
