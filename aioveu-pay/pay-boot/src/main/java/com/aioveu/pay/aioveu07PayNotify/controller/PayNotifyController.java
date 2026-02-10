package com.aioveu.pay.aioveu07PayNotify.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu07PayNotify.model.form.PayNotifyForm;
import com.aioveu.pay.aioveu07PayNotify.model.query.PayNotifyQuery;
import com.aioveu.pay.aioveu07PayNotify.model.vo.PayNotifyVO;
import com.aioveu.pay.aioveu07PayNotify.service.PayNotifyService;
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
 * @ClassName: PayNotifyController
 * @Description TODO 支付通知前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:01
 * @Version 1.0
 **/

@Tag(name = "支付通知接口")
@RestController
@RequestMapping("/api/v1/pay-notify")
@RequiredArgsConstructor
public class PayNotifyController {

    private final PayNotifyService payNotifyService;

    @Operation(summary = "支付通知分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayNotify:pay-notify:query')")
    public PageResult<PayNotifyVO> getPayNotifyPage(PayNotifyQuery queryParams ) {
        IPage<PayNotifyVO> result = payNotifyService.getPayNotifyPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增支付通知")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayNotify:pay-notify:add')")
    public Result<Void> savePayNotify(@RequestBody @Valid PayNotifyForm formData ) {
        boolean result = payNotifyService.savePayNotify(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取支付通知表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayNotify:pay-notify:edit')")
    public Result<PayNotifyForm> getPayNotifyForm(
            @Parameter(description = "支付通知ID") @PathVariable Long id
    ) {
        PayNotifyForm formData = payNotifyService.getPayNotifyFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改支付通知")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayNotify:pay-notify:edit')")
    public Result<Void> updatePayNotify(
            @Parameter(description = "支付通知ID") @PathVariable Long id,
            @RequestBody @Validated PayNotifyForm formData
    ) {
        boolean result = payNotifyService.updatePayNotify(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除支付通知")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayNotify:pay-notify:delete')")
    public Result<Void> deletePayNotifys(
            @Parameter(description = "支付通知ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payNotifyService.deletePayNotifys(ids);
        return Result.judge(result);
    }
}
