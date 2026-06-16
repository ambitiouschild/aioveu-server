package com.aioveu.oms.aioveu01Order.controller.app;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.result.ResultCode;
import com.aioveu.oms.aioveu01Order.model.form.ShipOrderDTO;
import com.aioveu.order.model.aioveu01Order.form.OmsOrderForm;
import com.aioveu.order.model.aioveu01Order.vo.OrderSubmitVO;
import com.aioveu.pay.model.aioveuPayment.PaymentResultVO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.order.model.aioveu05OrderPay.form.OrderPaymentForm;
import com.aioveu.order.model.aioveu05OrderPay.form.OrderSubmitForm;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import com.aioveu.oms.aioveu01Order.model.vo.OrderConfirmVO;
import com.aioveu.oms.aioveu01Order.model.vo.OrderPageVO;
import com.aioveu.oms.aioveu01Order.service.app.OrderService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    //包含多个筛选条件（如时间范围、订单状态、关键词等）。使用 POST可以通过请求体传递复杂的结构化数据
    //前端发送的是JSON请求体（POST + data参数），但后端没有用@RequestBody接收，所以Spring无法将请求体解析为对象。
    @Operation(summary ="订单分页列表")
    @PostMapping
    public PageResult<OrderPageVO> getOrderPage(@RequestBody OrderPageQuery queryParams) {


            // 添加日志查看接收到的参数
            log.info("【订单分页】查询参数: {}", JSON.toJSONString(queryParams));
            log.info("【订单分页】startTime: {}, endTime: {}",
                    queryParams.getStartTime(), queryParams.getEndTime());

            IPage<OrderPageVO> page = orderService.getOrderPage(queryParams);
            return PageResult.success(page);

            /*
            *   TODO 是的，您的理解完全正确。
                        如果您的 App 端采用上拉加载更多（流式加载）而非传统的分页器点击方式，那么接口的核心任务就变成了：
                        返回符合条件的完整订单列表（或首批若干条，后续由App端触发加载更多）。
                        返回基于所有数据的统计信息。
                        在这种情况下，不需要返回 IPage对象，因为它包含的 pageNum, pageSize, total, pages, hasNextPage等分页元数据对App端已失去意义。
                        因此，接口可以直接返回一个包含“订单列表”和“统计信息”的聚合对象。
            *
            * */

    }


    @Operation(summary = "获取订单统计信息")
    @PostMapping("/statistics")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:statistics')")
    @Log( value = "获取订单统计信息",module = LogModuleEnum.OMS)
    public Result<Map<String, Object>> getOrderStatistics(@RequestBody OrderPageQuery  queryParams ) {

        try {
            Map<String, Object> statistics = orderService.getOrderStatistics(queryParams);
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取订单统计失败：", e);
            return Result.failed("获取统计信息失败");
        }
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
    public Result<OrderSubmitVO> submitOrder(
            @RequestHeader("X-Client-Id") String clientId,
            @Validated @RequestBody OrderSubmitForm submitForm) {

        //在 Controller 中统一处理（最简单直接）
        //Seata 的拦截器捕获了这个异常，把它包装成了 RuntimeException
        //方案二（✅ 如果你非要 Service 里取）
        //必须在 Controller 里先存到 ThreadLocal，并且手动继承


        try {
            OrderSubmitVO orderSubmitVO = orderService.submitOrder(submitForm, clientId);
            log.info("=== 订单提交成功: {} ===", orderSubmitVO);
            return Result.success(orderSubmitVO);

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



    /**
     * 根据orderSn获取到订单
     */
    @Operation(summary ="根据orderSn获取到订单")
    @PostMapping("/{orderNo}")
    public Result<OmsOrderForm>  getOmsOrderByOrderNo(@PathVariable String orderSn) {
        OmsOrderForm omsOrderForm = orderService.getOmsOrderByOrderNo(orderSn);
        return Result.success(omsOrderForm);
    }




    @Operation(summary ="订单支付,只干一件事,获取支付参数（唤起微信 / 支付宝）")
    @PostMapping("/payment")
    public Result<PaymentResultVO> payOrder(@Validated @RequestBody OrderPaymentForm paymentForm) {

        try {
            // 调用支付服务
            PaymentResultVO paymentResult = orderService.payOrder(paymentForm);

            return Result.success(paymentResult);
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
            log.error("订单支付系统异常: ", e);
            return Result.failed(ResultCode.Order_submission_system_exception, "系统繁忙，请稍后重试");

        } finally {
            log.info("=== 订单支付结束 ===");
        }

    }

    @Operation(summary ="订单删除")
    @DeleteMapping("/{orderId}")
    public Result deleteOrder(@PathVariable Long orderId) {
        boolean deleted = orderService.deleteOrder(orderId);
        return Result.judge(deleted);
    }


    /**
     * 手动发货（商家后台 / 小程序）
     * ✅ **前端触发**
     * ✅ **微信发货**
     * ✅ **本地状态同步**
     * 订单号（orderSn / orderId）一定要放在 URL 路径里
     * 物流信息（公司、运单号、备注）放在 body（data）里
     */
    @Operation(summary ="手动发货（商家后台 / 小程序）")
    @PostMapping("/{orderSn}/ship")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:statistics')")
    @Log( value = "手动发货（商家后台 / 小程序）",module = LogModuleEnum.OMS)
    public Result<JsonNode> ship(@PathVariable String orderSn,
                                 @RequestBody ShipOrderDTO dto) {

        log.info("【发货】手动发货 orderSn={}", orderSn);

        try {
            JsonNode result = orderService.uploadShipping(orderSn, dto);

            if (result.has("errcode") && result.get("errcode").asInt() == 0) {
                orderService.markAsShipped(orderSn, dto);
            }
            return Result.success(result);

        } catch (Exception e) {
            log.error("获取订单统计失败：", e);
            return Result.failed("获取统计信息失败");
        }
    }





}
