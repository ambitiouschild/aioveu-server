package com.aioveu.refund.aioveu01RefundOrder.controller.app;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.refund.aioveu01RefundOrder.enums.RefundTypeEnum;
import com.aioveu.refund.aioveu01RefundOrder.model.entity.RefundOrder;
import com.aioveu.refund.aioveu01RefundOrder.model.form.RefundApplyFormDTO;
import com.aioveu.refund.aioveu01RefundOrder.model.form.RefundOrderForm;
import com.aioveu.refund.aioveu01RefundOrder.model.query.RefundOrderQuery;
import com.aioveu.refund.aioveu01RefundOrder.model.vo.RefundApplyResultVO;
import com.aioveu.refund.aioveu01RefundOrder.model.vo.RefundOrderVO;
import com.aioveu.refund.aioveu01RefundOrder.service.RefundOrderService;
import com.aioveu.refund.aioveu02RefundItem.model.entity.RefundItem;
import com.aioveu.refund.aioveu02RefundItem.service.RefundItemService;
import com.aioveu.refund.aioveu03RefundDelivery.model.entity.RefundDelivery;
import com.aioveu.refund.aioveu03RefundDelivery.model.form.RefundDeliveryForm;
import com.aioveu.refund.aioveu03RefundDelivery.service.RefundDeliveryService;
import com.aioveu.refund.aioveu04RefundOperationLog.model.entity.RefundOperationLog;
import com.aioveu.refund.aioveu04RefundOperationLog.service.RefundOperationLogService;
import com.aioveu.refund.aioveu05RefundProof.model.entity.RefundProof;
import com.aioveu.refund.aioveu05RefundProof.model.form.RefundProofForm;
import com.aioveu.refund.aioveu05RefundProof.service.RefundProofService;
import com.aioveu.refund.aioveu06RefundPayment.model.entity.RefundPayment;
import com.aioveu.refund.aioveu06RefundPayment.service.RefundPaymentService;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: RefundOrderForAppController
 * @Description TODO app订单退款申请控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 14:40
 * @Version 1.0
 **/

@Tag(name = "app订单退款申请接口")
@RestController
@RequestMapping("/api/v1/refund-order-app")
@RequiredArgsConstructor
public class RefundOrderForAppController {

    private final RefundOrderService refundOrderService;

    private final RefundItemService refundItemService;

    private final RefundDeliveryService refundDeliveryService;

    private final RefundProofService refundProofService;

    private final RefundOperationLogService refundOperationLogService;

    private final RefundPaymentService refundPaymentService;

    @Operation(summary = "创建退款申请")
    @PostMapping("/apply")
//    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOrder:refund-order:add')")
    public Result<Void> applyRefund(@RequestBody @Valid RefundApplyFormDTO formData) {


        // 1. 创建退款主订单
        RefundOrder refundOrder = refundOrderService.createRefundOrder(formData);

        //退款ID
        Long refundId = refundOrder.getId();
        //商品明细表单列表
        List<RefundItem> refundItems =formData.getRefundItems();
        // 货物流信息表单
        RefundDeliveryForm refundDeliveryForm= formData.getRefundDeliveryForm();
        // 退款类型
        Integer refundType= formData.getRefundType();


        // 2. 保存退款商品明细
        refundItemService.saveRefundItems(refundItems, refundId);

        // 3. 如果是退货退款，生成退货物流信息
        if (refundType == RefundTypeEnum.RETURN_AND_REFUND.getValue()) {
            refundDeliveryService.createRefundDelivery(refundDeliveryForm , refundId);
        }

        // 4. 保存退款凭证

        List<RefundProofForm> refundProofForms = formData.getRefundProofForms();
        if (CollectionUtils.isNotEmpty(refundProofForms)) {
            refundProofService.saveRefundProofs(refundProofForms, refundOrder.getId());
        }

        // 5. 记录操作日志

        boolean result = refundOperationLogService.saveRefundOperationLogWithRefundId(
                formData.getRefundOperationLogForm(),refundId);



        return Result.judge(result);
    }

    @Operation(summary = "查询退款详情")
    @GetMapping("/detail/{refundSn}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOrder:refund-order:edit')")
    public Result<RefundPayment> getRefundDetail(
            @Parameter(description = "订单退款Sn") @PathVariable String refundSn
    ) {

        //1. 查询退款主订单
        RefundOrder refundorder = refundOrderService.getRefundOrderEntityByRefundSn(refundSn);

        // 2. 查询退款商品明细
        Long refundId =refundorder.getId();
        List<RefundItem> items =  refundItemService.getRefundItemEntityByRefundId(refundId);

        // 3. 查询退款凭证
        List<RefundProof> proofs = refundProofService.getRefundProofEntityByRefundId(refundId);

        // 4. 查询操作日志
        List<RefundOperationLog> logs = refundOperationLogService.getRefundOperationLogEntityByRefundId(refundId);

        // 5. 如果是退货退款，查询物流信息
        if (refundorder.getRefundType() == RefundTypeEnum.RETURN_AND_REFUND.getValue()) {
            RefundDelivery delivery = refundDeliveryService.getRefundDeliveryEntityByRefundId(refundId);
        }

        // 6. 查询支付记录
        RefundPayment payment = refundPaymentService.getRefundPaymentEntityByRefundId(refundId);

        // 7. 组装返回结果
//        RefundDetailVO detailVO = assembleRefundDetail(order, items, proofs, logs, delivery, payment);

        return Result.success(payment);
    }


}
