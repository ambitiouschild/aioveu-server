package com.aioveu.oms.aioveu05OrderPay.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu05OrderPay.model.form.OmsOrderPayForm;
import com.aioveu.oms.aioveu05OrderPay.model.query.OmsOrderPayQuery;
import com.aioveu.oms.aioveu05OrderPay.model.vo.OmsOrderPayVO;
import com.aioveu.oms.aioveu05OrderPay.service.OmsOrderPayService;
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
 * @ClassName: OmsOrderPayController
 * @Description TODO  支付信息前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:03
 * @Version 1.0
 **/

@Tag(name = "支付信息接口")
@RestController
@RequestMapping("/api/v1/oms-order-pay")
@RequiredArgsConstructor
public class OmsOrderPayController {

    private final OmsOrderPayService omsOrderPayService;

    @Operation(summary = "支付信息分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderPay:oms-order-pay:query')")
    public PageResult<OmsOrderPayVO> getOmsOrderPayPage(OmsOrderPayQuery queryParams ) {
        IPage<OmsOrderPayVO> result = omsOrderPayService.getOmsOrderPayPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增支付信息")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderPay:oms-order-pay:add')")
    public Result<Void> saveOmsOrderPay(@RequestBody @Valid OmsOrderPayForm formData ) {
        boolean result = omsOrderPayService.saveOmsOrderPay(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取支付信息表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderPay:oms-order-pay:edit')")
    public Result<OmsOrderPayForm> getOmsOrderPayForm(
            @Parameter(description = "支付信息ID") @PathVariable Long id
    ) {
        OmsOrderPayForm formData = omsOrderPayService.getOmsOrderPayFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改支付信息")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderPay:oms-order-pay:edit')")
    public Result<Void> updateOmsOrderPay(
            @Parameter(description = "支付信息ID") @PathVariable Long id,
            @RequestBody @Validated OmsOrderPayForm formData
    ) {
        boolean result = omsOrderPayService.updateOmsOrderPay(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除支付信息")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderPay:oms-order-pay:delete')")
    public Result<Void> deleteOmsOrderPays(
            @Parameter(description = "支付信息ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = omsOrderPayService.deleteOmsOrderPays(ids);
        return Result.judge(result);
    }
}
