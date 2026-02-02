package com.aioveu.refund.aioveu06RefundPayment.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.refund.aioveu01RefundOrder.enums.RefundStatusEnum;
import com.aioveu.refund.aioveu01RefundOrder.model.entity.RefundOrder;
import com.aioveu.refund.aioveu01RefundOrder.service.RefundOrderService;
import com.aioveu.refund.aioveu04RefundOperationLog.enums.OperationTypeEnum;
import com.aioveu.refund.aioveu04RefundOperationLog.model.form.RefundOperationLogForm;
import com.aioveu.refund.aioveu04RefundOperationLog.service.RefundOperationLogService;
import com.aioveu.refund.aioveu06RefundPayment.model.entity.RefundPayment;
import com.aioveu.refund.aioveu06RefundPayment.model.form.ExecuteRefundFormDTO;
import com.aioveu.refund.aioveu06RefundPayment.model.form.RefundPaymentForm;
import com.aioveu.refund.aioveu06RefundPayment.model.query.RefundPaymentQuery;
import com.aioveu.refund.aioveu06RefundPayment.model.vo.RefundPaymentVO;
import com.aioveu.refund.aioveu06RefundPayment.service.RefundPaymentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;

/**
 * @ClassName: RefundPaymentController
 * @Description TODO 退款支付记录前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:48
 * @Version 1.0
 **/

@Slf4j
@Tag(name = "退款支付记录接口")
@RestController
@RequestMapping("/api/v1/refund-payment")
@RequiredArgsConstructor
public class RefundPaymentController {

    private final RefundPaymentService refundPaymentService;

    private final RefundOrderService refundOrderService;

    private final RefundOperationLogService refundOperationLogService;

    @Operation(summary = "退款支付记录分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundPayment:refund-payment:query')")
    public PageResult<RefundPaymentVO> getRefundPaymentPage(RefundPaymentQuery queryParams ) {
        IPage<RefundPaymentVO> result = refundPaymentService.getRefundPaymentPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增退款支付记录")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundPayment:refund-payment:add')")
    public Result<Void> saveRefundPayment(@RequestBody @Valid RefundPaymentForm formData ) {
        boolean result = refundPaymentService.saveRefundPayment(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取退款支付记录表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundPayment:refund-payment:edit')")
    public Result<RefundPaymentForm> getRefundPaymentForm(
            @Parameter(description = "退款支付记录ID") @PathVariable Long id
    ) {
        RefundPaymentForm formData = refundPaymentService.getRefundPaymentFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改退款支付记录")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundPayment:refund-payment:edit')")
    public Result<Void> updateRefundPayment(
            @Parameter(description = "退款支付记录ID") @PathVariable Long id,
            @RequestBody @Validated RefundPaymentForm formData
    ) {
        boolean result = refundPaymentService.updateRefundPayment(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除退款支付记录")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundPayment:refund-payment:delete')")
    public Result<Void> deleteRefundPayments(
            @Parameter(description = "退款支付记录ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = refundPaymentService.deleteRefundPayments(ids);
        return Result.judge(result);
    }


    @Operation(summary = "执行退款")
    @Transactional
    @PostMapping("/execute/{refundSn}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundPayment:refund-payment:add')")
    public Result<Void> executeRefund(
            @PathVariable String refundSn,
            @RequestBody @Valid ExecuteRefundFormDTO formData ) {


        RefundOrder refundOrder = refundOrderService.getOne(
                    new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getRefundSn, refundSn)
        );

        log.info("【执行退款】1.验证退款订单:{}",refundOrder);


        // 2. 调用支付系统退款
        log.info("【执行退款】2.调用支付系统退款");


        // 3. 记录支付记录
        RefundPayment payment = new RefundPayment();
        payment.setRefundId(refundOrder.getId());
//        payment.setPaymentSn(paymentResult.getPaymentNo());
        payment.setPaymentType(formData.getPaymentType());
        payment.setPaymentAmount(refundOrder.getRefundAmount());

//        payment.setPaymentStatus(paymentResult.isSuccess() ? 1 : 2);
        payment.setPaymentTime(now());
        payment.setPaymentChannel(formData.getPaymentChannel());
        payment.setPaymentTradeNo(formData.getPaymentTradeNo());
        payment.setPaymentFee(formData.getPaymentFee());
//        payment.setRemark(paymentResult.getMessage());
        log.info("【执行退款】3.记录支付记录");


        // 4. 更新退款订单状态
//        if (paymentResult.isSuccess()) {
                refundOrder.setStatus(RefundStatusEnum.Refund_successful.getValue()); //退款成功
//                refundOrder.setRefundTime(new Date());
//                refundOrder.setPaymentNo(paymentResult.getPaymentNo());
//        } else {
//            order.setRefundStatus(RefundStatus.REFUND_FAILED.getCode());
//        }
        refundOrderService.updateById(refundOrder);
        log.info("【执行退款】4.更新退款订单状态");


        //构建操作记录
        RefundOperationLogForm refundOperationLogForm = new RefundOperationLogForm();
        refundOperationLogForm.setRefundId(refundOrder.getId());
        refundOperationLogForm.setOperationType(OperationTypeEnum.System_automatic.getValue()); //系统自动
        refundOperationLogForm.setOperationContent( "商家已确认收货，商品状况：");
//        refundOperationLogForm.setOperatorId(formData.getOperatorId());  //操作人ID
//        refundOperationLogForm.setOperatorName(formData.getOperatorName());  //操作人姓名

        //保存操作记录
        boolean result = refundOperationLogService.saveRefundOperationLog(refundOperationLogForm);
        log.info("【执行退款】5. 记录操作日志");


        return Result.judge(result);
    }
}
