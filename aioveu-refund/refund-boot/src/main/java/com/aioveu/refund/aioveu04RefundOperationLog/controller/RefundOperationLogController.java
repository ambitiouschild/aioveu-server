package com.aioveu.refund.aioveu04RefundOperationLog.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.refund.aioveu04RefundOperationLog.model.form.RefundOperationLogForm;
import com.aioveu.refund.aioveu04RefundOperationLog.model.query.RefundOperationLogQuery;
import com.aioveu.refund.aioveu04RefundOperationLog.model.vo.RefundOperationLogVO;
import com.aioveu.refund.aioveu04RefundOperationLog.service.RefundOperationLogService;
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
 * @ClassName: RefundOperationLogController
 * @Description TODO 退款操作记录（用于审计）前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:25
 * @Version 1.0
 **/

@Tag(name = "退款操作记录（用于审计）接口")
@RestController
@RequestMapping("/api/v1/refund-operation-log")
@RequiredArgsConstructor
public class RefundOperationLogController {

    private final RefundOperationLogService refundOperationLogService;

    @Operation(summary = "退款操作记录（用于审计）分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOperationLog:refund-operation-log:query')")
    public PageResult<RefundOperationLogVO> getRefundOperationLogPage(RefundOperationLogQuery queryParams ) {
        IPage<RefundOperationLogVO> result = refundOperationLogService.getRefundOperationLogPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增退款操作记录（用于审计）")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOperationLog:refund-operation-log:add')")
    public Result<Void> saveRefundOperationLog(@RequestBody @Valid RefundOperationLogForm formData ) {
        boolean result = refundOperationLogService.saveRefundOperationLog(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取退款操作记录（用于审计）表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOperationLog:refund-operation-log:edit')")
    public Result<RefundOperationLogForm> getRefundOperationLogForm(
            @Parameter(description = "退款操作记录（用于审计）ID") @PathVariable Long id
    ) {
        RefundOperationLogForm formData = refundOperationLogService.getRefundOperationLogFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改退款操作记录（用于审计）")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOperationLog:refund-operation-log:edit')")
    public Result<Void> updateRefundOperationLog(
            @Parameter(description = "退款操作记录（用于审计）ID") @PathVariable Long id,
            @RequestBody @Validated RefundOperationLogForm formData
    ) {
        boolean result = refundOperationLogService.updateRefundOperationLog(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除退款操作记录（用于审计）")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOperationLog:refund-operation-log:delete')")
    public Result<Void> deleteRefundOperationLogs(
            @Parameter(description = "退款操作记录（用于审计）ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = refundOperationLogService.deleteRefundOperationLogs(ids);
        return Result.judge(result);
    }
}
