package com.aioveu.refund.aioveu07RefundReason.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.refund.aioveu07RefundReason.model.form.RefundReasonForm;
import com.aioveu.refund.aioveu07RefundReason.model.query.RefundReasonQuery;
import com.aioveu.refund.aioveu07RefundReason.model.vo.RefundReasonVO;
import com.aioveu.refund.aioveu07RefundReason.service.RefundReasonService;
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
 * @ClassName: RefundReasonController
 * @Description TODO 退款原因分类前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:58
 * @Version 1.0
 **/

@Tag(name = "退款原因分类接口")
@RestController
@RequestMapping("/api/v1/refund-reason")
@RequiredArgsConstructor
public class RefundReasonController {

    private final RefundReasonService refundReasonService;

    @Operation(summary = "退款原因分类分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundReason:refund-reason:query')")
    public PageResult<RefundReasonVO> getRefundReasonPage(RefundReasonQuery queryParams ) {
        IPage<RefundReasonVO> result = refundReasonService.getRefundReasonPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增退款原因分类")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundReason:refund-reason:add')")
    public Result<Void> saveRefundReason(@RequestBody @Valid RefundReasonForm formData ) {
        boolean result = refundReasonService.saveRefundReason(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取退款原因分类表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundReason:refund-reason:edit')")
    public Result<RefundReasonForm> getRefundReasonForm(
            @Parameter(description = "退款原因分类ID") @PathVariable Long id
    ) {
        RefundReasonForm formData = refundReasonService.getRefundReasonFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改退款原因分类")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundReason:refund-reason:edit')")
    public Result<Void> updateRefundReason(
            @Parameter(description = "退款原因分类ID") @PathVariable Long id,
            @RequestBody @Validated RefundReasonForm formData
    ) {
        boolean result = refundReasonService.updateRefundReason(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除退款原因分类")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundReason:refund-reason:delete')")
    public Result<Void> deleteRefundReasons(
            @Parameter(description = "退款原因分类ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = refundReasonService.deleteRefundReasons(ids);
        return Result.judge(result);
    }
}
