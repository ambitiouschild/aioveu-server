package com.aioveu.oms.aioveu07UndoLog.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu07UndoLog.model.form.UndoLogForm;
import com.aioveu.oms.aioveu07UndoLog.model.query.UndoLogQuery;
import com.aioveu.oms.aioveu07UndoLog.model.vo.UndoLogVO;
import com.aioveu.oms.aioveu07UndoLog.service.UndoLogService;
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
 * @ClassName: UndoLogController
 * @Description TODO  AT transaction mode undo table前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:44
 * @Version 1.0
 **/

@Tag(name = "AT transaction mode undo table接口")
@RestController
@RequestMapping("/api/v1/undo-log")
@RequiredArgsConstructor
public class UndoLogController {

    private final UndoLogService undoLogService;

    @Operation(summary = "AT transaction mode undo table分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsUndoLog:undo-log:query')")
    public PageResult<UndoLogVO> getUndoLogPage(UndoLogQuery queryParams ) {
        IPage<UndoLogVO> result = undoLogService.getUndoLogPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增AT transaction mode undo table")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsUndoLog:undo-log:add')")
    public Result<Void> saveUndoLog(@RequestBody @Valid UndoLogForm formData ) {
        boolean result = undoLogService.saveUndoLog(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取AT transaction mode undo table表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsUndoLog:undo-log:edit')")
    public Result<UndoLogForm> getUndoLogForm(
            @Parameter(description = "AT transaction mode undo tableID") @PathVariable Long id
    ) {
        UndoLogForm formData = undoLogService.getUndoLogFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改AT transaction mode undo table")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsUndoLog:undo-log:edit')")
    public Result<Void> updateUndoLog(
            @Parameter(description = "AT transaction mode undo tableID") @PathVariable Long id,
            @RequestBody @Validated UndoLogForm formData
    ) {
        boolean result = undoLogService.updateUndoLog(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除AT transaction mode undo table")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsUndoLog:undo-log:delete')")
    public Result<Void> deleteUndoLogs(
            @Parameter(description = "AT transaction mode undo tableID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = undoLogService.deleteUndoLogs(ids);
        return Result.judge(result);
    }
}
