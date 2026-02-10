package com.aioveu.pay.aioveu06PayFlow.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu06PayFlow.model.form.PayFlowForm;
import com.aioveu.pay.aioveu06PayFlow.model.query.PayFlowQuery;
import com.aioveu.pay.aioveu06PayFlow.model.vo.PayFlowVO;
import com.aioveu.pay.aioveu06PayFlow.service.PayFlowService;
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
 * @ClassName: PayFlowController
 * @Description TODO 支付流水前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 15:58
 * @Version 1.0
 **/

@Tag(name = "支付流水接口")
@RestController
@RequestMapping("/api/v1/pay-flow")
@RequiredArgsConstructor
public class PayFlowController {

    private final PayFlowService payFlowService;

    @Operation(summary = "支付流水分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayFlow:pay-flow:query')")
    public PageResult<PayFlowVO> getPayFlowPage(PayFlowQuery queryParams ) {
        IPage<PayFlowVO> result = payFlowService.getPayFlowPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增支付流水")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayFlow:pay-flow:add')")
    public Result<Void> savePayFlow(@RequestBody @Valid PayFlowForm formData ) {
        boolean result = payFlowService.savePayFlow(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取支付流水表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayFlow:pay-flow:edit')")
    public Result<PayFlowForm> getPayFlowForm(
            @Parameter(description = "支付流水ID") @PathVariable Long id
    ) {
        PayFlowForm formData = payFlowService.getPayFlowFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改支付流水")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayFlow:pay-flow:edit')")
    public Result<Void> updatePayFlow(
            @Parameter(description = "支付流水ID") @PathVariable Long id,
            @RequestBody @Validated PayFlowForm formData
    ) {
        boolean result = payFlowService.updatePayFlow(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除支付流水")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayFlow:pay-flow:delete')")
    public Result<Void> deletePayFlows(
            @Parameter(description = "支付流水ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payFlowService.deletePayFlows(ids);
        return Result.judge(result);
    }
}
