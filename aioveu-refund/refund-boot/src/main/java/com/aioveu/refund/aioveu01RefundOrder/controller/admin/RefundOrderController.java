package com.aioveu.refund.aioveu01RefundOrder.controller.admin;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.refund.aioveu01RefundOrder.converter.RefundOrderConverter;
import com.aioveu.refund.aioveu01RefundOrder.enums.RefundStatusEnum;
import com.aioveu.refund.aioveu01RefundOrder.enums.RefundTypeEnum;
import com.aioveu.refund.aioveu01RefundOrder.model.entity.RefundOrder;
import com.aioveu.refund.aioveu01RefundOrder.model.form.RefundAuditFormDTO;
import com.aioveu.refund.aioveu01RefundOrder.model.form.RefundOrderForm;
import com.aioveu.refund.aioveu01RefundOrder.model.query.RefundOrderQuery;
import com.aioveu.refund.aioveu01RefundOrder.model.vo.RefundOrderVO;
import com.aioveu.refund.aioveu01RefundOrder.service.RefundOrderService;
import com.aioveu.refund.aioveu04RefundOperationLog.enums.OperationTypeEnum;
import com.aioveu.refund.aioveu04RefundOperationLog.enums.OperatorTypeEnum;
import com.aioveu.refund.aioveu04RefundOperationLog.model.form.RefundOperationLogForm;
import com.aioveu.refund.aioveu04RefundOperationLog.service.RefundOperationLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

import static java.time.LocalDateTime.now;

/**
 * @ClassName: RefundOrderController
 * @Description TODO  订单退款申请前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:33
 * @Version 1.0
 **/

@Tag(name = "订单退款申请接口")
@RestController
@RequestMapping("/api/v1/refund-order")
@RequiredArgsConstructor
public class RefundOrderController {

    private final RefundOrderService refundOrderService;

    private final RefundOrderConverter refundOrderConverter;

    private final RefundOperationLogService refundOperationLogService;

    @Operation(summary = "订单退款申请分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOrder:refund-order:query')")
    public PageResult<RefundOrderVO> getRefundOrderPage(RefundOrderQuery queryParams ) {
        IPage<RefundOrderVO> result = refundOrderService.getRefundOrderPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增订单退款申请")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOrder:refund-order:add')")
    public Result<Void> saveRefundOrder(@RequestBody @Valid RefundOrderForm formData ) {
        boolean result = refundOrderService.saveRefundOrder(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取订单退款申请表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOrder:refund-order:edit')")
    public Result<RefundOrderForm> getRefundOrderForm(
            @Parameter(description = "订单退款申请ID") @PathVariable Long id
    ) {
        RefundOrderForm formData = refundOrderService.getRefundOrderFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改订单退款申请")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOrder:refund-order:edit')")
    public Result<Void> updateRefundOrder(
            @Parameter(description = "订单退款申请ID") @PathVariable Long id,
            @RequestBody @Validated RefundOrderForm formData
    ) {
        boolean result = refundOrderService.updateRefundOrder(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除订单退款申请")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOrder:refund-order:delete')")
    public Result<Void> deleteRefundOrders(
            @Parameter(description = "订单退款申请ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = refundOrderService.deleteRefundOrders(ids);
        return Result.judge(result);
    }

    @Operation(summary = "审核退款申请")
    @PostMapping("/{id}/audit")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundOrder:refund-order:add')")
    public Result<Void> auditRefund(
            @Valid @RequestBody RefundAuditFormDTO auditDTO
    ) {

        // 1. 查询退款订单
        RefundOrderForm refundOrder = refundOrderService.getRefundOrderFormData(auditDTO.getId());

        // 2. 更新退款状态
        if (refundOrder.getId() != null) {
            if (refundOrder.getRefundType() == RefundTypeEnum.RETURN_AND_REFUND.getValue()) {

                //审核通过
                // 退货退款：生成退货地址
                refundOrder.setStatus(RefundStatusEnum.Approved.getValue());
//                generateReturnAddress(refundOrder.getId());
            } else {
                // 仅退款：直接进入退款中状态
                refundOrder.setStatus(RefundStatusEnum.Refunding.getValue());
            }
        } else {
            // 审核拒绝
            refundOrder.setStatus(RefundStatusEnum.Rejected_after_review.getValue());
            refundOrder.setHandleNote(auditDTO.getHandleNote());
        }

        refundOrder.setHandleTime(now());
        //处理人
        refundOrder.setHandleBy(auditDTO.getHandleBy());

        refundOrderConverter.toEntity(refundOrder);


        //记录操作日志
        RefundOperationLogForm formData = new RefundOperationLogForm();
        formData.setRefundId(auditDTO.getId());
        formData.setOperationType(OperationTypeEnum.Merchant_processing.getValue()); //操作类型：商家处理
        formData.setOperationContent(auditDTO.getRefundReason());
        formData.setOperatorId(auditDTO.getId());
        formData.setOperatorType(OperatorTypeEnum.merchant.getValue());// 操作人类型，商家

        boolean result = refundOperationLogService.saveRefundOperationLog(formData);

        return Result.judge(result);
    }
}
