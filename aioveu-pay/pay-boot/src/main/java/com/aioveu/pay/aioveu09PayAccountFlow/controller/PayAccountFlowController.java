package com.aioveu.pay.aioveu09PayAccountFlow.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu09PayAccountFlow.model.form.PayAccountFlowForm;
import com.aioveu.pay.aioveu09PayAccountFlow.model.query.PayAccountFlowQuery;
import com.aioveu.pay.aioveu09PayAccountFlow.model.vo.PayAccountFlowVO;
import com.aioveu.pay.aioveu09PayAccountFlow.service.PayAccountFlowService;
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
 * @ClassName: PayAccountFlowController
 * @Description TODO 账户流水前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:33
 * @Version 1.0
 **/

@Tag(name = "账户流水接口")
@RestController
@RequestMapping("/api/v1/pay-account-flow")
@RequiredArgsConstructor
public class PayAccountFlowController {

    private final PayAccountFlowService payAccountFlowService;

    @Operation(summary = "账户流水分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayAccountFlow:pay-account-flow:query')")
    public PageResult<PayAccountFlowVO> getPayAccountFlowPage(PayAccountFlowQuery queryParams ) {
        IPage<PayAccountFlowVO> result = payAccountFlowService.getPayAccountFlowPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增账户流水")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayAccountFlow:pay-account-flow:add')")
    public Result<Void> savePayAccountFlow(@RequestBody @Valid PayAccountFlowForm formData ) {
        boolean result = payAccountFlowService.savePayAccountFlow(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取账户流水表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayAccountFlow:pay-account-flow:edit')")
    public Result<PayAccountFlowForm> getPayAccountFlowForm(
            @Parameter(description = "账户流水ID") @PathVariable Long id
    ) {
        PayAccountFlowForm formData = payAccountFlowService.getPayAccountFlowFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改账户流水")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayAccountFlow:pay-account-flow:edit')")
    public Result<Void> updatePayAccountFlow(
            @Parameter(description = "账户流水ID") @PathVariable Long id,
            @RequestBody @Validated PayAccountFlowForm formData
    ) {
        boolean result = payAccountFlowService.updatePayAccountFlow(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除账户流水")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayAccountFlow:pay-account-flow:delete')")
    public Result<Void> deletePayAccountFlows(
            @Parameter(description = "账户流水ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payAccountFlowService.deletePayAccountFlows(ids);
        return Result.judge(result);
    }
}
