package com.aioveu.oms.aioveu06OrderSetting.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu06OrderSetting.model.form.OmsOrderSettingForm;
import com.aioveu.oms.aioveu06OrderSetting.model.query.OmsOrderSettingQuery;
import com.aioveu.oms.aioveu06OrderSetting.model.vo.OmsOrderSettingVO;
import com.aioveu.oms.aioveu06OrderSetting.service.OmsOrderSettingService;
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
 * @ClassName: OmsOrderSettingController
 * @Description TODO 订单配置信息前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:19
 * @Version 1.0
 **/

@Tag(name = "订单配置信息接口")
@RestController
@RequestMapping("/api/v1/oms-order-setting")
@RequiredArgsConstructor
public class OmsOrderSettingController {

    private final OmsOrderSettingService omsOrderSettingService;

    @Operation(summary = "订单配置信息分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderSetting:oms-order-setting:query')")
    public PageResult<OmsOrderSettingVO> getOmsOrderSettingPage(OmsOrderSettingQuery queryParams ) {
        IPage<OmsOrderSettingVO> result = omsOrderSettingService.getOmsOrderSettingPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增订单配置信息")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderSetting:oms-order-setting:add')")
    public Result<Void> saveOmsOrderSetting(@RequestBody @Valid OmsOrderSettingForm formData ) {
        boolean result = omsOrderSettingService.saveOmsOrderSetting(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取订单配置信息表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderSetting:oms-order-setting:edit')")
    public Result<OmsOrderSettingForm> getOmsOrderSettingForm(
            @Parameter(description = "订单配置信息ID") @PathVariable Long id
    ) {
        OmsOrderSettingForm formData = omsOrderSettingService.getOmsOrderSettingFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改订单配置信息")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderSetting:oms-order-setting:edit')")
    public Result<Void> updateOmsOrderSetting(
            @Parameter(description = "订单配置信息ID") @PathVariable Long id,
            @RequestBody @Validated OmsOrderSettingForm formData
    ) {
        boolean result = omsOrderSettingService.updateOmsOrderSetting(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除订单配置信息")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderSetting:oms-order-setting:delete')")
    public Result<Void> deleteOmsOrderSettings(
            @Parameter(description = "订单配置信息ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = omsOrderSettingService.deleteOmsOrderSettings(ids);
        return Result.judge(result);
    }
}
