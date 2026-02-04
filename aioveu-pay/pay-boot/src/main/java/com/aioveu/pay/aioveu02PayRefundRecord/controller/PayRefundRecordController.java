package com.aioveu.pay.aioveu02PayRefundRecord.controller;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu02PayRefundRecord.model.form.PayRefundRecordForm;
import com.aioveu.pay.aioveu02PayRefundRecord.model.query.PayRefundRecordQuery;
import com.aioveu.pay.aioveu02PayRefundRecord.model.vo.PayRefundRecordVO;
import com.aioveu.pay.aioveu02PayRefundRecord.service.PayRefundRecordService;
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
 * @ClassName: PayRefundRecordController
 * @Description TODO 退款记录前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 18:55
 * @Version 1.0
 **/

@Tag(name = "退款记录接口")
@RestController
@RequestMapping("/api/v1/pay-refund-record")
@RequiredArgsConstructor
public class PayRefundRecordController {

    private final PayRefundRecordService payRefundRecordService;

    @Operation(summary = "退款记录分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayRefundRecord:pay-refund-record:query')")
    @Log(value = "退款记录分页列表", module = LogModuleEnum.PAY)
    public PageResult<PayRefundRecordVO> getPayRefundRecordPage(PayRefundRecordQuery queryParams ) {
        IPage<PayRefundRecordVO> result = payRefundRecordService.getPayRefundRecordPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增退款记录")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayRefundRecord:pay-refund-record:add')")
    public Result<Void> savePayRefundRecord(@RequestBody @Valid PayRefundRecordForm formData ) {
        boolean result = payRefundRecordService.savePayRefundRecord(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取退款记录表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayRefundRecord:pay-refund-record:edit')")
    public Result<PayRefundRecordForm> getPayRefundRecordForm(
            @Parameter(description = "退款记录ID") @PathVariable Long id
    ) {
        PayRefundRecordForm formData = payRefundRecordService.getPayRefundRecordFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改退款记录")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayRefundRecord:pay-refund-record:edit')")
    public Result<Void> updatePayRefundRecord(
            @Parameter(description = "退款记录ID") @PathVariable Long id,
            @RequestBody @Validated PayRefundRecordForm formData
    ) {
        boolean result = payRefundRecordService.updatePayRefundRecord(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除退款记录")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayRefundRecord:pay-refund-record:delete')")
    public Result<Void> deletePayRefundRecords(
            @Parameter(description = "退款记录ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payRefundRecordService.deletePayRefundRecords(ids);
        return Result.judge(result);
    }
}
