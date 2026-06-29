package com.aioveu.oms.aioveu12OrderExportTask.controller.admin;


import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu01Order.model.query.OrderExportQuery;
import com.aioveu.oms.aioveu12OrderExportTask.model.form.OmsOrderExportTaskForm;
import com.aioveu.oms.aioveu12OrderExportTask.model.query.OmsOrderExportTaskQuery;
import com.aioveu.oms.aioveu12OrderExportTask.model.vo.OmsOrderExportTaskVo;
import com.aioveu.oms.aioveu12OrderExportTask.service.OmsOrderExportTaskService;
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
 * @ClassName: OmsOrderExportTaskController
 * @Description TODO 订单导出任务前端控制层
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 18:27
 * @Version 1.0
 **/
@Tag(name = "订单导出任务接口")
@RestController
@RequestMapping("/aioveu/api/v8/admin/oms//oms-order-export-task")
@RequiredArgsConstructor
public class OmsOrderExportTaskController {

    private final OmsOrderExportTaskService omsOrderExportTaskService;

    @Operation(summary = "订单导出任务分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderExportTask:oms-order-export-task:list')")
    public PageResult<OmsOrderExportTaskVo> getOmsOrderExportTaskPage(OmsOrderExportTaskQuery queryParams ) {
        IPage<OmsOrderExportTaskVo> result = omsOrderExportTaskService.getOmsOrderExportTaskPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增订单导出任务")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderExportTask:oms-order-export-task:create')")
    public Result<Void> saveOmsOrderExportTask(@RequestBody @Valid OmsOrderExportTaskForm formData ) {
        boolean result = omsOrderExportTaskService.saveOmsOrderExportTask(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取订单导出任务表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderExportTask:oms-order-export-task:update')")
    public Result<OmsOrderExportTaskForm> getOmsOrderExportTaskForm(
            @Parameter(description = "订单导出任务ID") @PathVariable Long id
    ) {
        OmsOrderExportTaskForm formData = omsOrderExportTaskService.getOmsOrderExportTaskFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改订单导出任务")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderExportTask:oms-order-export-task:update')")
    public Result<Void> updateOmsOrderExportTask(
            @Parameter(description = "订单导出任务ID") @PathVariable Long id,
            @RequestBody @Validated OmsOrderExportTaskForm formData
    ) {
        boolean result = omsOrderExportTaskService.updateOmsOrderExportTask(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除订单导出任务")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderExportTask:oms-order-export-task:delete')")
    public Result<Void> deleteOmsOrderExportTasks(
            @Parameter(description = "订单导出任务ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = omsOrderExportTaskService.deleteOmsOrderExportTasks(ids);
        return Result.judge(result);
    }

}
