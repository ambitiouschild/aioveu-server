package com.aioveu.pay.aioveu08PayAccount.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu08PayAccount.model.form.PayAccountForm;
import com.aioveu.pay.aioveu08PayAccount.model.query.PayAccountQuery;
import com.aioveu.pay.aioveu08PayAccount.model.vo.PayAccountVO;
import com.aioveu.pay.aioveu08PayAccount.service.PayAccountService;
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
 * @ClassName: PayAccountController
 * @Description TODO 支付账户前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:14
 * @Version 1.0
 **/

@Tag(name = "支付账户接口")
@RestController
@RequestMapping("/api/v1/pay-account")
@RequiredArgsConstructor
public class PayAccountController {

    private final PayAccountService payAccountService;

    @Operation(summary = "支付账户分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayAccount:pay-account:query')")
    public PageResult<PayAccountVO> getPayAccountPage(PayAccountQuery queryParams ) {
        IPage<PayAccountVO> result = payAccountService.getPayAccountPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增支付账户")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayAccount:pay-account:add')")
    public Result<Void> savePayAccount(@RequestBody @Valid PayAccountForm formData ) {
        boolean result = payAccountService.savePayAccount(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取支付账户表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayAccount:pay-account:edit')")
    public Result<PayAccountForm> getPayAccountForm(
            @Parameter(description = "支付账户ID") @PathVariable Long id
    ) {
        PayAccountForm formData = payAccountService.getPayAccountFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改支付账户")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayAccount:pay-account:edit')")
    public Result<Void> updatePayAccount(
            @Parameter(description = "支付账户ID") @PathVariable Long id,
            @RequestBody @Validated PayAccountForm formData
    ) {
        boolean result = payAccountService.updatePayAccount(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除支付账户")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayAccount:pay-account:delete')")
    public Result<Void> deletePayAccounts(
            @Parameter(description = "支付账户ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payAccountService.deletePayAccounts(ids);
        return Result.judge(result);
    }
}
