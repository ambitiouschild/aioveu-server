package com.aioveu.pay.aioveu05PayReconciliationDetail.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.form.PayReconciliationDetailForm;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.query.PayReconciliationDetailQuery;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.vo.PayReconciliationDetailVO;
import com.aioveu.pay.aioveu05PayReconciliationDetail.service.PayReconciliationDetailService;
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
 * @ClassName: PayReconciliationDetailController
 * @Description TODO 对账明细前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 15:12
 * @Version 1.0
 **/

@Tag(name = "对账明细接口")
@RestController
@RequestMapping("/api/v1/pay-reconciliation-detail")
@RequiredArgsConstructor
public class PayReconciliationDetailController {

    private final PayReconciliationDetailService payReconciliationDetailService;

    @Operation(summary = "对账明细分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayReconciliationDetail:pay-reconciliation-detail:query')")
    public PageResult<PayReconciliationDetailVO> getPayReconciliationDetailPage(PayReconciliationDetailQuery queryParams ) {
        IPage<PayReconciliationDetailVO> result = payReconciliationDetailService.getPayReconciliationDetailPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增对账明细")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayReconciliationDetail:pay-reconciliation-detail:add')")
    public Result<Void> savePayReconciliationDetail(@RequestBody @Valid PayReconciliationDetailForm formData ) {
        boolean result = payReconciliationDetailService.savePayReconciliationDetail(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取对账明细表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayReconciliationDetail:pay-reconciliation-detail:edit')")
    public Result<PayReconciliationDetailForm> getPayReconciliationDetailForm(
            @Parameter(description = "对账明细ID") @PathVariable Long id
    ) {
        PayReconciliationDetailForm formData = payReconciliationDetailService.getPayReconciliationDetailFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改对账明细")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayReconciliationDetail:pay-reconciliation-detail:edit')")
    public Result<Void> updatePayReconciliationDetail(
            @Parameter(description = "对账明细ID") @PathVariable Long id,
            @RequestBody @Validated PayReconciliationDetailForm formData
    ) {
        boolean result = payReconciliationDetailService.updatePayReconciliationDetail(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除对账明细")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayReconciliationDetail:pay-reconciliation-detail:delete')")
    public Result<Void> deletePayReconciliationDetails(
            @Parameter(description = "对账明细ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payReconciliationDetailService.deletePayReconciliationDetails(ids);
        return Result.judge(result);
    }
}
