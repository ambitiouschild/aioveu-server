package com.aioveu.oms.aioveu02OrderItem.controller.app;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu02OrderItem.model.form.OmsOrderItemForm;
import com.aioveu.oms.aioveu02OrderItem.model.query.OmsOrderItemQuery;
import com.aioveu.oms.aioveu02OrderItem.model.vo.OmsOrderDetailVO;
import com.aioveu.oms.aioveu02OrderItem.model.vo.OmsOrderItemVO;
import com.aioveu.oms.aioveu02OrderItem.service.OmsOrderItemService;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: OmsOrderDetailController
 * @Description TODO  订单详情控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/5/1 21:41
 * @Version 1.0
 **/

@Slf4j
@Tag(name = "获取订单详情",description = "根据订单编号获取订单详细信息")
@RestController
@RequestMapping("/app-api/v1/order")
@RequiredArgsConstructor
public class OmsOrderDetailController {

    private final OmsOrderItemService omsOrderItemService;


    @Operation(summary = "获取订单详情", description = "根据订单编号获取订单详细信息")
    @GetMapping("/detail/{orderSn}")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrderItem:oms-order-item:edit')")
    @Log( value = "获取订单详情",module = LogModuleEnum.OMS)
    public Result<OmsOrderDetailVO> getOmsOrderDetail(
            @Parameter(description = "订单编号（orderSn）", required = true)
            @PathVariable("orderSn") @NotNull String orderSn,
            @Parameter(description = "渠道标识 (用于后续扩展)", example = "1")
            @RequestParam(value = "channel", required = false, defaultValue = "1") Integer channel)
    {

        log.info("【获取订单详情】订单编号: {}, 渠道: {}", orderSn, channel);
        try {
            // 验证订单编号
            if (StringUtils.isBlank(orderSn)) {
                return Result.failed("订单编号不能为空");
            }

            // 获取订单详情
            OmsOrderDetailVO orderDetail = omsOrderItemService.getOrderDetailBySn(orderSn, channel);

            if (orderDetail == null) {
                return Result.failed("订单不存在或已被删除");
            }

            return Result.success(orderDetail);

        } catch (Exception e) {
            log.info("【获取订单详情异常】订单编号: {}, 渠道: {}", orderSn, channel, e);
            return Result.failed("获取订单详情失败: " + e.getMessage());
        }

    }


}
