package com.aioveu.registry.aioveu08RegistryAppFilingRecord.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.form.RegistryAppFilingRecordForm;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.query.RegistryAppFilingRecordQuery;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.vo.RegistryAppFilingRecordVo;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.service.RegistryAppFilingRecordService;
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
 * @ClassName: RegistryAppFilingRecordController
 * @Description TODO 小程序备案记录前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:19
 * @Version 1.0
 **/
@Tag(name = "小程序备案记录接口")
@RestController
@RequestMapping("/api/v1/registry-app-filing-record")
@RequiredArgsConstructor
public class RegistryAppFilingRecordController {


    private final RegistryAppFilingRecordService registryAppFilingRecordService;

    @Operation(summary = "小程序备案记录分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAppFilingRecord:registry-app-filing-record:list')")
    public PageResult<RegistryAppFilingRecordVo> getRegistryAppFilingRecordPage(RegistryAppFilingRecordQuery queryParams ) {
        IPage<RegistryAppFilingRecordVo> result = registryAppFilingRecordService.getRegistryAppFilingRecordPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增小程序备案记录")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAppFilingRecord:registry-app-filing-record:create')")
    public Result<Void> saveRegistryAppFilingRecord(@RequestBody @Valid RegistryAppFilingRecordForm formData ) {
        boolean result = registryAppFilingRecordService.saveRegistryAppFilingRecord(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取小程序备案记录表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAppFilingRecord:registry-app-filing-record:update')")
    public Result<RegistryAppFilingRecordForm> getRegistryAppFilingRecordForm(
            @Parameter(description = "小程序备案记录ID") @PathVariable Long id
    ) {
        RegistryAppFilingRecordForm formData = registryAppFilingRecordService.getRegistryAppFilingRecordFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改小程序备案记录")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAppFilingRecord:registry-app-filing-record:update')")
    public Result<Void> updateRegistryAppFilingRecord(
            @Parameter(description = "小程序备案记录ID") @PathVariable Long id,
            @RequestBody @Validated RegistryAppFilingRecordForm formData
    ) {
        boolean result = registryAppFilingRecordService.updateRegistryAppFilingRecord(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除小程序备案记录")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRegistryAppFilingRecord:registry-app-filing-record:delete')")
    public Result<Void> deleteRegistryAppFilingRecords(
            @Parameter(description = "小程序备案记录ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = registryAppFilingRecordService.deleteRegistryAppFilingRecords(ids);
        return Result.judge(result);
    }
}
