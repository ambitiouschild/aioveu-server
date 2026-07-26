package com.aioveu.pay.aioveu00Payment.service.impl;


import com.aioveu.common.enums.pay.CallbackTriggerSourceEnum;
import com.aioveu.common.enums.pay.PaymentCallbackStatusEnum;
import com.aioveu.common.enums.pay.PaymentStatusEnum;
import com.aioveu.pay.aioveu00Payment.Processor.Impl.BusinessProcessorComposite;
import com.aioveu.pay.aioveu00Payment.service.PayOrderSuccessHandlerService;
import com.aioveu.pay.aioveu00Payment.service.PaymentRecoveryService;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import com.aioveu.pay.aioveu06PayFlow.service.PayFlowService;
import com.aioveu.pay.aioveu12MqProducerPayment.Publisher.PaymentEventPublisher;
import com.aioveu.pay.aioveu13PayCallbackRecord.service.PayCallbackRecordService;
import com.aioveu.pay.model.aioveuPayment.PaymentCallbackDTO;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @ClassName: PayOrderSuccessHandlerImpl
 * @Description TODO 支付确认成功后的统一处理
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/25 23:12
 * @Version 1.0
 **/

@Component
@RequiredArgsConstructor
@Slf4j
public class PayOrderSuccessHandlerServiceImpl implements PayOrderSuccessHandlerService {

    private final PayOrderService payOrderService;
    private final PayFlowService payFlowService;
    private final PayCallbackRecordService payCallbackRecordService;
    private final PaymentEventPublisher paymentEventPublisher;
    private final BusinessProcessorComposite businessProcessorComposite;

    /**
     * 统一处理支付成功
     *
     * @param paymentNo     支付单号
     * @param transactionId 第三方交易号
     * @param paidTime      支付时间
     * @param rawParams     原始参数（微信回调传 Map，轮询传 WechatPayQueryResult，Job 传查库结果）
     * @param source        来源标识（"WECHAT_CALLBACK" / "POLLING" / "JOB"）
     */
    @Transactional(rollbackFor = Exception.class)
    public void handlePaySuccess(
            String paymentNo,
            String transactionId,
            LocalDateTime paidTime,
            Object rawParams,
            CallbackTriggerSourceEnum source
    ) {
        log.info("【支付成功处理】来源={}, paymentNo={}, transactionId={}",
                source, paymentNo, transactionId);

        // 1. 查 PayOrder（带乐观锁版本）
        PayOrder payOrder = payOrderService.getByPaymentNo(paymentNo);
        Integer oldVersion = payOrder.getVersion();
        log.error(">>>>>> oldVersion={}", oldVersion); // 确认有值
        if (payOrder == null) {
            log.error("【支付成功处理】支付单不存在: paymentNo={}", paymentNo);
            return;
        }

        // 2. 幂等：终态直接返回
        if (PaymentStatusEnum.isTerminal(payOrder.getPaymentStatus())) {
            log.info("【支付成功处理】已是终态，跳过, paymentNo={}, status={}",
                    paymentNo, payOrder.getPaymentStatus());
            return;
        }

        // ✅ 在这里加：记录回调/触发来源 // ✅ 统一记录（所有来源都记，但用 source 区分）
        payCallbackRecordService.saveCallbackRecord(paymentNo, transactionId, paidTime, rawParams, source);


        // 3. 更新 PayOrder
        // 3. 直接在这个对象上改字段（version 已经在里面了，别动它）
        payOrder.setPaymentStatus(PaymentStatusEnum.PAID);
        payOrder.setThirdTransactionNo(transactionId);
        payOrder.setPaymentTime(paidTime != null ? paidTime : LocalDateTime.now());

        // 4. updateById（MP 乐观锁自动用 payOrder.version 做 CAS）
        boolean updated =  payOrderService.updateById(payOrder);
        if (!updated) {
            log.warn("【支付成功处理】并发冲突，更新失败, paymentNo={}", paymentNo);
            return;
        }

        // 4. 回调记录（只有微信回调需要，轮询/Job 可以不写或写个简化版）
        if ("WECHAT_CALLBACK".equals(source) && rawParams instanceof Map) {
            payCallbackRecordService.markConsumed(
                    transactionId, paymentNo, payOrder.getOrderNo(), (Map<String, String>) rawParams
            );
        }

        // 5. 支付流水
        PaymentCallbackDTO flowDto = buildFlowDto(payOrder, transactionId, rawParams);
        payFlowService.recordPaymentFlow(payOrder, flowDto);

        // 6. ✅ 发 MQ
        paymentEventPublisher.publishPaymentSuccess(payOrder);

        // 7. 业务处理（放这里，MQ 之后） 谁改 PAID 谁负责，只此一家，别无分店。 👍
        try {
            businessProcessorComposite.onPaid(paymentNo);
        } catch (Exception e) {
            log.error("【支付成功处理】业务处理失败, paymentNo={}", paymentNo, e);
        }

        log.info("【支付成功处理】完成, paymentNo={}, source={}", paymentNo, source);
    }

    private PaymentCallbackDTO buildFlowDto(PayOrder payOrder, String transactionId, Object rawParams) {
        PaymentCallbackDTO dto = new PaymentCallbackDTO();
        dto.setPaymentNo(payOrder.getPaymentNo());
        dto.setOrderNo(payOrder.getOrderNo());
        dto.setChannel("WECHAT");
        dto.setThirdTransactionId(transactionId);
        dto.setPaidAmount(payOrder.getPaymentAmount());
        dto.setPaidTime(LocalDateTime.now());
        dto.setStatus(PaymentCallbackStatusEnum.SUCCESS);
        dto.setRawData(rawParams != null ? JSON.toJSONString(rawParams) : "");
        return dto;
    }


    /**
     * 处理支付失败/关闭（终态但不是 PAID）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePayFail(String paymentNo, PaymentStatusEnum targetStatus, CallbackTriggerSourceEnum source) {
        log.info("【支付失败处理】来源={}, paymentNo={}, status={}", source, paymentNo, targetStatus);

        PayOrder payOrder = payOrderService.getByPaymentNo(paymentNo);
        Integer oldVersion = payOrder.getVersion();
        log.error(">>>>>> oldVersion={}", oldVersion); // 确认有值
        if (payOrder == null) return;

        // 终态保护
        if (PaymentStatusEnum.isTerminal(payOrder.getPaymentStatus())) {
            log.info("【支付失败处理】已是终态，跳过, paymentNo={}", paymentNo);
            return;
        }

        // 更新为失败/关闭
        PayOrder update = new PayOrder();
        update.setId(payOrder.getId());
        update.setPaymentStatus(targetStatus);
        update.setVersion(payOrder.getVersion());
        boolean updated = payOrderService.updateById(update);
        if (!updated) {
            log.warn("【支付失败处理】并发冲突, paymentNo={}", paymentNo);
            return;
        }

        // 失败不发 MQ，只记流水
        log.info("【支付失败处理】订单标记为失败, paymentNo={}, status={}", paymentNo, targetStatus);
    }

}
