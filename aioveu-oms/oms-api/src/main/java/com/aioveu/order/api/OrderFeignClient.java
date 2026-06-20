package com.aioveu.order.api;

import com.aioveu.common.enums.pay.PaymentStatusEnum;
import com.aioveu.order.model.aioveu01Order.form.OmsOrderForm;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: OrderFeignClient
 * @Description TODO  订单微服务Feign 客户端
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/10 13:29
 * @Version 1.0
 **/
//@FeignClient(value = "aioveu-oms", contextId = "order", configuration = {FeignDecoderConfig.class})

@FeignClient(value = "aioveu-tenant-oms"
)
public interface OrderFeignClient {

    /**
     * 根据订单编号查询订单详情
     */
    @GetMapping("/api/v1/oms-order/orderNo/{orderNo}")
    OmsOrderForm getOrderDetailByOrderNo(@Parameter(name ="订单编号") @PathVariable String orderNo);

    /**
     * 根据微信返回结果更新订单状态操作
     */

    @PutMapping("/api/v1/oms-order/{orderNo}/{status}")
    boolean updateOrderStatusByWechatPay(
            @Parameter(name ="订单编号") @PathVariable String orderNo,
            @Parameter(description = "微信返回结果") @PathVariable PaymentStatusEnum status
    );


    /**
     * 根据orderSn获取到订单
     */

    @PostMapping("/app-api/v1/orders/{orderSn}")
    OmsOrderForm getOmsOrderByOrderNo(@PathVariable String orderSn);

}
