package com.aioveu.pay.aioveu01.service.MockPay.service.impl;

import com.aioveu.common.enums.pay.PaymentChannelEnum;
import com.aioveu.common.enums.pay.PaymentMethodEnum;
import com.aioveu.common.enums.pay.PaymentStatusEnum;
import com.aioveu.common.enums.pay.RefundStatusEnum;
import com.aioveu.pay.aioveu01.service.MockPay.MockRequestFactory.MockRequestFactory;
import com.aioveu.pay.aioveu01.service.MockPay.config.MockPayConfig;
import com.aioveu.pay.aioveu01.service.MockPay.service.MockPayService;
import com.aioveu.pay.aioveu01.service.WechatPay.utils.weChatPay.aioveuWeChatPayGeneratePayParamsUtil;
import com.aioveu.pay.model.aioveuPayAdapter.MockPayQueryResult;
import com.aioveu.pay.model.aioveuPayment.PaymentParamsVO;
import com.aioveu.pay.model.aioveuPayment.RefundRequestDTO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestPayToTPPDTO;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static com.aioveu.pay.utils.LocalDateTimeUtil.toLocalDateTime;
import static com.fasterxml.jackson.core.io.NumberInput.parseBigDecimal;
import static org.bouncycastle.math.ec.rfc8032.Ed25519.sign;


/**
 * @ClassName: MockPayServiceImpl
 * @Description TODO  模拟支付服务实现  - 返回支付参数
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 18:43
 * @Version 1.0
 **/

@Slf4j
@Service
@Primary  // 优先使用这个Mock实现
public class MockPayServiceImpl implements MockPayService {

    private final MockRequestFactory requestFactory;
    @Autowired
    private aioveuWeChatPayGeneratePayParamsUtil aioveuWeChatPayGeneratePayParamsUtil;

    private final MockPayConfig mockPayConfig;
    private final Random random = new Random();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public MockPayServiceImpl(MockRequestFactory requestFactory,
                              MockPayConfig mockPayConfig) {
        this.requestFactory = requestFactory;
        this.mockPayConfig = mockPayConfig;
        log.info("【Mock】模拟支付服务初始化完成");
    }

    @Override
    public PaymentParamsVO jsapiPay(PaymentRequestPayToTPPDTO request) {

        try {
                if (!mockPayConfig.getEnabled()) {
                    throw new RuntimeException("【Mock】模拟支付未启用");
                }


                String payOrderNo = request.getPayOrderNo();
                BigDecimal amount = request.getPaymentAmount();
                String openId = request.getOpenId();

                // 记录请求
                if (mockPayConfig.getLogRequest()) {
                    log.info("【Mock】支付订单: {}, 金额: {}分, OpenID: {}",
                            payOrderNo, amount, openId);
                }

                // 模拟延迟
                simulateDelay();

                // 模拟支付结果
                boolean success = mockPayConfig.shouldSuccess();

                String prepayId = "这是我要测试的预付ID";
                // 生成支付参数
                Map<String, Object> payParams = aioveuWeChatPayGeneratePayParamsUtil.generateJsapiPayParams();

                // 生成支付参数
                return PaymentParamsVO.builder()
//                        .payOrderNo(request.getPayOrderNo())
                        .orderSn(request.getOrderSn())
//                        .amount(request.getPaymentAmount())
//                        .subject(request.getSubject())
//                        .body(request.getBody())
                        .paymentChannel(PaymentChannelEnum.MOCK)
                        .paymentMethod(PaymentMethodEnum.JSAPI)
                        .prepayId(prepayId)
                        .payParams(payParams)
//                        .createTime(System.currentTimeMillis())
//                        .expireTime(System.currentTimeMillis() + 30 * 60 * 1000) // 30分钟
                        .build();

            }catch (Exception e) {
                log.error("【Mock】模拟支付jsapiPay支付失败, 支付订单号: {}", request.getPayOrderNo(), e);
                throw new RuntimeException("【Mock】模拟支付jsapiPay支付失败", e);  //你调用了一个声明抛出 Exception的方法，但没有处理这个异常。
        }

    }



    @Override
    public PaymentParamsVO appPay(PaymentRequestPayToTPPDTO request) {

        try {
            if (!mockPayConfig.getEnabled()) {
                throw new RuntimeException("【Mock】模拟支付未启用");
            }


            String payOrderNo = request.getPayOrderNo();
            BigDecimal amount = request.getPaymentAmount();
            String openId = request.getOpenId();

            // 记录请求
            if (mockPayConfig.getLogRequest()) {
                log.info("【Mock】 支付订单: {}, 金额: {}分, OpenID: {}",
                        payOrderNo, amount, openId);
            }

            // 模拟延迟
            simulateDelay();

            // 模拟支付结果
            boolean success = mockPayConfig.shouldSuccess();

            String prepayId = "111";
            // 生成支付参数
            Map<String, Object> payParams = aioveuWeChatPayGeneratePayParamsUtil.generateJsapiPayParams();

            // 生成支付参数
            return PaymentParamsVO.builder()
//                    .payOrderNo(request.getPayOrderNo())
                    .orderSn(request.getOrderSn())
//                    .amount(request.getPaymentAmount())
//                    .subject(request.getSubject())
//                    .body(request.getBody())
                    .paymentChannel(PaymentChannelEnum.MOCK)
                    .paymentMethod(PaymentMethodEnum.APP)
                    .prepayId(prepayId)
                    .payParams(payParams)
//                    .createTime(System.currentTimeMillis())
//                    .expireTime(System.currentTimeMillis() + 30 * 60 * 1000) // 30分钟
                    .build();

        }catch (Exception e) {
            log.error("【Mock】模拟支付app支付失败, 订单号: {}", request.getPayOrderNo(), e);
            throw new RuntimeException("【Mock】模拟支付app支付失败", e);  //你调用了一个声明抛出 Exception的方法，但没有处理这个异常。
        }
    }

    @Override
    public PaymentParamsVO h5Pay(PaymentRequestPayToTPPDTO request) {

        try {
            if (!mockPayConfig.getEnabled()) {
                throw new RuntimeException("【Mock】模拟支付未启用");
            }


            String payOrderNo = request.getPayOrderNo();
            BigDecimal amount = request.getPaymentAmount();
            String openId = request.getOpenId();

            // 记录请求
            if (mockPayConfig.getLogRequest()) {
                log.info("【Mock】 支付订单: {}, 金额: {}分, OpenID: {}",
                        payOrderNo, amount, openId);
            }

            // 模拟延迟
            simulateDelay();

            // 模拟支付结果
            boolean success = mockPayConfig.shouldSuccess();

            String prepayId = "111";
            // 生成支付参数
            Map<String, Object> payParams = aioveuWeChatPayGeneratePayParamsUtil.generateJsapiPayParams();

            // 生成支付参数
            return PaymentParamsVO.builder()
//                    .payOrderNo(request.getPayOrderNo())
                    .orderSn(request.getOrderSn())
//                    .amount(request.getPaymentAmount())
//                    .subject(request.getSubject())
//                    .body(request.getBody())
                    .paymentChannel(PaymentChannelEnum.MOCK)
                    .paymentMethod(PaymentMethodEnum.H5)
                    .prepayId(prepayId)
                    .payParams(payParams)
//                    .createTime(System.currentTimeMillis())
//                    .expireTime(System.currentTimeMillis() + 30 * 60 * 1000) // 30分钟
                    .build();

        }catch (Exception e) {
            log.error("【Mock】模拟支付H5支付失败, 支付订单号: {}", request.getPayOrderNo(), e);
            throw new RuntimeException("【Mock】模拟支付H5支付失败", e);  //你调用了一个声明抛出 Exception的方法，但没有处理这个异常。
        }
    }

    private void simulateDelay() {
        if (mockPayConfig.getDelay() > 0) {
            try {
                Thread.sleep(mockPayConfig.getDelay());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    /**

     */
    @Override
    public MockPayQueryResult queryPayment(String paymentNo)  {
        try {
            log.info("查询支付宝订单状态, 订单号: {}", paymentNo);

            if (paymentNo == null || paymentNo.trim().isEmpty()) {
                throw new IllegalArgumentException("支付订单号不能为空");
            }

            // 创建查询请求参数
            Map<String, String> params = requestFactory.createQueryRequest(paymentNo, mockPayConfig);
            log.debug("支付宝查询订单请求参数: {}", params);

            // 创建查询请求
            AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
            alipayRequest.setBizContent(params.get("biz_content"));

            // 执行请求
//            AlipayTradeQueryResponse response = alipayClient.execute(alipayRequest);
            AlipayTradeQueryResponse response = new AlipayTradeQueryResponse();

            MockPayQueryResult result = convertToPaymentStatus(response);
            log.info("查询支付宝订单状态成功, 订单号: {}, 状态: {}",
                    paymentNo, result.getPaymentStatus());

            return result;

        } catch (Exception e) {
            log.error("查询支付宝订单状态失败, 订单号: {}", paymentNo, e);
            throw new RuntimeException("查询支付宝订单状态失败: " + e.getMessage(), e);
        }
    }

    /**
     * 退款
     */
    @Override
    public com.aioveu.pay.model.aioveuPayment.RefundResultVO refund(RefundRequestDTO request)  {
        try {
            log.info("支付宝退款, 退款单号: {}, 支付订单号: {}, 退款金额: {}",
                    request.getRefundNo(), request.getPaymentNo(), request.getRefundAmount());

            // 验证退款请求
            validateRefundRequest(request);

            // 创建退款请求参数
            Map<String, String> params = requestFactory.createRefundRequest(
                    request.getRefundNo(), request.getPaymentNo(),
                    request.getRefundAmount(), request.getRefundReason(), mockPayConfig);
            log.debug("支付宝退款请求参数: {}", params);

            // 创建退款请求
            AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
            alipayRequest.setBizContent(params.get("biz_content"));

            // 执行请求
//            AlipayTradeRefundResponse response = alipayClient.execute(alipayRequest);

            AlipayTradeRefundResponse response = new AlipayTradeRefundResponse();

            com.aioveu.pay.model.aioveuPayment.RefundResultVO result = convertToRefundResult(request, response);
            log.info("支付宝退款成功, 退款单号: {}", request.getRefundNo());

            return result;

        } catch (Exception e) {
            log.error("支付宝退款失败, 退款单号: {}", request.getRefundNo(), e);
            throw new RuntimeException("支付宝退款失败: " + e.getMessage(), e);
        }
    }

    /**
     * 关闭订单
     */
    @Override
    public boolean closePayment(String paymentNo)  {
        try {
            log.info("关闭支付宝订单, 订单号: {}", paymentNo);

            if (paymentNo == null || paymentNo.trim().isEmpty()) {
                throw new IllegalArgumentException("支付订单号不能为空");
            }

            // 创建关闭请求参数
            Map<String, String> params = requestFactory.createCloseRequest(paymentNo, mockPayConfig);
            log.debug("支付宝关闭订单请求参数: {}", params);

            // 创建关闭请求
            AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
            alipayRequest.setBizContent(params.get("biz_content"));

            // 执行请求
//            AlipayTradeCloseResponse response = alipayClient.execute(alipayRequest);

            AlipayTradeQueryResponse response = new AlipayTradeQueryResponse();

            boolean success = true;
            log.info("关闭支付宝订单结果, 订单号: {}, 成功: {}", paymentNo, success);

            return success;

        } catch (Exception e) {
            log.error("关闭支付宝订单失败, 订单号: {}", paymentNo, e);
            throw new RuntimeException("关闭支付宝订单失败: " + e.getMessage(), e);
        }
    }

    /**
     * 转换支付状态
     */
    private MockPayQueryResult convertToPaymentStatus(AlipayTradeQueryResponse response) {

        if (!true) {
            String errorMsg = String.format("查询失败: %s - %s",
                    response.getSubCode(), response.getSubMsg());
            throw new RuntimeException(errorMsg);
        }


        return MockPayQueryResult.builder()
                .paymentNo(response.getOutTradeNo())
                .thirdPaymentNo(response.getTradeNo())
                .amount(parseBigDecimal(response.getTotalAmount()))
                .paymentStatus(convertAlipayStatus(response.getTradeStatus()))
                .paymentTime(toLocalDateTime(response.getSendPayDate()))
                .build();
    }

    /**
     * 转换退款结果
     */
    private com.aioveu.pay.model.aioveuPayment.RefundResultVO convertToRefundResult(RefundRequestDTO request,
                                                                                    AlipayTradeRefundResponse response) {

        if (!true) {
            String errorMsg = String.format("退款失败: %s - %s",
                    response.getSubCode(), response.getSubMsg());
            throw new RuntimeException(errorMsg);
        }

        return com.aioveu.pay.model.aioveuPayment.RefundResultVO.builder()
                .refundNo(request.getRefundNo())
                .thirdRefundNo(response.getTradeNo())
                .refundAmount(parseBigDecimal(response.getRefundFee()))
                .refundStatus(RefundStatusEnum.SUCCESS.getValue())
                .refundTime(new Date())
                .build();
    }

    /**
     * 转换支付宝状态
     */
    private PaymentStatusEnum convertAlipayStatus(String tradeStatus) {
        if (tradeStatus == null) {
            return PaymentStatusEnum.FAILED;
        }

        switch (tradeStatus) {
            case "TRADE_SUCCESS":
            case "TRADE_FINISHED":
                return PaymentStatusEnum.PAID;
            case "WAIT_BUYER_PAY":
                return PaymentStatusEnum.PAYING;
            case "TRADE_CLOSED":
                return PaymentStatusEnum.CLOSED;
            default:
                return PaymentStatusEnum.FAILED;
        }
    }

    /**
     * 验证退款请求参数
     *
     * @param request 退款请求
     */
    private void validateRefundRequest(RefundRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("退款请求不能为空");
        }

        if (request.getRefundNo() == null || request.getRefundNo().trim().isEmpty()) {
            throw new IllegalArgumentException("退款单号不能为空");
        }

        if (request.getPaymentNo() == null || request.getPaymentNo().trim().isEmpty()) {
            throw new IllegalArgumentException("支付订单号不能为空");
        }

        if (request.getRefundAmount() == null ||
                request.getRefundAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("退款金额必须大于0");
        }
    }
}
