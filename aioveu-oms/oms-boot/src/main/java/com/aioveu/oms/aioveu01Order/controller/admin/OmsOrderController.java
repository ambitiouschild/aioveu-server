package com.aioveu.oms.aioveu01Order.controller.admin;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.oms.aioveu01Order.model.form.OmsOrderForm;
import com.aioveu.oms.aioveu01Order.model.vo.OrderDTO;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu02OrderItem.model.entity.OmsOrderItem;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import com.aioveu.oms.aioveu01Order.model.vo.OmsOrderPageVO;
import com.aioveu.oms.aioveu01Order.service.admin.OmsOrderService;
import com.aioveu.oms.aioveu02OrderItem.service.OmsOrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 订单详情前端控制层
 *
 * @author 可我不敌可爱
 * @since 2026-01-07 18:11
 */

@Tag(name  = "Admin-订单详情接口")
@RestController
//@RequestMapping("/api/v1/orders")
@RequestMapping("/api/v1/oms-order")
@RequiredArgsConstructor
public class OmsOrderController {

    private final OmsOrderService omsOrderService;

    private final OmsOrderItemService orderItemService;

    @Operation(summary ="订单分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:query')")
    @Log( value = "订单分页列表",module = LogModuleEnum.OMS)
    public PageResult<OmsOrderPageVO> getOrderPage(OrderPageQuery queryParams) {
        IPage<OmsOrderPageVO> page = omsOrderService.getOmsOrderPage(queryParams);
        return PageResult.success(page);
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{orderId}")
    @Log( value = "订单详情",module = LogModuleEnum.OMS)
    public Result getOrderDetail(
            @Parameter(name ="订单ID") @PathVariable Long orderId
    ) {
        OrderDTO orderDTO = new OrderDTO();
        // 订单
        OmsOrder order = omsOrderService.getById(orderId);

        // 订单明细
        List<OmsOrderItem> orderItems = orderItemService.list(new LambdaQueryWrapper<OmsOrderItem>()
                .eq(OmsOrderItem::getOrderId, orderId)
        );
        orderItems = Optional.ofNullable(orderItems).orElse(Collections.EMPTY_LIST);

        orderDTO.setOrder(order).setOrderItems(orderItems);
        return Result.success(orderDTO);
    }

    @Operation(summary = "新增订单详情")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:add')")
    @Log( value = "新增订单详情",module = LogModuleEnum.OMS)
    public Result<Void> saveOmsOrder(@RequestBody @Valid OmsOrderForm formData ) {
        boolean result = omsOrderService.saveOmsOrder(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取订单详情表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:edit')")
    @Log( value = "获取订单详情表单数据",module = LogModuleEnum.OMS)
    public Result<OmsOrderForm> getOmsOrderForm(
            @Parameter(description = "订单详情ID") @PathVariable Long id
    ) {
        OmsOrderForm formData = omsOrderService.getOmsOrderFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改订单详情")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:edit')")
    @Log( value = "修改订单详情",module = LogModuleEnum.OMS)
    public Result<Void> updateOmsOrder(
            @Parameter(description = "订单详情ID") @PathVariable Long id,
            @RequestBody @Validated OmsOrderForm formData
    ) {
        boolean result = omsOrderService.updateOmsOrder(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除订单详情")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:delete')")
    @Log( value = "删除订单详情",module = LogModuleEnum.OMS)
    public Result<Void> deleteOmsOrders(
            @Parameter(description = "订单详情ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = omsOrderService.deleteOmsOrders(ids);
        return Result.judge(result);
    }
}
