package com.aioveu.pay.aioveu13PayCallbackRecord.controller;


import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.form.PayCallbackRecordForm;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.query.PayCallbackRecordQuery;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.vo.PayCallbackRecordVo;
import com.aioveu.pay.aioveu13PayCallbackRecord.service.PayCallbackRecordService;
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
 * @ClassName: PayCallbackRecordController
 * @Description TODO 支付回调记录前端控制层
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/29 18:15
 * @Version 1.0
 **/
@Tag(name = "支付回调记录接口")
@RestController
@RequestMapping("/aioveu/api/v8/admin/pay/pay-callback-record")
@RequiredArgsConstructor
public class PayCallbackRecordController {

    private final PayCallbackRecordService payCallbackRecordService;

    @Operation(summary = "支付回调记录分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayCallbackRecord:pay-callback-record:list')")
    public PageResult<PayCallbackRecordVo> getPayCallbackRecordPage(PayCallbackRecordQuery queryParams ) {
        IPage<PayCallbackRecordVo> result = payCallbackRecordService.getPayCallbackRecordPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增支付回调记录")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayCallbackRecord:pay-callback-record:create')")
    public Result<Void> savePayCallbackRecord(@RequestBody @Valid PayCallbackRecordForm formData ) {
        boolean result = payCallbackRecordService.savePayCallbackRecord(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取支付回调记录表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayCallbackRecord:pay-callback-record:update')")
    public Result<PayCallbackRecordForm> getPayCallbackRecordForm(
            @Parameter(description = "支付回调记录ID") @PathVariable Long id
    ) {
        PayCallbackRecordForm formData = payCallbackRecordService.getPayCallbackRecordFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改支付回调记录")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayCallbackRecord:pay-callback-record:update')")
    public Result<Void> updatePayCallbackRecord(
            @Parameter(description = "支付回调记录ID") @PathVariable Long id,
            @RequestBody @Validated PayCallbackRecordForm formData
    ) {
        boolean result = payCallbackRecordService.updatePayCallbackRecord(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除支付回调记录")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayCallbackRecord:pay-callback-record:delete')")
    public Result<Void> deletePayCallbackRecords(
            @Parameter(description = "支付回调记录ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payCallbackRecordService.deletePayCallbackRecords(ids);
        return Result.judge(result);
    }
}
