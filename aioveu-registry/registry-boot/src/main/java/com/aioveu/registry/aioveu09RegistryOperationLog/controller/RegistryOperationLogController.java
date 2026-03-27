package com.aioveu.registry.aioveu09RegistryOperationLog.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.form.RegistryOperationLogForm;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.query.RegistryOperationLogQuery;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.vo.RegistryOperationLogVo;
import com.aioveu.registry.aioveu09RegistryOperationLog.service.RegistryOperationLogService;
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
 * @ClassName: RegistryOperationLogController
 * @Description TODO 操作日志前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:32
 * @Version 1.0
 **/
@Tag(name = "操作日志接口")
@RestController
@RequestMapping("/api/v1/registry-operation-log")
@RequiredArgsConstructor
public class RegistryOperationLogController {

    private final RegistryOperationLogService registryOperationLogService;

    @Operation(summary = "操作日志分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryOperationLog:registry-operation-log:list')")
    public PageResult<RegistryOperationLogVo> getRegistryOperationLogPage(RegistryOperationLogQuery queryParams ) {
        IPage<RegistryOperationLogVo> result = registryOperationLogService.getRegistryOperationLogPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增操作日志")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryOperationLog:registry-operation-log:create')")
    public Result<Void> saveRegistryOperationLog(@RequestBody @Valid RegistryOperationLogForm formData ) {
        boolean result = registryOperationLogService.saveRegistryOperationLog(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取操作日志表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryOperationLog:registry-operation-log:update')")
    public Result<RegistryOperationLogForm> getRegistryOperationLogForm(
            @Parameter(description = "操作日志ID") @PathVariable Long id
    ) {
        RegistryOperationLogForm formData = registryOperationLogService.getRegistryOperationLogFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改操作日志")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryOperationLog:registry-operation-log:update')")
    public Result<Void> updateRegistryOperationLog(
            @Parameter(description = "操作日志ID") @PathVariable Long id,
            @RequestBody @Validated RegistryOperationLogForm formData
    ) {
        boolean result = registryOperationLogService.updateRegistryOperationLog(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除操作日志")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryOperationLog:registry-operation-log:delete')")
    public Result<Void> deleteRegistryOperationLogs(
            @Parameter(description = "操作日志ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = registryOperationLogService.deleteRegistryOperationLogs(ids);
        return Result.judge(result);
    }
}
