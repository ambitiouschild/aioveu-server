package com.aioveu.pay.aioveu05PayConfigAlipay.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.form.PayConfigAlipayForm;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.query.PayConfigAlipayQuery;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.vo.PayConfigAlipayVo;
import com.aioveu.pay.aioveu05PayConfigAlipay.service.PayConfigAlipayService;
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
 * @ClassName: PayConfigAlipayController
 * @Description TODO 支付宝支付配置前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 17:14
 * @Version 1.0
 **/
@Tag(name = "支付宝支付配置接口")
@RestController
@RequestMapping("/api/v1/pay-config-alipay")
@RequiredArgsConstructor
public class PayConfigAlipayController {

    private final PayConfigAlipayService payConfigAlipayService;

    @Operation(summary = "支付宝支付配置分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigAlipay:pay-config-alipay:list')")
    public PageResult<PayConfigAlipayVo> getPayConfigAlipayPage(PayConfigAlipayQuery queryParams ) {
        IPage<PayConfigAlipayVo> result = payConfigAlipayService.getPayConfigAlipayPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增支付宝支付配置")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigAlipay:pay-config-alipay:create')")
    public Result<Void> savePayConfigAlipay(@RequestBody @Valid PayConfigAlipayForm formData ) {
        boolean result = payConfigAlipayService.savePayConfigAlipay(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取支付宝支付配置表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigAlipay:pay-config-alipay:update')")
    public Result<PayConfigAlipayForm> getPayConfigAlipayForm(
            @Parameter(description = "支付宝支付配置ID") @PathVariable Long id
    ) {
        PayConfigAlipayForm formData = payConfigAlipayService.getPayConfigAlipayFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改支付宝支付配置")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigAlipay:pay-config-alipay:update')")
    public Result<Void> updatePayConfigAlipay(
            @Parameter(description = "支付宝支付配置ID") @PathVariable Long id,
            @RequestBody @Validated PayConfigAlipayForm formData
    ) {
        boolean result = payConfigAlipayService.updatePayConfigAlipay(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除支付宝支付配置")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigAlipay:pay-config-alipay:delete')")
    public Result<Void> deletePayConfigAlipays(
            @Parameter(description = "支付宝支付配置ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payConfigAlipayService.deletePayConfigAlipays(ids);
        return Result.judge(result);
    }
}
