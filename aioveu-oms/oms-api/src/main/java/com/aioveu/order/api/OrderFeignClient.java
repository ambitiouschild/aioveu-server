package com.aioveu.order.api;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.common.web.config.FeignDecoderConfig;
import com.aioveu.order.model.OmsOrder;
import com.aioveu.order.model.OrderDTO;
import io.swagger.v3.oas.annotations.Operation;
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

@FeignClient(value = "aioveu-tenant-oms")
public interface OrderFeignClient {

    /**
     * 根据订单编号查询订单详情
     */
    @GetMapping("/api/v1/oms-order/orderNo/{orderNo}")
    OmsOrder getOrderDetailByOrderNo(@Parameter(name ="订单编号") @PathVariable String orderNo);

    /**
     * 根据微信返回结果更新订单状态操作
     */

    @PutMapping("/api/v1/oms-order/{orderNo}/{status}")
    boolean updateOrderStatusByWechatPay(
            @Parameter(name ="订单编号") @PathVariable String orderNo,
            @Parameter(description = "微信返回结果") @PathVariable Integer status
    );


}
