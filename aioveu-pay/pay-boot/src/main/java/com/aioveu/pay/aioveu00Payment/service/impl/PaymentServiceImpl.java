package com.aioveu.pay.aioveu00Payment.service.impl;

import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.result.Result;
import com.aioveu.common.result.ResultCode;
import com.aioveu.pay.aioveu00Payment.service.PaymentService;
import com.aioveu.pay.aioveu01PayOrder.converter.PayOrderConverter;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import com.aioveu.pay.aioveu06PayFlow.service.PayFlowService;
import com.aioveu.pay.aioveu07PayNotify.service.PayNotifyService;
import com.aioveu.pay.aioveu08PayAccount.service.PayAccountService;
import com.aioveu.pay.aioveuModule.PaymentStrategy.PaymentStrategy;
import com.aioveu.pay.aioveuModule.PaymentStrategy.impl.PaymentStrategyFactory;
//import com.aioveu.pay.aioveuModule.channelRouter.ChannelRouter;
import com.aioveu.pay.aioveuModule.enums.PaymentStatusEnum;
import com.aioveu.pay.aioveuModule.model.vo.*;
import com.alibaba.fastjson.JSON;
import com.wechat.pay.java.service.refund.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
@RequiredArgsConstructor  // Lombok 自动生成构造函数
public class PaymentServiceImpl implements PaymentService {


    @Value("${pay.wechat.mch-key:}")
    private String wxMchKey;

    @Value("${pay.alipay.public-key:}")
    private String aliPublicKey;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayOrderConverter payOrderConverter;


    @Autowired
    //使用字段注入
    private PayAccountService payAccountService;

    //使用构造器注入（推荐）
    private final PayFlowService payFlowService;

    private PayNotifyService payNotifyService;

//    @Autowired
//    private ChannelRouter channelRouter;

    @Autowired
    private PaymentStrategyFactory strategyFactory;  // 策略工厂


    @Autowired
    public PaymentServiceImpl(PaymentStrategyFactory strategyFactory,
                              PayFlowService payFlowService) {
        this.strategyFactory = strategyFactory;
        this.payFlowService = payFlowService;
    }

    /**
     * 统一支付接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<PaymentParamsVO> unifiedPayment(PaymentRequestDTO request) {
        try {
            log.info("============【支付服务处理】统一支付接口============");

            // 1. 参数校验
//            validatePaymentRequest(request);

//             2. 创建支付订单
            PayOrder payOrder = payOrderConverter.toPayOrder(request);
            payOrderService.save(payOrder);
            String paymentNo = payOrder.getPaymentNo();
            log.info("【Pay】支付订单支付单号paymentNo：{}",paymentNo);

            // 3. 根据支付渠道选择支付策略
            PaymentStrategy strategy = strategyFactory.getStrategy(request.getChannel());
            log.info("【Pay】获取支付策略：{}",strategy.getClass().getSimpleName());

            // 4. 调用策略获取支付参数
            PaymentParamsVO params = strategy.appPay(paymentNo, request);
            log.info("【Pay】调用策略支付：{}", params);

            // 5. 直接返回支付参数VO
            return Result.success(params);

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

        String paymentNo = callback.getPaymentNo();
        log.info("【支付回调】开始处理，支付单号: {}", paymentNo);

        try {
            // ============ 1. 验证回调签名 ============
//            if (!verifyCallbackSign.verifyCallbackSign(callback)) {
//                log.error("【支付回调】签名验证失败: {}", paymentNo);
//                throw new BusinessException(ResultCode.CALLBACK_SIGN_ERROR, "回调签名验证失败");
//            }

            // ============ 2. 查询支付订单 ============
            PayOrder order = payOrderService.selectByPaymentNo(paymentNo);
            if (order == null) {
                log.error("【支付回调】订单不存在: {}", paymentNo);
                throw new BusinessException(ResultCode.ORDER_NOT_FOUND, "支付订单不存在");
            }

            // ============ 3. 避免重复处理 ============
            if (order.getPaymentStatus() != PaymentStatusEnum.PENDING.getValue()
                    && order.getPaymentStatus() != PaymentStatusEnum.PROCESSING.getValue()) {
                log.warn("订单已处理，跳过回调: paymentNo={}, status={}",
                        callback.getPaymentNo(), order.getPaymentStatus());
                return Result.success();
            }

            // ============ 4. 验证回调金额 ============


            // ============ 5. 更新订单状态 ============
            Boolean result = payOrderService.updateOrderStatus(order,callback);


            // ============ 6. 处理业务逻辑 ============
            payOrderService.handleBusinessLogic(order,callback);


            // ============ 7. 记录支付流水 ============
            payFlowService.recordPaymentFlow(order, callback);


            // ============ 8. 发送异步通知 ============
            payNotifyService.sendPaymentNotify(order, callback);

            log.info("【支付回调】处理完成: {}，状态: {}", paymentNo, order.getPaymentStatus());

            return Result.success();

        } catch (Exception e) {
            log.error("处理支付回调异常", e);
            throw new BusinessException("处理支付回调失败");
        }
    }

    /**
     * 处理微信回调
     */
    @Override
    public String handleWechatCallback(String xmlData) {
        try {
            // 1. 解析XML
            Map<String, String> params = parseWechatXml(xmlData);
            log.info("解析微信回调参数: {}", JSON.toJSONString(params));

            // 2. 验证签名
            if (!verifyWechatSign(params)) {
                log.error("微信签名验证失败");
                return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名验证失败]]></return_msg></xml>";
            }

            // 3. 获取订单号
            String orderNo = params.get("out_trade_no");
            if (!StringUtils.hasText(orderNo)) {
                log.error("订单号为空");
                return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[订单号为空]]></return_msg></xml>";
            }

            // 4. 处理支付结果
            String resultCode = params.get("result_code");
            if ("SUCCESS".equals(resultCode)) {
                // 支付成功逻辑
                String transactionId = params.get("transaction_id");
                log.info("订单支付成功: {}, 微信订单号: {}", orderNo, transactionId);

                // TODO: 更新订单状态
                // orderService.paySuccess(orderNo, transactionId);

                return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            } else {
                // 支付失败
                log.error("支付失败，订单号: {}, 错误码: {}", orderNo, params.get("err_code"));
                return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[支付失败]]></return_msg></xml>";
            }

        } catch (Exception e) {
            log.error("处理微信回调异常", e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[系统异常]]></return_msg></xml>";
        }
    }


    /**
     * 处理支付宝回调
     */
    @Override
    public String handleAlipayCallback(Map<String, String> params) {
        try {
            log.info("处理支付宝回调参数: {}", JSON.toJSONString(params));

            // 1. 验证签名
            if (!verifyAlipaySign(params)) {
                log.error("支付宝签名验证失败");
                return "failure";
            }

            // 2. 获取订单号
            String orderNo = params.get("out_trade_no");
            if (!StringUtils.hasText(orderNo)) {
                log.error("订单号为空");
                return "failure";
            }

            // 3. 验证交易状态
            String tradeStatus = params.get("trade_status");
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                // 支付成功
                String tradeNo = params.get("trade_no");
                log.info("订单支付成功: {}, 支付宝订单号: {}", orderNo, tradeNo);

                // TODO: 更新订单状态
                // orderService.paySuccess(orderNo, tradeNo);

                return "success";
            } else {
                log.error("支付失败，订单号: {}, 状态: {}", orderNo, tradeStatus);
                return "failure";
            }

        } catch (Exception e) {
            log.error("处理支付宝回调异常", e);
            return "failure";
        }
    }

    /**
     * 验证微信签名
     */
    private boolean verifyWechatSign(Map<String, String> params) {
        try {
            if (!StringUtils.hasText(wxMchKey)) {
                log.error("微信商户密钥未配置");
                return false;
            }

            String receivedSign = params.get("sign");
            if (!StringUtils.hasText(receivedSign)) {
                log.error("微信回调中无签名");
                return false;
            }

            // 计算签名
            String generatedSign = calculateWechatSign(params, wxMchKey);

            boolean ok = receivedSign.equals(generatedSign);
            if (!ok) {
                log.error("签名不匹配，收到: {}, 计算: {}", receivedSign, generatedSign);
            }

            return ok;

        } catch (Exception e) {
            log.error("验证微信签名异常", e);
            return false;
        }
    }

    /**
     * 计算微信签名
     */
    private String calculateWechatSign(Map<String, String> params, String mchKey) {
        // 1. 按key排序
        Map<String, String> sorted = new TreeMap<>(params);

        // 2. 拼接字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // 跳过sign字段和空值
            if (!"sign".equals(key) && StringUtils.hasText(value)) {
                if (sb.length() > 0) sb.append("&");
                sb.append(key).append("=").append(value);
            }
        }

        // 3. 加上key
        sb.append("&key=").append(mchKey);

        // 4. 计算MD5
        String signStr = sb.toString();
        log.debug("微信签名字符串: {}", signStr);

        String md5 = DigestUtils.md5DigestAsHex(signStr.getBytes(StandardCharsets.UTF_8));
        return md5.toUpperCase();
    }

    /**
     * 验证支付宝签名
     */
    private boolean verifyAlipaySign(Map<String, String> params) {
        try {
            if (!StringUtils.hasText(aliPublicKey)) {
                log.warn("支付宝公钥未配置，跳过验证");
                return true;  // 开发环境跳过
            }

            // 这里需要支付宝SDK
            // boolean valid = AlipaySignature.rsaCheckV1(params, aliPublicKey, "UTF-8", "RSA2");
            // return valid;

            // 暂时返回true，生产环境一定要实现
            log.warn("支付宝签名验证暂未实现");
            return true;

        } catch (Exception e) {
            log.error("验证支付宝签名异常", e);
            return false;
        }
    }

    /**
     * 解析微信XML
     */
    private Map<String, String> parseWechatXml(String xml) {
        Map<String, String> map = new HashMap<>();

        if (!StringUtils.hasText(xml)) {
            return map;
        }

        try {
            // 简单的XML解析
            xml = xml.replaceAll("<!\\[CDATA\\[|\\]\\]>", "");  // 移除CDATA

            // 正则匹配标签
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<([^>]+)>([^<]+)</\\1>");
            java.util.regex.Matcher matcher = pattern.matcher(xml);

            while (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2);
                map.put(key, value);
            }

        } catch (Exception e) {
            log.error("解析XML异常", e);
        }

        return map;
    }



    /**
     * 统一退款接口
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Result<RefundResultVO> unifiedRefund(RefundRequestDTO request) {
//        try {
//            // 1. 参数校验
////            validateRefundRequest(request);
//
//            // 2. 创建退款记录
//            RefundApplyDTO applyDTO = buildRefundApplyDTO(request);
//            Result<String> createResult = refundService.applyRefund(applyDTO);
//            if (!createResult.isSuccess()) {
//                return Result.error(createResult.getCode(), createResult.getMessage());
//            }
//
//            String refundNo = createResult.getData();
//
//            // 3. 执行退款
//            RefundStrategy strategy = refundStrategyFactory.getStrategy(request.getChannel());
//            RefundResultVO result = strategy.refund(refundNo, request);
//
//            return Result.success(result);
//
//        } catch (BusinessException e) {
//            log.error("退款业务异常", e);
//            throw e;
//        } catch (Exception e) {
//            log.error("退款系统异常", e);
//            throw new BusinessException("退款系统异常");
//        }
//    }
}
