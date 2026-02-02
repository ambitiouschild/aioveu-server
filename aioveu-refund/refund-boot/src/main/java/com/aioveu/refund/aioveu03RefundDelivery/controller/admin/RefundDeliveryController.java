package com.aioveu.refund.aioveu03RefundDelivery.controller.admin;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.refund.aioveu01RefundOrder.enums.RefundStatusEnum;
import com.aioveu.refund.aioveu01RefundOrder.model.entity.RefundOrder;
import com.aioveu.refund.aioveu01RefundOrder.service.RefundOrderService;
import com.aioveu.refund.aioveu03RefundDelivery.model.form.ConfirmReceiveFormDTO;
import com.aioveu.refund.aioveu03RefundDelivery.model.form.RefundDeliveryForm;
import com.aioveu.refund.aioveu03RefundDelivery.model.query.RefundDeliveryQuery;
import com.aioveu.refund.aioveu03RefundDelivery.model.vo.RefundDeliveryVO;
import com.aioveu.refund.aioveu03RefundDelivery.service.RefundDeliveryService;
import com.aioveu.refund.aioveu04RefundOperationLog.enums.OperationTypeEnum;
import com.aioveu.refund.aioveu04RefundOperationLog.model.form.RefundOperationLogForm;
import com.aioveu.refund.aioveu04RefundOperationLog.service.RefundOperationLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: RefundDeliveryController
 * @Description TODO  退款物流信息（用于退货）前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:08
 * @Version 1.0
 **/

@Slf4j
@Tag(name = "退款物流信息（用于退货）接口")
@RestController
@RequestMapping("/api/v1/refund-delivery")
@RequiredArgsConstructor
public class RefundDeliveryController {

    private final RefundDeliveryService refundDeliveryService;

    private final RefundOrderService refundOrderService;

    private final RefundOperationLogService refundOperationLogService;

    @Operation(summary = "退款物流信息（用于退货）分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:query')")
    public PageResult<RefundDeliveryVO> getRefundDeliveryPage(RefundDeliveryQuery queryParams ) {
        IPage<RefundDeliveryVO> result = refundDeliveryService.getRefundDeliveryPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增退款物流信息（用于退货）")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:add')")
    public Result<Void> saveRefundDelivery(@RequestBody @Valid RefundDeliveryForm formData ) {
        boolean result = refundDeliveryService.saveRefundDelivery(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取退款物流信息（用于退货）表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:edit')")
    public Result<RefundDeliveryForm> getRefundDeliveryForm(
            @Parameter(description = "退款物流信息（用于退货）ID") @PathVariable Long id
    ) {
        RefundDeliveryForm formData = refundDeliveryService.getRefundDeliveryFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改退款物流信息（用于退货）")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:edit')")
    public Result<Void> updateRefundDelivery(
            @Parameter(description = "退款物流信息（用于退货）ID") @PathVariable Long id,
            @RequestBody @Validated RefundDeliveryForm formData
    ) {
        boolean result = refundDeliveryService.updateRefundDelivery(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除退款物流信息（用于退货）")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:delete')")
    public Result<Void> deleteRefundDeliverys(
            @Parameter(description = "退款物流信息（用于退货）ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = refundDeliveryService.deleteRefundDeliverys(ids);
        return Result.judge(result);
    }


    @Operation(summary = "商家确认收货")
    @PostMapping("/confirm-receive/{refundNo}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:add')")
    public Result<Void> confirmReceive(
            @PathVariable Long refundId,
            @RequestBody @Valid ConfirmReceiveFormDTO formData ) {


        refundDeliveryService.confirmReceive(refundId ,formData);
        log.info("【商家确认收货】更新物流信息");

        RefundOrder order =  refundOrderService.getOne(new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getId, refundId));

        order.setStatus(RefundStatusEnum.Refunding.getValue()); // 更新主订单状态为退款中

        refundOrderService.updateById(order);
        log.info("【商家确认收货】更新主订单状态为退款中");

        //构建操作记录
        RefundOperationLogForm refundOperationLogForm = new RefundOperationLogForm();
        refundOperationLogForm.setRefundId(refundId);
        refundOperationLogForm.setOperationType(OperationTypeEnum.Merchant_processing.getValue()); //商家处理
        refundOperationLogForm.setOperationContent( "商家已确认收货，商品状况：");
        refundOperationLogForm.setOperatorId(formData.getOperatorId());  //操作人ID
        refundOperationLogForm.setOperatorName(formData.getOperatorName());  //操作人姓名

        //保存操作记录
        boolean result = refundOperationLogService.saveRefundOperationLog(refundOperationLogForm);
        log.info("【商家确认收货】保存操作记录");

        return Result.judge(result);
    }





}
