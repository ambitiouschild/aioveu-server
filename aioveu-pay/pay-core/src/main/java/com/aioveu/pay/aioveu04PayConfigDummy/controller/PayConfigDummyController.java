package com.aioveu.pay.aioveu04PayConfigDummy.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu04PayConfigDummy.model.form.PayConfigDummyForm;
import com.aioveu.pay.aioveu04PayConfigDummy.model.query.PayConfigDummyQuery;
import com.aioveu.pay.aioveu04PayConfigDummy.model.vo.PayConfigDummyVo;
import com.aioveu.pay.aioveu04PayConfigDummy.service.PayConfigDummyService;
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
 * @ClassName: PayConfigDummyController
 * @Description TODO 模拟支付配置前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 17:38
 * @Version 1.0
 **/
@Tag(name = "模拟支付配置接口")
@RestController
@RequestMapping("/api/v1/pay-config-dummy")
@RequiredArgsConstructor
public class PayConfigDummyController {

    private final PayConfigDummyService payConfigDummyService;

    @Operation(summary = "模拟支付配置分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigDummy:pay-config-dummy:list')")
    public PageResult<PayConfigDummyVo> getPayConfigDummyPage(PayConfigDummyQuery queryParams ) {
        IPage<PayConfigDummyVo> result = payConfigDummyService.getPayConfigDummyPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增模拟支付配置")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigDummy:pay-config-dummy:create')")
    public Result<Void> savePayConfigDummy(@RequestBody @Valid PayConfigDummyForm formData ) {
        boolean result = payConfigDummyService.savePayConfigDummy(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取模拟支付配置表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigDummy:pay-config-dummy:update')")
    public Result<PayConfigDummyForm> getPayConfigDummyForm(
            @Parameter(description = "模拟支付配置ID") @PathVariable Long id
    ) {
        PayConfigDummyForm formData = payConfigDummyService.getPayConfigDummyFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改模拟支付配置")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigDummy:pay-config-dummy:update')")
    public Result<Void> updatePayConfigDummy(
            @Parameter(description = "模拟支付配置ID") @PathVariable Long id,
            @RequestBody @Validated PayConfigDummyForm formData
    ) {
        boolean result = payConfigDummyService.updatePayConfigDummy(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除模拟支付配置")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayConfigDummy:pay-config-dummy:delete')")
    public Result<Void> deletePayConfigDummys(
            @Parameter(description = "模拟支付配置ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payConfigDummyService.deletePayConfigDummys(ids);
        return Result.judge(result);
    }
}
