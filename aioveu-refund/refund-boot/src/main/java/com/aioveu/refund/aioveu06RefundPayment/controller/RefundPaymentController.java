package com.aioveu.refund.aioveu06RefundPayment.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.refund.aioveu06RefundPayment.model.form.RefundPaymentForm;
import com.aioveu.refund.aioveu06RefundPayment.model.query.RefundPaymentQuery;
import com.aioveu.refund.aioveu06RefundPayment.model.vo.RefundPaymentVO;
import com.aioveu.refund.aioveu06RefundPayment.service.RefundPaymentService;
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
 * @ClassName: RefundPaymentController
 * @Description TODO 退款支付记录前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:48
 * @Version 1.0
 **/

@Tag(name = "退款支付记录接口")
@RestController
@RequestMapping("/api/v1/refund-payment")
@RequiredArgsConstructor
public class RefundPaymentController {

    private final RefundPaymentService refundPaymentService;

    @Operation(summary = "退款支付记录分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundPayment:refund-payment:query')")
    public PageResult<RefundPaymentVO> getRefundPaymentPage(RefundPaymentQuery queryParams ) {
        IPage<RefundPaymentVO> result = refundPaymentService.getRefundPaymentPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增退款支付记录")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundPayment:refund-payment:add')")
    public Result<Void> saveRefundPayment(@RequestBody @Valid RefundPaymentForm formData ) {
        boolean result = refundPaymentService.saveRefundPayment(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取退款支付记录表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundPayment:refund-payment:edit')")
    public Result<RefundPaymentForm> getRefundPaymentForm(
            @Parameter(description = "退款支付记录ID") @PathVariable Long id
    ) {
        RefundPaymentForm formData = refundPaymentService.getRefundPaymentFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改退款支付记录")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundPayment:refund-payment:edit')")
    public Result<Void> updateRefundPayment(
            @Parameter(description = "退款支付记录ID") @PathVariable Long id,
            @RequestBody @Validated RefundPaymentForm formData
    ) {
        boolean result = refundPaymentService.updateRefundPayment(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除退款支付记录")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundPayment:refund-payment:delete')")
    public Result<Void> deleteRefundPayments(
            @Parameter(description = "退款支付记录ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = refundPaymentService.deleteRefundPayments(ids);
        return Result.judge(result);
    }
}
