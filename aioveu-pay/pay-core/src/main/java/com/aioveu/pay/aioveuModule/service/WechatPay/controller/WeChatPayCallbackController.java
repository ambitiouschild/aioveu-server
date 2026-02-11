//package com.aioveu.pay.aioveuModule.service.WechatPay.controller;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.io.FileUtil;
//import com.aioveu.pay.aioveuModule.service.WechatPay.config.WeChatPayConfig;
//import com.aioveu.pay.aioveuModule.model.vo.PaymentCallbackDTO;
//import com.aioveu.pay.aioveuModule.service.WechatPay.service.WeChatPayService;
//import com.alibaba.fastjson.JSON;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.nio.charset.StandardCharsets;
//import java.security.Signature;
//import java.security.cert.CertificateFactory;
//import java.security.cert.X509Certificate;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @ClassName: WechatPayCallbackController
// * @Description TODO 微信支付回调处理控制器
// * @Author 可我不敌可爱
// * @Author 雒世松
// * @Date 2026/2/10 19:20
// * @Version 1.0
// **/
//
//@RestController
//@RequestMapping("/api/payment/wechat")
//@Slf4j
//public class WeChatPayCallbackController {
//
//    /**
//     * 微信支付配置
//     */
//    private final WeChatPayConfig weChatPayConfig;
//
//    /**
//     * 微信支付服务
//     */
//    private final WeChatPayService weChatPayService;
//
//    /**
//     * 成功响应码
//     */
//    private static final String SUCCESS_CODE = "SUCCESS";
//
//    /**
//     * 失败响应码
//     */
//    private static final String FAIL_CODE = "FAIL";
//
//    /**
//     * 微信支付回调头 - 序列号
//     */
//    private static final String WECHATPAY_SERIAL_HEADER = "Wechatpay-Serial";
//
//    /**
//     * 微信支付回调头 - 签名
//     */
//    private static final String WECHATPAY_SIGNATURE_HEADER = "Wechatpay-Signature";
//
//    /**
//     * 微信支付回调头 - 时间戳
//     */
//    private static final String WECHATPAY_TIMESTAMP_HEADER = "Wechatpay-Timestamp";
//
//    /**
//     * 微信支付回调头 - 随机数
//     */
//    private static final String WECHATPAY_NONCE_HEADER = "Wechatpay-Nonce";
//
//    /**
//     * 时间格式化模式
//     */
//    private static final String TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";
//
//    /**
//     * 支付结果通知回调
//     */
//    @PostMapping("/notify")
//    public String notifyCallback(HttpServletRequest request) {
//        try {
//            // 1. 获取请求体
//            String body = getRequestBody(request);
//
//            // 2. 验证签名
//            if (!verifySignature(request, body)) {
//                log.error("微信支付回调签名验证失败");
//                return buildErrorResponse("FAIL", "签名验证失败");
//            }
//
//            // 3. 解析通知数据
//            com.wechat.pay.java.service.payments.model.Transaction transaction =
//                    JSON.parseObject(body,
//                            com.wechat.pay.java.service.payments.model.Transaction.class);
//
//            // 4. 构建回调DTO
//            PaymentCallbackDTO callback = PaymentCallbackDTO.builder()
//                    .paymentNo(transaction.getOutTradeNo())
//                    .thirdPaymentNo(transaction.getTransactionId())
//                    .transactionNo(transaction.getTransactionId())
//                    .amount(new BigDecimal(transaction.getAmount().getTotal())
//                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
//                    .success("SUCCESS".equals(transaction.getTradeState().name()))
//                    .callbackParams(JSON.parseObject(body))
//                    .build();
//
//            if (transaction.getSuccessTime() != null) {
//                callback.setPaymentTime(DateUtil.parse(
//                        transaction.getSuccessTime(),
//                        "yyyy-MM-dd'T'HH:mm:ssXXX"
//                ));
//            }
//
//            // 5. 处理回调
//            weChatPayService.handleCallback(callback);
//
//            // 6. 返回成功响应
//            return buildSuccessResponse();
//
//        } catch (Exception e) {
//            log.error("处理微信支付回调异常", e);
//            return buildErrorResponse("FAIL", "系统异常");
//        }
//    }
//
//    /**
//     * 退款结果通知回调
//     */
//    @PostMapping("/refund/notify")
//    public String refundNotifyCallback(HttpServletRequest request) {
//        try {
//            String body = getRequestBody(request);
//
//            if (!verifySignature(request, body)) {
//                log.error("微信退款回调签名验证失败");
//                return buildErrorResponse("FAIL", "签名验证失败");
//            }
//
//            // 解析退款通知
//            com.wechat.pay.java.service.refund.model.RefundNotification notification =
//                    JSON.parseObject(body,
//                            com.wechat.pay.java.service.refund.model.RefundNotification.class);
//
//            // 处理退款回调逻辑
//            handleRefundCallback(notification);
//
//            return buildSuccessResponse();
//
//        } catch (Exception e) {
//            log.error("处理微信退款回调异常", e);
//            return buildErrorResponse("FAIL", "系统异常");
//        }
//    }
//
//    /**
//     * 验证签名
//     */
//    private boolean verifySignature(HttpServletRequest request, String body) {
//        try {
//            // 获取请求头
//            String serial = request.getHeader("Wechatpay-Serial");
//            String signature = request.getHeader("Wechatpay-Signature");
//            String timestamp = request.getHeader("Wechatpay-Timestamp");
//            String nonce = request.getHeader("Wechatpay-Nonce");
//
//            if (StringUtils.isAnyBlank(serial, signature, timestamp, nonce)) {
//                return false;
//            }
//
//            // 构建签名串
//            String signStr = timestamp + "\n" + nonce + "\n" + body + "\n";
//
//            // 验证签名
//            return verifySignature(signStr, signature, serial);
//
//        } catch (Exception e) {
//            log.error("验证签名异常", e);
//            return false;
//        }
//    }
//
//    /**
//     * 验证签名
//     */
//    private boolean verifySignature(String message, String signature, String serialNo)
//            throws Exception {
//
//        // 根据序列号获取平台证书
//        X509Certificate certificate = getPlatformCertificate(serialNo);
//        if (certificate == null) {
//            return false;
//        }
//
//        // 验证签名
//        Signature sign = Signature.getInstance("SHA256withRSA");
//        sign.initVerify(certificate.getPublicKey());
//        sign.update(message.getBytes(StandardCharsets.UTF_8));
//
//        return sign.verify(Base64.getDecoder().decode(signature));
//    }
//
//    /**
//     * 获取平台证书
//     */
//    private X509Certificate getPlatformCertificate(String serialNo) throws Exception {
//        // 可以从缓存或数据库中获取
//        // 这里简化处理
//        if (StringUtils.isNotBlank(wechatPayConfig.getPlatformCertPath())) {
//            String certContent = FileUtil.readString(
//                    wechatPayConfig.getPlatformCertPath(),
//                    StandardCharsets.UTF_8
//            );
//
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            InputStream is = new ByteArrayInputStream(certContent.getBytes());
//            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
//
//            if (serialNo.equals(cert.getSerialNumber().toString())) {
//                return cert;
//            }
//        }
//
//        return null;
//    }
//
//    /**
//     * 获取请求体
//     */
//    private String getRequestBody(HttpServletRequest request) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        try (BufferedReader reader = request.getReader()) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line);
//            }
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 构建成功响应
//     */
//    private String buildSuccessResponse() {
//        Map<String, String> response = new HashMap<>();
//        response.put("code", "SUCCESS");
//        response.put("message", "成功");
//        return JSON.toJSONString(response);
//    }
//
//    /**
//     * 构建错误响应
//     */
//    private String buildErrorResponse(String code, String message) {
//        Map<String, String> response = new HashMap<>();
//        response.put("code", code);
//        response.put("message", message);
//        return JSON.toJSONString(response);
//    }
//}
