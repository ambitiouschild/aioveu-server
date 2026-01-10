package com.aioveu.oms.aioveu03OrderDelivery.controller;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu03OrderDelivery.model.form.OmsOrderDeliveryForm;
import com.aioveu.oms.aioveu03OrderDelivery.model.query.OmsOrderDeliveryQuery;
import com.aioveu.oms.aioveu03OrderDelivery.model.vo.OmsOrderDeliveryVO;
import com.aioveu.oms.aioveu03OrderDelivery.service.OmsOrderDeliveryService;
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
 * @ClassName: OmsOrderDeliveryController
 * @Description TODO  订单物流记录前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/8 20:28
 * @Version 1.0
 **/

@Tag(name = "订单物流记录接口")
@RestController
@RequestMapping("/api/v1/oms-order-delivery")
@RequiredArgsConstructor
public class OmsOrderDeliveryController {

    private final OmsOrderDeliveryService omsOrderDeliveryService;

    @Operation(summary = "订单物流记录分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderDelivery:oms-order-delivery:query')")
    @Log( value = "订单物流记录分页列表",module = LogModuleEnum.OMS)
    public PageResult<OmsOrderDeliveryVO> getOmsOrderDeliveryPage(OmsOrderDeliveryQuery queryParams ) {
        IPage<OmsOrderDeliveryVO> result = omsOrderDeliveryService.getOmsOrderDeliveryPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增订单物流记录")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderDelivery:oms-order-delivery:add')")
    @Log( value = "新增订单物流记录",module = LogModuleEnum.OMS)
    public Result<Void> saveOmsOrderDelivery(@RequestBody @Valid OmsOrderDeliveryForm formData ) {
        boolean result = omsOrderDeliveryService.saveOmsOrderDelivery(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取订单物流记录表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderDelivery:oms-order-delivery:edit')")
    @Log( value = "获取订单物流记录表单数据",module = LogModuleEnum.OMS)
    public Result<OmsOrderDeliveryForm> getOmsOrderDeliveryForm(
            @Parameter(description = "订单物流记录ID") @PathVariable Long id
    ) {
        OmsOrderDeliveryForm formData = omsOrderDeliveryService.getOmsOrderDeliveryFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改订单物流记录")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderDelivery:oms-order-delivery:edit')")
    @Log( value = "修改订单物流记录",module = LogModuleEnum.OMS)
    public Result<Void> updateOmsOrderDelivery(
            @Parameter(description = "订单物流记录ID") @PathVariable Long id,
            @RequestBody @Validated OmsOrderDeliveryForm formData
    ) {
        boolean result = omsOrderDeliveryService.updateOmsOrderDelivery(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除订单物流记录")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderDelivery:oms-order-delivery:delete')")
    @Log( value = "删除订单物流记录",module = LogModuleEnum.OMS)
    public Result<Void> deleteOmsOrderDeliverys(
            @Parameter(description = "订单物流记录ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = omsOrderDeliveryService.deleteOmsOrderDeliverys(ids);
        return Result.judge(result);
    }
}
