package com.aioveu.oms.aioveu01Order.controller.app;

import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.result.ResultCode;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu05OrderPay.model.form.OrderPaymentForm;
import com.aioveu.oms.aioveu05OrderPay.model.form.OrderSubmitForm;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import com.aioveu.oms.aioveu01Order.model.vo.OrderConfirmVO;
import com.aioveu.oms.aioveu01Order.model.vo.OrderPageVO;
import com.aioveu.oms.aioveu01Order.service.app.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: TODO APP-订单控制层
 * @Author: 雒世松
 * @Date: 2025/6/5 18:05
 * @param
 * @return:
 **/

@Tag(name  = "APP-订单接口")
@RestController
@RequestMapping("/app-api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Operation(summary ="订单分页列表")
    @GetMapping
    public PageResult<OrderPageVO> getOrderPage(OrderPageQuery queryParams) {
        IPage<OrderPageVO> result = orderService.getOrderPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "订单确认", description = "进入订单确认页面有两个入口，1：立即购买；2：购物车结算")
    @PostMapping("/confirm")
    public Result<OrderConfirmVO> confirmOrder(
            @Parameter(name ="立即购买必填，购物车结算不填") @RequestParam(required = false) Long skuId
    ) {
        OrderConfirmVO result = orderService.confirmOrder(skuId);
        return Result.success(result);
    }

    @Operation(summary ="订单提交")
    @PostMapping("/submit")
    public Result<String> submitOrder(@Validated @RequestBody OrderSubmitForm submitForm) {

        //在 Controller 中统一处理（最简单直接）
        //Seata 的拦截器捕获了这个异常，把它包装成了 RuntimeException
        try {
            String orderSn = orderService.submitOrder(submitForm);
            log.info("=== 订单提交成功: {} ===", orderSn);
            return Result.success(orderSn);

        } catch (RuntimeException e) {
            // 专门处理 Seata 包装的异常

            if (e.getMessage() != null && e.getMessage().contains("try to proceed invocation error")) {
                Throwable cause = e.getCause();

                if (cause instanceof BusinessException) {
                    BusinessException be = (BusinessException) cause;
                    log.warn("Seata包装的业务异常: {}", be.getMessage());
                    return Result.failed(ResultCode.Order_submission_system_exception, be.getMessage());
                }
            }
            return Result.failed(ResultCode.Order_submission_system_exception, e.getMessage());

        } catch (Exception e) {
            log.error("订单提交系统异常: ", e);
            return Result.failed(ResultCode.Order_submission_system_exception, "系统繁忙，请稍后重试");

        } finally {
            log.info("=== 订单提交结束 ===");
        }
    }

    @Operation(summary ="订单支付")
    @PostMapping("/payment")
    public Result payOrder(@Validated @RequestBody OrderPaymentForm paymentForm) {
        boolean result = orderService.payOrder(paymentForm);
        return Result.judge(result);
    }

    @Operation(summary ="订单删除")
    @DeleteMapping("/{orderId}")
    public Result deleteOrder(@PathVariable Long orderId) {
        boolean deleted = orderService.deleteOrder(orderId);
        return Result.judge(deleted);
    }

}
