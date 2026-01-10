package com.aioveu.oms.aioveu04OrderLog.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu04OrderLog.model.form.OmsOrderLogForm;
import com.aioveu.oms.aioveu04OrderLog.model.query.OmsOrderLogQuery;
import com.aioveu.oms.aioveu04OrderLog.model.vo.OmsOrderLogVO;
import com.aioveu.oms.aioveu04OrderLog.service.OmsOrderLogService;
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
 * @ClassName: OmsOrderLogController
 * @Description TODO  订单操作历史记录前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 16:45
 * @Version 1.0
 **/

@Tag(name = "订单操作历史记录接口")
@RestController
@RequestMapping("/api/v1/oms-order-log")
@RequiredArgsConstructor
public class OmsOrderLogController {

    private final OmsOrderLogService omsOrderLogService;

    @Operation(summary = "订单操作历史记录分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderLog:oms-order-log:query')")
    public PageResult<OmsOrderLogVO> getOmsOrderLogPage(OmsOrderLogQuery queryParams ) {
        IPage<OmsOrderLogVO> result = omsOrderLogService.getOmsOrderLogPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增订单操作历史记录")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderLog:oms-order-log:add')")
    public Result<Void> saveOmsOrderLog(@RequestBody @Valid OmsOrderLogForm formData ) {
        boolean result = omsOrderLogService.saveOmsOrderLog(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取订单操作历史记录表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderLog:oms-order-log:edit')")
    public Result<OmsOrderLogForm> getOmsOrderLogForm(
            @Parameter(description = "订单操作历史记录ID") @PathVariable Long id
    ) {
        OmsOrderLogForm formData = omsOrderLogService.getOmsOrderLogFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改订单操作历史记录")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderLog:oms-order-log:edit')")
    public Result<Void> updateOmsOrderLog(
            @Parameter(description = "订单操作历史记录ID") @PathVariable Long id,
            @RequestBody @Validated OmsOrderLogForm formData
    ) {
        boolean result = omsOrderLogService.updateOmsOrderLog(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除订单操作历史记录")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderLog:oms-order-log:delete')")
    public Result<Void> deleteOmsOrderLogs(
            @Parameter(description = "订单操作历史记录ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = omsOrderLogService.deleteOmsOrderLogs(ids);
        return Result.judge(result);
    }
}
