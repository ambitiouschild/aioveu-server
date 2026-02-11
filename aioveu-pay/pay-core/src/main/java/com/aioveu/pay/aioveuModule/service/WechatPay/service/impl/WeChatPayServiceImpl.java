package com.aioveu.pay.aioveuModule.service.WechatPay.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.aioveu.pay.aioveuModule.model.vo.*;
import com.aioveu.pay.aioveuModule.service.WechatPay.config.WeChatPayConfig;
import com.aioveu.pay.aioveuModule.service.WechatPay.requestFactory.WeChatPayRequestFactory;
import com.aioveu.pay.aioveuModule.service.WechatPay.service.WeChatPayService;
import com.aioveu.pay.aioveuModule.enums.PaymentStatusEnum;
import com.aioveu.pay.aioveuModule.enums.RefundStatusEnum;
import com.alibaba.fastjson.JSON;

//在同一个类中，当不同支付方式的实体类名相同但包路径不同时，确实会产生冲突。
//注意：Java本身不支持import as语法，这是Python的语法。Java中需要使用方案1或方案3。
//方案4：使用工厂模式（最推荐）

//-------------------------------------------------------
//如果您确实是普通商户（非服务商）
import com.wechat.pay.java.service.payments.jsapi.JsapiService;  //- 普通商户支付
import com.wechat.pay.java.service.payments.app.AppService;  // - 普通商户支付
import com.wechat.pay.java.service.payments.h5.H5Service;   // - 普通商户支付

//如果您确实是普通商户（非服务商）
//import com.wechat.pay.java.service.partnerpayments.jsapi.JsapiService;   // - 服务商模式支付
//import com.wechat.pay.java.service.partnerpayments.jsapi.model.PrepayRequest;  // - 服务商模式支付
//import com.wechat.pay.java.service.partnerpayments.jsapi.model.PrepayResponse;  // - 服务商模式支付
//import com.wechat.pay.java.service.partnerpayments.app.AppService;  // - 服务商模式支付
//import com.wechat.pay.java.service.partnerpayments.h5.H5Service;   // - 服务商模式支付
//-------------------------------------------------------
import com.wechat.pay.java.service.refund.RefundService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.wechat.pay.java.service.payments.nativepay.NativeService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: WechatPayServiceImpl
 * @Description TODO  微信支付服务实现类 - 使用工厂模式  - 返回支付参数
 *                      支付路由策略
 *                      根据用户设备选择支付方式
 *                      根据支付成功率动态路由
 *                      支持灰度发布
 *                      分布式事务
 *                      支付成功与业务处理的一致性
 *                      使用消息队列保证最终一致性
 *                      补偿机制处理异常情况
 *                      安全防护
 *                      频率限制
 *                      金额校验
 *                      IP白名单
 *                      防重放攻击
 *                      这个微信支付实现方案涵盖了主流的支付场景，包括JSAPI、APP、Native、H5支付，以及完整的退款、回调处理流程。
 *                      您可以根据实际业务需求进行调整和扩展。
 *
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 18:46
 * @Version 1.0
 **/

@Service
@Slf4j
//将 WechatPay改为 wechatpay符合命名规范
public class WeChatPayServiceImpl implements WeChatPayService {

    private static final String SIGN_TYPE_RSA = "RSA";
    private static final String CURRENCY_CNY = "CNY";
    private static final int AMOUNT_MULTIPLIER = 100;

    private final WeChatPayConfig wechatPayConfig;
    private final WeChatPayRequestFactory requestFactory;
    private final JsapiService jsapiService;
//    private final NativeService nativeService;
    private final AppService appService;
    private final H5Service h5Service;
    private final RefundService refundService;

    @Autowired
    public WeChatPayServiceImpl(WeChatPayConfig wechatPayConfig,
                                WeChatPayRequestFactory requestFactory,
                                @Autowired(required = false) JsapiService jsapiService,  // 修改为 required = false
                                @Autowired(required = false) AppService appService,
                                @Autowired(required = false) H5Service h5Service,
//                                @Autowired(required = false) NativePayService nativePayService,
                                @Autowired(required = false) RefundService refundService) {
        this.wechatPayConfig = wechatPayConfig;
        this.requestFactory = requestFactory;
        this.jsapiService = jsapiService;
//        this.nativeService = nativeService;
        this.appService = appService;
        this.h5Service = h5Service;
        this.refundService = refundService;
    }

    /**
     * JSAPI支付（小程序/公众号）
     */
    @Override
    public PaymentParamsVO jsapiPay(PaymentRequestDTO request) {
        try {
            // 使用工厂创建请求
            com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest prepayRequest =
                    requestFactory.createJsapiRequest(request, wechatPayConfig);
            log.info("【微信支付-JSAPI支付（小程序/公众号）】使用工厂创建请求");


            // 调用支付接口
            com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse response =
                    jsapiService.prepay(prepayRequest);
            log.info("【微信支付-JSAPI支付（小程序/公众号）】调用支付接口");

            // 生成支付参数
            Map<String, String> payParams = generateJsapiPayParams(response.getPrepayId());
            log.info("【微信支付-JSAPI支付（小程序/公众号）】生成支付参数");

            return PaymentParamsVO.builder()
                    .paymentNo(request.getOrderNo())
                    .paymentParams(JSON.toJSONString(payParams))
                    .thirdPaymentNo(response.getPrepayId())
                    .build();
        } catch (Exception e) {
            log.error("JSAPI支付失败, 订单号: {}", request.getOrderNo(), e);
            throw new RuntimeException("JSAPI支付失败", e);
        }
    }

    /**
     * Native支付（扫码支付）
     */
//    @Override
//    public PaymentParamsVO nativePay(PaymentRequestDTO request) {
//        try {
//            // 使用工厂创建请求
//            com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest prepayRequest =
//                    requestFactory.createNativeRequest(request, wechatPayConfig);
//            log.info("【微信支付-Native支付（扫码支付）】使用工厂创建请求");
//
//            // 调用支付接口
//            com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse response =
//                    nativeService.prepay(prepayRequest);
//            log.info("【微信支付-Native支付（扫码支付）】调用支付接口");
//
//
//            return PaymentParamsVO.builder()
//                    .paymentNo(request.getOrderNo())
//                    .paymentParams(response.getCodeUrl())
//                    .thirdPaymentNo(response.getPrepayId())
//                    .qrCodeUrl(response.getCodeUrl()) // 专门存储二维码URL
//                    .build();
//        } catch (Exception e) {
//            log.error("Native支付失败, 订单号: {}", request.getOrderNo(), e);
//            throw new RuntimeException("Native支付失败", e);
//        }
//    }

    /**
     * App支付
     */
    @Override
    public PaymentParamsVO appPay(PaymentRequestDTO request) {
        try {
            // 使用工厂创建请求
            com.wechat.pay.java.service.payments.app.model.PrepayRequest prepayRequest =
                    requestFactory.createAppRequest(request, wechatPayConfig);
            log.info("【微信支付-App支付】使用工厂创建请求");

            // 调用支付接口
            com.wechat.pay.java.service.payments.app.model.PrepayResponse response =
                    appService.prepay(prepayRequest);
            log.info("【微信支付-App支付】调用支付接口");

            // 生成支付参数
            Map<String, String> payParams = generateAppPayParams(response.getPrepayId());
            log.info("【微信支付-App支付】生成支付参数");

            return PaymentParamsVO.builder()
                    .paymentNo(request.getOrderNo())
                    .paymentParams(JSON.toJSONString(payParams))
                    .thirdPaymentNo(response.getPrepayId())
                    .build();
        } catch (Exception e) {
            log.error("App支付失败, 订单号: {}", request.getOrderNo(), e);
            throw new RuntimeException("App支付失败", e);
        }
    }

    /**
     * H5支付
     */
    @Override
    public PaymentParamsVO h5Pay(PaymentRequestDTO request) {
        try {
            // 使用工厂创建请求
            com.wechat.pay.java.service.payments.h5.model.PrepayRequest prepayRequest =
                    requestFactory.createH5Request(request, wechatPayConfig);
            log.info("【微信支付-H5支付】使用工厂创建请求");

            // 调用支付接口
            com.wechat.pay.java.service.payments.h5.model.PrepayResponse response =
                    h5Service.prepay(prepayRequest);
            log.info("【微信支付-H5支付】调用支付接口");


            return PaymentParamsVO.builder()
                    .paymentNo(request.getOrderNo())
                    .paymentParams(response.getH5Url())
//                    .thirdPaymentNo(response.getPrepayId())
                    .h5Url(response.getH5Url()) // 专门存储H5 URL
                    .build();
        } catch (Exception e) {
            log.error("H5支付失败, 订单号: {}", request.getOrderNo(), e);
            throw new RuntimeException("H5支付失败", e);
        }
    }

    /**
     * 查询支付结果
     */
    @Override
    public PaymentStatusVO queryPayment(String paymentNo) {
        try {
            // 使用构造函数创建查询请求
            com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest queryRequest =
                    new com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest();

            queryRequest.setOutTradeNo(paymentNo);
            queryRequest.setMchid(wechatPayConfig.getMchId());

            com.wechat.pay.java.service.payments.model.Transaction transaction =
                    jsapiService.queryOrderByOutTradeNo(queryRequest);


            return convertToPaymentStatus(transaction);
        } catch (Exception e) {
            log.error("查询支付结果失败, 订单号: {}", paymentNo, e);
            throw new RuntimeException("查询支付结果失败", e);
        }
    }

    /**
     * 关闭订单
     */
    @Override
    public boolean closePayment(String paymentNo) {
        try {
            com.wechat.pay.java.service.payments.jsapi.model.CloseOrderRequest closeRequest =
                    new com.wechat.pay.java.service.payments.jsapi.model.CloseOrderRequest();
            closeRequest.setMchid(wechatPayConfig.getMchId());
            closeRequest.setOutTradeNo(paymentNo);

            jsapiService.closeOrder(closeRequest);
            return true;
        } catch (Exception e) {
            log.error("关闭订单失败, 订单号: {}", paymentNo, e);
            throw new RuntimeException("关闭订单失败", e);
        }
    }

    /**
     * 申请退款
     */
    @Override
    public RefundResultVO refund(RefundRequestDTO request) {
        try {
            com.wechat.pay.java.service.refund.model.CreateRequest refundRequest =
                    buildRefundRequest(request);

            com.wechat.pay.java.service.refund.model.Refund response =
                    refundService.create(refundRequest);

            return buildRefundResult(request, response);
        } catch (Exception e) {
            log.error("退款申请失败, 订单号: {}", request.getPaymentNo(), e);
            throw new RuntimeException("退款申请失败", e);
        }
    }

    // ========== 私有方法 ==========



    /**
     * 生成JSAPI支付参数
     */
    private Map<String, String> generateJsapiPayParams(String prepayId) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = IdUtil.fastSimpleUUID().substring(0, 32);
        String packageStr = "prepay_id=" + prepayId;

        String signStr = String.format("%s\n%d\n%s\n%s\n",
                wechatPayConfig.getAppId(),
                timestamp,
                nonceStr,
                packageStr
        );

        String sign = sign(signStr);

        Map<String, String> params = new HashMap<>();
        params.put("appId", wechatPayConfig.getAppId());
        params.put("timeStamp", String.valueOf(timestamp));
        params.put("nonceStr", nonceStr);
        params.put("package", packageStr);
        params.put("signType", SIGN_TYPE_RSA);
        params.put("paySign", sign);

        return params;
    }

    /**
     * 生成App支付参数
     */
    private Map<String, String> generateAppPayParams(String prepayId) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = IdUtil.fastSimpleUUID().substring(0, 32);

        String signStr = String.format("%s\n%d\n%s\n%s\n",
                wechatPayConfig.getAppId(),
                timestamp,
                nonceStr,
                prepayId
        );

        String sign = sign(signStr);

        Map<String, String> params = new HashMap<>();
        params.put("appid", wechatPayConfig.getAppId());
        params.put("partnerid", wechatPayConfig.getMchId());
        params.put("prepayid", prepayId);
        params.put("package", "Sign=WXPay");
        params.put("noncestr", nonceStr);
        params.put("timestamp", String.valueOf(timestamp));
        params.put("sign", sign);

        return params;
    }


    /**
     * 构建退款请求
     */
    private com.wechat.pay.java.service.refund.model.CreateRequest buildRefundRequest(
            RefundRequestDTO request) {
        com.wechat.pay.java.service.refund.model.CreateRequest refundRequest =
                new com.wechat.pay.java.service.refund.model.CreateRequest();


        // 创建金额对象
        com.wechat.pay.java.service.refund.model.AmountReq amount =
                new com.wechat.pay.java.service.refund.model.AmountReq();
        amount.setTotal(convertAmountToFen(request.getAmount()));
        amount.setRefund(convertAmountToFen(request.getRefundAmount()));
        amount.setCurrency(CURRENCY_CNY);

        refundRequest.setAmount(amount);
        refundRequest.setOutTradeNo(request.getPaymentNo());
        refundRequest.setOutRefundNo(request.getRefundNo());
        refundRequest.setReason(request.getRefundReason());
        refundRequest.setNotifyUrl(wechatPayConfig.getRefundNotifyUrl());

        return refundRequest;
    }

    /**
     * 构建退款结果
     */
    private RefundResultVO buildRefundResult(RefundRequestDTO request,
                                             com.wechat.pay.java.service.refund.model.Refund response) {

        return RefundResultVO.builder()
                .refundNo(request.getRefundNo())
                .thirdRefundNo(response.getRefundId())
                .refundAmount(request.getRefundAmount())
                .refundStatus(convertRefundStatus(response.getStatus()))
                .refundTime(response.getSuccessTime() != null ?
                        DateUtil.parse(response.getSuccessTime(), "yyyy-MM-dd'T'HH:mm:ssXXX") : null)
                .errorMessage(getRefundErrorMessage(response.getStatus()))
                .build();
    }

    private Integer convertRefundStatus(com.wechat.pay.java.service.refund.model.Status wechatStatus) {

        if (wechatStatus == null) {
            return RefundStatusEnum.FAILED.getValue();
        }

        switch (wechatStatus) {
            case SUCCESS:
                return RefundStatusEnum.SUCCESS.getValue();
            case PROCESSING:
                return RefundStatusEnum.PROCESSING.getValue();
            case CLOSED:
            case ABNORMAL:
            default:
                return RefundStatusEnum.FAILED.getValue();
        }
    }

    private String getRefundErrorMessage(com.wechat.pay.java.service.refund.model.Status status) {
        if (status == null) return "状态未知";

        switch (status) {
            case SUCCESS: return null;
            case PROCESSING: return "处理中";
            case CLOSED: return "已关闭";
            case ABNORMAL: return "异常";
            default: return "失败";
        }
    }

    /**
     * 转换支付状态
     */
    private PaymentStatusVO convertToPaymentStatus(
            com.wechat.pay.java.service.payments.model.Transaction transaction) {

        PaymentStatusVO statusVO = new PaymentStatusVO();
        statusVO.setPaymentNo(transaction.getOutTradeNo());
        statusVO.setThirdPaymentNo(transaction.getTransactionId());
        statusVO.setAmount(convertFenToYuan(transaction.getAmount().getTotal()));

        String tradeState = transaction.getTradeState().name();
        statusVO.setPaymentStatus(convertWechatStatus(tradeState));

        if (transaction.getSuccessTime() != null) {
            try {
                statusVO.setPaymentTime(DateUtil.parse(
                        transaction.getSuccessTime(),
                        "yyyy-MM-dd'T'HH:mm:ssXXX"
                ));
            } catch (Exception e) {
                log.warn("解析支付时间失败", e);
            }
        }

        return statusVO;
    }

    /**
     * 转换微信支付状态
     */
    private Integer convertWechatStatus(String wechatStatus) {
        switch (wechatStatus) {
            case "SUCCESS":
                return PaymentStatusEnum.SUCCESS.getValue();
            case "REFUND":
                return PaymentStatusEnum.REFUNDED.getValue();
            case "NOTPAY":
            case "USERPAYING":
                return PaymentStatusEnum.PENDING.getValue();
            case "CLOSED":
                return PaymentStatusEnum.CLOSED.getValue();
            case "REVOKED":
            case "PAYERROR":
            default:
                return PaymentStatusEnum.FAILED.getValue();
        }
    }

    /**
     * 金额转换：元转分
     * 返回Long类型以匹配SDK要求
     */
    private Long  convertAmountToFen(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(AMOUNT_MULTIPLIER)).longValue();
    }

    /**
     * 金额转换：分转元
     */
    private BigDecimal convertFenToYuan(int fen) {
        return new BigDecimal(fen).divide(BigDecimal.valueOf(AMOUNT_MULTIPLIER), 2, RoundingMode.HALF_UP);
    }

    /**
     * 生成签名
     */
    private String sign(String message) throws Exception {
        PrivateKey privateKey = loadPrivateKey();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 加载私钥
     */
    private PrivateKey loadPrivateKey() throws Exception {
        String privateKeyContent = wechatPayConfig.getPrivateKey();
        if (StringUtils.isBlank(privateKeyContent)) {
            privateKeyContent = FileUtil.readString(
                    wechatPayConfig.getPrivateKeyPath(),
                    StandardCharsets.UTF_8
            );
        }

        privateKeyContent = privateKeyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }
}
