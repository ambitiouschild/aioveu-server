package com.aioveu.pay.aioveu04PayReconciliation.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu04PayReconciliation.model.form.PayReconciliationForm;
import com.aioveu.pay.aioveu04PayReconciliation.model.query.PayReconciliationQuery;
import com.aioveu.pay.aioveu04PayReconciliation.model.vo.PayReconciliationVO;
import com.aioveu.pay.aioveu04PayReconciliation.service.PayReconciliationService;
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
 * @ClassName: PayReconciliationController
 * @Description TODO 支付对账前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 21:04
 * @Version 1.0
 **/


@Tag(name = "支付对账接口")
@RestController
@RequestMapping("/api/v1/pay-reconciliation")
@RequiredArgsConstructor
public class PayReconciliationController {


    private final PayReconciliationService payReconciliationService;

    @Operation(summary = "支付对账分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayReconciliation:pay-reconciliation:query')")
    public PageResult<PayReconciliationVO> getPayReconciliationPage(PayReconciliationQuery queryParams ) {
        IPage<PayReconciliationVO> result = payReconciliationService.getPayReconciliationPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增支付对账")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayReconciliation:pay-reconciliation:add')")
    public Result<Void> savePayReconciliation(@RequestBody @Valid PayReconciliationForm formData ) {
        boolean result = payReconciliationService.savePayReconciliation(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取支付对账表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayReconciliation:pay-reconciliation:edit')")
    public Result<PayReconciliationForm> getPayReconciliationForm(
            @Parameter(description = "支付对账ID") @PathVariable Long id
    ) {
        PayReconciliationForm formData = payReconciliationService.getPayReconciliationFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改支付对账")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayReconciliation:pay-reconciliation:edit')")
    public Result<Void> updatePayReconciliation(
            @Parameter(description = "支付对账ID") @PathVariable Long id,
            @RequestBody @Validated PayReconciliationForm formData
    ) {
        boolean result = payReconciliationService.updatePayReconciliation(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除支付对账")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayReconciliation:pay-reconciliation:delete')")
    public Result<Void> deletePayReconciliations(
            @Parameter(description = "支付对账ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payReconciliationService.deletePayReconciliations(ids);
        return Result.judge(result);
    }

}
