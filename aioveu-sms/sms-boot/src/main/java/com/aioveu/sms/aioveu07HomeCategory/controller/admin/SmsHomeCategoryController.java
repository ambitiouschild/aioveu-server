package com.aioveu.sms.aioveu07HomeCategory.controller.admin;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.sms.aioveu07HomeCategory.model.form.SmsHomeCategoryForm;
import com.aioveu.sms.aioveu07HomeCategory.model.query.SmsHomeCategoryQuery;
import com.aioveu.sms.aioveu07HomeCategory.model.vo.SmsHomeCategoryVO;
import com.aioveu.sms.aioveu07HomeCategory.service.SmsHomeCategoryService;
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
 * @ClassName: SmsHomeCategoryController
 * @Description TODO 首页分类配置前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:19
 * @Version 1.0
 **/
@Tag(name = "首页分类配置接口")
@RestController
@RequestMapping("/api/v1/sms-home-category")
@RequiredArgsConstructor
public class SmsHomeCategoryController {

    private final SmsHomeCategoryService smsHomeCategoryService;

    @Operation(summary = "首页分类配置分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsHomeCategory:sms-home-category:query')")
    public PageResult<SmsHomeCategoryVO> getSmsHomeCategoryPage(SmsHomeCategoryQuery queryParams ) {
        IPage<SmsHomeCategoryVO> result = smsHomeCategoryService.getSmsHomeCategoryPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增首页分类配置")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsHomeCategory:sms-home-category:add')")
    public Result<Void> saveSmsHomeCategory(@RequestBody @Valid SmsHomeCategoryForm formData ) {
        boolean result = smsHomeCategoryService.saveSmsHomeCategory(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取首页分类配置表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsHomeCategory:sms-home-category:edit')")
    public Result<SmsHomeCategoryForm> getSmsHomeCategoryForm(
            @Parameter(description = "首页分类配置ID") @PathVariable Long id
    ) {
        SmsHomeCategoryForm formData = smsHomeCategoryService.getSmsHomeCategoryFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改首页分类配置")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsHomeCategory:sms-home-category:edit')")
    public Result<Void> updateSmsHomeCategory(
            @Parameter(description = "首页分类配置ID") @PathVariable Long id,
            @RequestBody @Validated SmsHomeCategoryForm formData
    ) {
        boolean result = smsHomeCategoryService.updateSmsHomeCategory(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除首页分类配置")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsHomeCategory:sms-home-category:delete')")
    public Result<Void> deleteSmsHomeCategorys(
            @Parameter(description = "首页分类配置ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = smsHomeCategoryService.deleteSmsHomeCategorys(ids);
        return Result.judge(result);
    }
}
