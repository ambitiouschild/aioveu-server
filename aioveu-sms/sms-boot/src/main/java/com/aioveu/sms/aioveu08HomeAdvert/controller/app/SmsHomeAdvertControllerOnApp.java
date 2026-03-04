package com.aioveu.sms.aioveu08HomeAdvert.controller.app;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.sms.aioveu07HomeCategory.model.vo.SmsHomeCategoryVO;
import com.aioveu.sms.aioveu08HomeAdvert.model.form.SmsHomeAdvertForm;
import com.aioveu.sms.aioveu08HomeAdvert.model.query.SmsHomeAdvertQuery;
import com.aioveu.sms.aioveu08HomeAdvert.model.vo.SmsHomeAdvertVO;
import com.aioveu.sms.aioveu08HomeAdvert.service.SmsHomeAdvertService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: SmsHomeAdvertController
 * @Description TODO 首页广告配置（增加跳转路径）前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:40
 * @Version 1.0
 **/
@Tag(name = "首页广告配置（增加跳转路径）接口")
@RestController
@RequestMapping("/app-api/v1/sms-home-advert")
@RequiredArgsConstructor
public class SmsHomeAdvertControllerOnApp {

    private final SmsHomeAdvertService smsHomeAdvertService;

    @Operation(summary = "首页广告配置（增加跳转路径）分页列表")
    @GetMapping("/page")
    public Result<List<SmsHomeAdvertVO>> getSmsHomeCategoryList() {
        List<SmsHomeAdvertVO> result = smsHomeAdvertService.getSmsHomeAdvertList();
        return Result.success(result);
    }

    @Operation(summary = "新增首页广告配置（增加跳转路径）")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsHomeAdvert:sms-home-advert:add')")
    public Result<Void> saveSmsHomeAdvert(@RequestBody @Valid SmsHomeAdvertForm formData ) {
        boolean result = smsHomeAdvertService.saveSmsHomeAdvert(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取首页广告配置（增加跳转路径）表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsHomeAdvert:sms-home-advert:edit')")
    public Result<SmsHomeAdvertForm> getSmsHomeAdvertForm(
            @Parameter(description = "首页广告配置（增加跳转路径）ID") @PathVariable Long id
    ) {
        SmsHomeAdvertForm formData = smsHomeAdvertService.getSmsHomeAdvertFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改首页广告配置（增加跳转路径）")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsHomeAdvert:sms-home-advert:edit')")
    public Result<Void> updateSmsHomeAdvert(
            @Parameter(description = "首页广告配置（增加跳转路径）ID") @PathVariable Long id,
            @RequestBody @Validated SmsHomeAdvertForm formData
    ) {
        boolean result = smsHomeAdvertService.updateSmsHomeAdvert(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除首页广告配置（增加跳转路径）")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsHomeAdvert:sms-home-advert:delete')")
    public Result<Void> deleteSmsHomeAdverts(
            @Parameter(description = "首页广告配置（增加跳转路径）ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = smsHomeAdvertService.deleteSmsHomeAdverts(ids);
        return Result.judge(result);
    }
}
