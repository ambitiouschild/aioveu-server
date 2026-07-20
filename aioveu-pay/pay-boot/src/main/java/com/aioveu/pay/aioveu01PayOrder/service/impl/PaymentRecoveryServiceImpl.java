package com.aioveu.pay.aioveu01PayOrder.service.impl;


import com.aioveu.common.enums.pay.PaymentStatusEnum;
import com.aioveu.pay.aioveu01.service.WechatPay.service.WeChatPayService;
import com.aioveu.pay.aioveu01PayOrder.mapper.PayOrderMapper;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.Processor.BusinessProcessor;
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
public class PaymentRecoveryServiceImpl {

    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private WeChatPayService weChatPayService;

    //Spring 会自动注入 唯一实现类（如果有多个再配合 @Qualifier）。
    @Resource
    private BusinessProcessor businessProcessor;

    /**
     * 单笔订单兜底查单
     */
    @Transactional(rollbackFor = Exception.class)
    public void recover(String paymentNo) {
        PayOrder order = payOrderMapper.selectOne(
                Wrappers.<PayOrder>lambdaQuery()
                        .eq(PayOrder::getPaymentNo, paymentNo)
        );

        if (order == null || PaymentStatusEnum.isTerminal(order.getPaymentStatus())){
            return;
        }

        // 5 分钟内查过，直接跳过
        if (order.getLastQueryTime() != null &&
                order.getLastQueryTime().isAfter(LocalDateTime.now().minusMinutes(5))) {
            return;
        }

        try {
            PaymentStatusVO wx = weChatPayService.queryPayment(paymentNo);

            PaymentStatusEnum statusEnum =
                    PaymentStatusEnum.fromCode(wx.getPaymentStatus());

            if (wx == null || !PaymentStatusEnum.isTerminal(statusEnum)) {
                payOrderMapper.updateLastQueryTime(
                        order.getId(),
                        LocalDateTime.now()
                );
                return;
            }


            // 支付状态必须先落库（你已经做到了）
            int rows = payOrderMapper.updateStatusIfNonTerminal(
                    order.getId(),
                    wx.getPaymentStatus(),
                    wx.getThirdPaymentNo(),
                    wx.getPaymentTime()
            );





            if (rows == 1) {
                log.info("兜底查单成功, paymentNo={}", paymentNo);


                //支付模块只认 Composite（关键） ✅ 支付模块从此不关心有多少个业务 // 实际是 Composite
                businessProcessor.onPaid(paymentNo);
            }

        } catch (Exception e) {
            log.error("兜底查单异常, paymentNo={}", paymentNo, e);
        }
    }
}
