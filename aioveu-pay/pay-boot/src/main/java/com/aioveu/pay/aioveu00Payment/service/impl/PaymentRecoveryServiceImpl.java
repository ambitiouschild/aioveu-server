package com.aioveu.pay.aioveu00Payment.service.impl;


import com.aioveu.common.enums.pay.PaymentStatusEnum;
import com.aioveu.pay.aioveu00Payment.Processor.Impl.BusinessProcessorComposite;
import com.aioveu.pay.aioveu00Payment.service.PaymentRecoveryService;
import com.aioveu.pay.aioveu01.service.WechatPay.service.WeChatPayService;
import com.aioveu.pay.aioveu01PayOrder.mapper.PayOrderMapper;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu00Payment.Processor.BusinessProcessor;
import com.aioveu.pay.aioveu12MqProducerPayment.Publisher.PaymentEventPublisher;
import com.aioveu.pay.model.aioveuPayment.PaymentStatusVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @ClassName: PaymentRecoveryServiceImpl
 * @Description TODO PaymentRecoveryServiceImpl
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/20 19:31
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRecoveryServiceImpl implements PaymentRecoveryService {


    private PayOrderMapper payOrderMapper;
    private WeChatPayService weChatPayService;
    //Spring 会自动注入 唯一实现类（如果有多个再配合 @Qualifier）。
    private final BusinessProcessorComposite businessProcessorComposite;

    private final PaymentEventPublisher paymentEventPublisher;

    /**
     * 单笔订单兜底查单（Job / 回调触发）
     */
    public void recover(String paymentNo) {






        PayOrder payOrder = payOrderMapper.selectOne(
                Wrappers.<PayOrder>lambdaQuery()
                        .eq(PayOrder::getPaymentNo, paymentNo)
        );

        if (payOrder == null || PaymentStatusEnum.isTerminal(payOrder.getPaymentStatus())){
            return;
        }

        // 5 分钟内查过，直接跳过
        if (skipByRecentQuery(payOrder)) {
            return;
        }

        try {
            PaymentStatusVO wx = weChatPayService.queryPayment(paymentNo);

            if (wx == null) {
                return;
            }

            PaymentStatusEnum statusEnum = wx.getPaymentStatus();
            if (!PaymentStatusEnum.isTerminal(statusEnum)) {
                payOrderMapper.updateLastQueryTime(
                        payOrder.getId(),
                        LocalDateTime.now()
                );
                return;
            }


            // 支付状态必须先落库（你已经做到了）
            boolean updated = updateLocalStatus(payOrder, wx);
            // 只有真正更新支付状态时，才触发业务处理（防止回调/Job并发重复投递）

            //⚠️ 一个“并发边缘风险”
            //recover()可能被并发调用
            //解决方案 1（推荐）：乐观锁 + 状态双判
            //✅ 解决方案 2（更保险）：业务幂等（可选）
            if (updated && PaymentStatusEnum.PAID == wx.getPaymentStatus()) {

//                if (alreadyProcessed(paymentNo)) {
//                    log.warn("支付单业务已处理，跳过, paymentNo={}", paymentNo);
//                    return;
//                }

                PayOrder freshOrder = payOrderMapper.selectById(payOrder.getId());

                //MQ 消息应该是“更新后的事实”
                paymentEventPublisher.publishPaymentSuccess(freshOrder);
                triggerBusinessProcess(paymentNo);

            } else {
                // 失败 / 关闭：只更新状态，不推进业务
                log.info("兜底查单确认非成功终态, paymentNo={}, status={}",
                        paymentNo, wx.getPaymentStatus());
            }


        } catch (Exception e) {
            log.error("兜底查单异常, paymentNo={}", paymentNo, e);
        }
    }

    private void triggerBusinessProcess(String paymentNo) {
        try {
            businessProcessorComposite.onPaid(paymentNo);
        } catch (Exception e) {
            log.error("支付成功业务处理失败, paymentNo={}", paymentNo, e);
            // 不抛异常，避免影响支付状态
        }
    }

    //确保 updateStatusIfNonTerminal使用了乐观锁
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLocalStatus(PayOrder order, PaymentStatusVO wx) {
        int rows = payOrderMapper.updateStatusIfNonTerminal(
                order.getId(),
                wx.getPaymentStatus(),
                wx.getThirdPaymentNo(),
                wx.getPaymentTime()
        );
        return rows == 1;
    }

    private boolean skipByRecentQuery(PayOrder order) {
        return order.getLastQueryTime() != null
                && order.getLastQueryTime().isAfter(LocalDateTime.now().minusMinutes(5));
    }


}
