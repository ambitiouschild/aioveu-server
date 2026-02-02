package com.aioveu.refund.aioveu03RefundDelivery.controller.app;

import com.aioveu.common.result.Result;
import com.aioveu.refund.aioveu01RefundOrder.model.entity.RefundOrder;
import com.aioveu.refund.aioveu01RefundOrder.model.form.RefundOrderForm;
import com.aioveu.refund.aioveu01RefundOrder.service.RefundOrderService;
import com.aioveu.refund.aioveu03RefundDelivery.model.entity.RefundDelivery;
import com.aioveu.refund.aioveu03RefundDelivery.model.form.RefundDeliveryForm;
import com.aioveu.refund.aioveu03RefundDelivery.service.RefundDeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: RefundDeliveryForAppController
 * @Description TODO  APP用户退款物流信息（用于退货）控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 18:17
 * @Version 1.0
 **/

@Tag(name = "APP用户退款物流信息（用于退货）接口")
@RestController
@RequestMapping("/api/v1/refund-delivery")
@RequiredArgsConstructor
public class RefundDeliveryForAppController {


    private final RefundOrderService refundOrderService;

    private final RefundDeliveryService refundDeliveryService;

    @Operation(summary = "新增退款物流信息（用于退货）")
    @PostMapping("/fill")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:add')")
    public Result<Void> fillRefundDelivery(@RequestBody @Valid RefundDeliveryForm formData ) {
        RefundDelivery delivery = refundDeliveryService.fillRefundDelivery(formData);


        // 更新主订单状态
//        Long  refundId = formData.getRefundId();
//        RefundOrderForm order = refundOrderService.getRefundOrderFormData(refundId);
//        order.setRefundStatus(RefundStatus.WAIT_RECEIVE.getCode());
//        refundOrderService.updateRefundOrder(refundId ,order);

        return Result.success();
    }
}
