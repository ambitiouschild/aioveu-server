package com.aioveu.pay.aioveu01PayOrder.controller.app;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu01PayOrder.model.query.PayOrderQuery;
import com.aioveu.pay.model.aioveu01PayOrder.vo.PayOrderVO;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import com.aioveu.pay.model.aioveu01PayOrder.form.PayOrderForm;
import com.aioveu.pay.model.aioveu01PayOrder.form.PayOrderCreateForm;
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
 * @ClassName: PayOrderControllerForApp
 * @Description TODO 支付订单前端控制层APP
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 17:34
 * @Version 1.0
 **/

@Tag(name = "支付订单接口")
@RestController
@RequestMapping("/app-api/v1/pay-order")
@RequiredArgsConstructor
public class PayOrderControllerForApp {

    private final PayOrderService payOrderService;

    @Operation(summary = "支付订单分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayOrder:pay-order:query')")
    @Log(value = "支付订单分页列表", module = LogModuleEnum.PAY)
    public PageResult<PayOrderVO> getPayOrderPage(PayOrderQuery queryParams ) {
        IPage<PayOrderVO> result = payOrderService.getPayOrderPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "根据订单号查询支付订单")
    @GetMapping("/getByOrderNo")
    @Log(value = "支付订单分页列表", module = LogModuleEnum.PAY)
    public Result<PayOrderVO> getByOrderNo(@RequestBody @Valid String orderNo ) {
        PayOrderVO result = payOrderService.getByOrderNo(orderNo);


        return Result.success(result);
    }


    @Operation(summary = "新增支付订单")
    @PostMapping
//    @PreAuthorize("@ss.hasPerm('aioveuMallPayOrder:pay-order:add')")
    public Result<String> createPayOrder(@RequestBody @Valid PayOrderCreateForm formData ) {
        String orderNo = payOrderService.createPayOrder(formData);
        return Result.success(orderNo);
    }

    @Operation(summary = "获取支付订单表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayOrder:pay-order:edit')")
    public Result<PayOrderForm> getPayOrderForm(
            @Parameter(description = "支付订单ID") @PathVariable Long id
    ) {
        PayOrderForm formData = payOrderService.getPayOrderFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改支付订单")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayOrder:pay-order:edit')")
    public Result<Void> updatePayOrder(
            @Parameter(description = "支付订单ID") @PathVariable Long id,
            @RequestBody @Validated PayOrderForm formData
    ) {
        boolean result = payOrderService.updatePayOrder(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除支付订单")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayOrder:pay-order:delete')")
    public Result<Void> deletePayOrders(
            @Parameter(description = "支付订单ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = payOrderService.deletePayOrders(ids);
        return Result.judge(result);
    }
}
