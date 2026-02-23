package com.aioveu.tenant.aioveu08Config.controller;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.tenant.aioveu08Config.model.form.ConfigForm;
import com.aioveu.tenant.aioveu08Config.model.query.ConfigQuery;
import com.aioveu.tenant.aioveu08Config.model.vo.ConfigVO;
import com.aioveu.tenant.aioveu08Config.service.ConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: ConfigController
 * @Description TODO 系统配置前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 14:33
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "08.系统配置")
@RequestMapping("/api/v1/configs")
public class ConfigController {

    private final ConfigService configService;

    @Operation(summary = "系统配置分页列表")
    @GetMapping
    @PreAuthorize("@ss.hasPerm('sys:config:list')")
    @Log( value = "系统配置分页列表",module = LogModuleEnum.SETTING)
    public PageResult<ConfigVO> page(@ParameterObject ConfigQuery queryParams) {
        IPage<ConfigVO> result = configService.page(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增系统配置")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:config:create')")
    @Log( value = "新增系统配置",module = LogModuleEnum.SETTING)
    public Result<?> save(@RequestBody @Valid ConfigForm configForm) {
        return Result.judge(configService.save(configForm));
    }

    @Operation(summary = "获取系统配置表单数据")
    @GetMapping("/{id}/form")
    public Result<ConfigForm> getConfigForm(
            @Parameter(description = "系统配置ID") @PathVariable Long id
    ) {
        ConfigForm formData = configService.getConfigFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "刷新系统配置缓存")
    @PutMapping("/refresh")
    @PreAuthorize("@ss.hasPerm('sys:config:refresh')")
    @Log( value = "刷新系统配置缓存",module = LogModuleEnum.SETTING)
    public Result<ConfigForm> refreshCache() {
        return Result.judge(configService.refreshCache());
    }

    @Operation(summary = "修改系统配置")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('sys:config:update')")
    @Log( value = "修改系统配置",module = LogModuleEnum.SETTING)
    public Result<?> update(@Valid @PathVariable Long id, @RequestBody ConfigForm configForm) {
        return Result.judge(configService.edit(id, configForm));
    }

    @Operation(summary = "删除系统配置")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPerm('sys:config:delete')")
    @Log( value = "删除系统配置",module = LogModuleEnum.SETTING)
    public Result<?> delete(@PathVariable Long id) {
        return Result.judge(configService.delete(id));
    }
}
