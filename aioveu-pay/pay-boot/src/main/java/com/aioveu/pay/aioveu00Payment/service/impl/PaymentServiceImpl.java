package com.aioveu.pay.aioveu00Payment.service.impl;

import com.aioveu.pay.aioveu00Payment.service.PaymentService;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import com.aioveu.pay.aioveu08PayAccount.service.PayAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: aa
 * @Description TODO 主要业务逻辑接口实现
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 20:53
 * @Version 1.0
 **/

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private PayAccountService payAccountService;

    @Autowired
    private ChannelRouter channelRouter;

    @Autowired
    private PaymentStrategyFactory strategyFactory;

    /**
     * 统一支付接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<PaymentResultVO> unifiedPayment(PaymentRequestDTO request) {
        try {
            // 1. 参数校验
            validatePaymentRequest(request);

            // 2. 创建支付订单
            PayOrderCreateDTO orderDTO = buildPayOrderDTO(request);
            Result<String> createResult = payOrderService.createPayOrder(orderDTO);
            if (!createResult.isSuccess()) {
                return Result.error(createResult.getCode(), createResult.getMessage());
            }

            String paymentNo = createResult.getData();

            // 3. 根据支付渠道选择支付策略
            PaymentStrategy strategy = strategyFactory.getStrategy(request.getChannel());
            PaymentParamsVO params = strategy.pay(paymentNo, request);

            // 4. 返回支付参数
            PaymentResultVO result = PaymentResultVO.builder()
                    .paymentNo(paymentNo)
                    .paymentParams(params)
                    .paymentStatus(PaymentStatus.PENDING.getCode())
                    .build();

            return Result.success(result);

        } catch (BusinessException e) {
            log.error("支付业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("支付系统异常", e);
            throw new BusinessException("支付系统异常");
        }
    }

    /**
     * 处理支付回调
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> handleCallback(PaymentCallbackDTO callback) {
        try {
            // 1. 验证回调签名
            if (!verifyCallbackSign(callback)) {
                throw new BusinessException("回调签名验证失败");
            }

            // 2. 查询支付订单
            PayOrder order = payOrderMapper.selectByPaymentNo(callback.getPaymentNo());
            if (order == null) {
                throw new BusinessException("支付订单不存在");
            }

            // 3. 避免重复处理
            if (order.getPaymentStatus() != PaymentStatus.PENDING.getCode()
                    && order.getPaymentStatus() != PaymentStatus.PROCESSING.getCode()) {
                log.warn("订单已处理，跳过回调: paymentNo={}, status={}",
                        callback.getPaymentNo(), order.getPaymentStatus());
                return Result.success();
            }

            // 4. 更新订单状态
            if (callback.isSuccess()) {
                order.setPaymentStatus(PaymentStatus.SUCCESS.getCode());
                order.setPaymentTime(callback.getPaymentTime());
                order.setThirdPaymentNo(callback.getThirdPaymentNo());
                order.setThirdTransactionNo(callback.getTransactionNo());
            } else {
                order.setPaymentStatus(PaymentStatus.FAILED.getCode());
                order.setErrorCode(callback.getErrorCode());
                order.setErrorMessage(callback.getErrorMessage());
            }

            // 5. 更新余额（如果是余额支付）
            if (PaymentChannel.BALANCE.getCode().equals(order.getPaymentChannel())) {
                handleBalancePayment(order);
            }

            // 6. 记录支付流水
            createPaymentFlow(order);

            // 7. 发送异步通知
            sendPaymentNotify(order);

            return Result.success();

        } catch (Exception e) {
            log.error("处理支付回调异常", e);
            throw new BusinessException("处理支付回调失败");
        }
    }

    /**
     * 统一退款接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<RefundResultVO> unifiedRefund(RefundRequestDTO request) {
        try {
            // 1. 参数校验
            validateRefundRequest(request);

            // 2. 创建退款记录
            RefundApplyDTO applyDTO = buildRefundApplyDTO(request);
            Result<String> createResult = refundService.applyRefund(applyDTO);
            if (!createResult.isSuccess()) {
                return Result.error(createResult.getCode(), createResult.getMessage());
            }

            String refundNo = createResult.getData();

            // 3. 执行退款
            RefundStrategy strategy = refundStrategyFactory.getStrategy(request.getChannel());
            RefundResultVO result = strategy.refund(refundNo, request);

            return Result.success(result);

        } catch (BusinessException e) {
            log.error("退款业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("退款系统异常", e);
            throw new BusinessException("退款系统异常");
        }
    }
}
