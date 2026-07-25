package com.aioveu.pay.aioveu00Payment.service.impl;


import com.aioveu.common.enums.pay.PaymentStatusEnum;
import com.aioveu.pay.aioveu00Payment.Processor.Impl.BusinessProcessorComposite;
import com.aioveu.pay.aioveu00Payment.service.PayOrderSuccessHandlerService;
import com.aioveu.pay.aioveu00Payment.service.PaymentRecoveryService;
import com.aioveu.pay.aioveu01.service.WechatPay.service.WeChatPayService;
import com.aioveu.pay.aioveu01PayOrder.mapper.PayOrderMapper;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu12MqProducerPayment.Publisher.PaymentEventPublisher;
import com.aioveu.pay.model.aioveuPayQueryResultAdapter.WechatPayQueryResult;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

    //核心思路：Job 只做"查 + 调 handler"
    private final PayOrderSuccessHandlerService payOrderSuccessHandlerService; // ✅ 统一 handler


    /**
     * 单笔订单兜底查单（Job / 回调触发）
     */
    @Override
    public void recover(String paymentNo) {

        PayOrder payOrder = payOrderMapper.selectOne(
                Wrappers.<PayOrder>lambdaQuery()
                        .eq(PayOrder::getPaymentNo, paymentNo)
        );

        if (payOrder == null || PaymentStatusEnum.isTerminal(payOrder.getPaymentStatus())){
            return;
        }

        // 1. 不存在或已是终态 → 跳过
        if (payOrder == null || PaymentStatusEnum.isTerminal(payOrder.getPaymentStatus())) {
            return;
        }

        // 2. 5 分钟内查过 → 跳过（节流）
        if (skipByRecentQuery(payOrder)) {
            return;
        }

        try {

            //3.查询微信状态
            WechatPayQueryResult wx = weChatPayService.queryPayment(paymentNo);

            if (wx == null) {
                return;
            }

            //  // 4. 非终态 → 只更新查询时间，不推进
            PaymentStatusEnum wxStatus = wx.getPaymentStatus();
            if (!PaymentStatusEnum.isTerminal(wxStatus)) {
                payOrderMapper.updateLastQueryTime(
                        payOrder.getId(),
                        LocalDateTime.now()
                );
                return;
            }

            // 5. ✅ 终态 → 调统一 handler（不管成功失败都让它处理）
            log.info("【Job兜底】查到终态, paymentNo={}, status={}", paymentNo, wxStatus);
            //⚠️ 一个“并发边缘风险”
            //recover()可能被并发调用
            //解决方案 1（推荐）：乐观锁 + 状态双判
            //✅ 解决方案 2（更保险）：业务幂等（可选）
            if (wxStatus == PaymentStatusEnum.PAID) {

                // ✅ 统一入口，handler 里做：更新DB + 流水 + MQ + 业务
                payOrderSuccessHandlerService.handlePaySuccess(
                        paymentNo,
                        wx.getThirdPaymentNo(),
                        wx.getPaymentTime(),
                        wx,
                        "JOB"
                );

            } else {
                // 失败 / 关闭：只更新状态，不推进业务
                log.info("兜底查单确认非成功终态, paymentNo={}, status={}",
                        paymentNo, wx.getPaymentStatus());
                // 失败/关闭：也走 handler（或者只更新状态，不发 MQ）
                // 建议也走 handler，handler 内部会根据 status 决定是否发 MQ
                payOrderSuccessHandlerService.handlePayFail(
                        paymentNo,
                        wxStatus,
                        "JOB"
                );
            }


        } catch (Exception e) {
            log.error("兜底查单异常, paymentNo={}", paymentNo, e);
        }
    }

    //确保 updateStatusIfNonTerminal使用了乐观锁
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLocalStatus(PayOrder order, WechatPayQueryResult wx) {
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
