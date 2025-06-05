package com.aioveu.oms.controller.admin;

import com.aioveu.oms.model.dto.OrderDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.model.dto.OrderDTO;
import com.aioveu.oms.model.entity.OmsOrder;
import com.aioveu.oms.model.entity.OmsOrderItem;
import com.aioveu.oms.model.query.OrderPageQuery;
import com.aioveu.oms.model.vo.OmsOrderPageVO;
import com.aioveu.oms.service.admin.OmsOrderService;
import com.aioveu.oms.service.app.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Admin-订单控制层
 *
 * @author huawei
 * @since 2.3.0
 */
@Tag(name  = "Admin-订单管理")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OmsOrderController {

    private final OmsOrderService orderService;

    private final OrderItemService orderItemService;

    @Operation(summary ="订单分页列表")
    @GetMapping
    public PageResult<OmsOrderPageVO> getOrderPage(OrderPageQuery queryParams) {
        IPage<OmsOrderPageVO> page = orderService.getOrderPage(queryParams);
        return PageResult.success(page);
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{orderId}")
    public Result getOrderDetail(
            @Parameter(name ="订单ID") @PathVariable Long orderId
    ) {
        OrderDTO orderDTO = new OrderDTO();
        // 订单
        OmsOrder order = orderService.getById(orderId);

        // 订单明细
        List<OmsOrderItem> orderItems = orderItemService.list(new LambdaQueryWrapper<OmsOrderItem>()
                .eq(OmsOrderItem::getOrderId, orderId)
        );
        orderItems = Optional.ofNullable(orderItems).orElse(Collections.EMPTY_LIST);

        orderDTO.setOrder(order).setOrderItems(orderItems);
        return Result.success(orderDTO);
    }
}
