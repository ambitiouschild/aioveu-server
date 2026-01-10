package com.aioveu.oms.aioveu02OrderItem.controller;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu02OrderItem.model.form.OmsOrderItemForm;
import com.aioveu.oms.aioveu02OrderItem.model.query.OmsOrderItemQuery;
import com.aioveu.oms.aioveu02OrderItem.model.vo.OmsOrderItemVO;
import com.aioveu.oms.aioveu02OrderItem.service.OmsOrderItemService;
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
 * @ClassName: OmsOrderItemController
 * @Description TODO  订单商品信息前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/8 19:36
 * @Version 1.0
 **/

@Tag(name = "订单商品信息接口")
@RestController
@RequestMapping("/api/v1/oms-order-item")
@RequiredArgsConstructor
public class OmsOrderItemController {

    private final OmsOrderItemService omsOrderItemService;

    @Operation(summary = "订单商品信息分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderItem:oms-order-item:query')")
    @Log( value = "订单商品信息分页列表",module = LogModuleEnum.OMS)
    public PageResult<OmsOrderItemVO> getOmsOrderItemPage(OmsOrderItemQuery queryParams ) {
        IPage<OmsOrderItemVO> result = omsOrderItemService.getOmsOrderItemPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增订单商品信息")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderItem:oms-order-item:add')")
    @Log( value = "新增订单商品信息",module = LogModuleEnum.OMS)
    public Result<Void> saveOmsOrderItem(@RequestBody @Valid OmsOrderItemForm formData ) {
        boolean result = omsOrderItemService.saveOmsOrderItem(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取订单商品信息表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderItem:oms-order-item:edit')")
    @Log( value = "获取订单商品信息表单数据",module = LogModuleEnum.OMS)
    public Result<OmsOrderItemForm> getOmsOrderItemForm(
            @Parameter(description = "订单商品信息ID") @PathVariable Long id
    ) {
        OmsOrderItemForm formData = omsOrderItemService.getOmsOrderItemFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改订单商品信息")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderItem:oms-order-item:edit')")
    @Log( value = "修改订单商品信息",module = LogModuleEnum.OMS)
    public Result<Void> updateOmsOrderItem(
            @Parameter(description = "订单商品信息ID") @PathVariable Long id,
            @RequestBody @Validated OmsOrderItemForm formData
    ) {
        boolean result = omsOrderItemService.updateOmsOrderItem(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除订单商品信息")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderItem:oms-order-item:delete')")
    @Log( value = "删除订单商品信息",module = LogModuleEnum.OMS)
    public Result<Void> deleteOmsOrderItems(
            @Parameter(description = "订单商品信息ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = omsOrderItemService.deleteOmsOrderItems(ids);
        return Result.judge(result);
    }
}
