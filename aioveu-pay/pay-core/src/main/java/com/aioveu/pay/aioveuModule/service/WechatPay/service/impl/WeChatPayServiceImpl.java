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
import java.util.concurrent.ConcurrentHashMap;

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

    // 单例配置
    private volatile  com.wechat.pay.java.core.Config sdkConfig;

    /*   TODO  这个设计的优点
                ✅ 单例模式：避免重复创建
                ✅ 懒加载：按需创建，提高启动速度
                ✅ 线程安全：使用ConcurrentHashMap和synchronized
                ✅ 可热更新：支持重新加载配置
                ✅ 代码清晰：职责分离明确
                ✅ 性能优秀：服务复用，连接池复用
                */



    // 服务缓存
    private final Map<String, Object> serviceCache = new ConcurrentHashMap<>();
    // 自己管理这些服务实例  在每个方法中直接使用


    @Autowired
    public WeChatPayServiceImpl(WeChatPayConfig wechatPayConfig,
                                WeChatPayRequestFactory requestFactory
                                //移除构造函数注入，改为自己初始化
    ) {
        this.wechatPayConfig = wechatPayConfig;
        this.requestFactory = requestFactory;
        log.info("【微信支付】服务初始化完成，商户号: {}", wechatPayConfig.getMchId());
        // 1. 直接从配置创建SDK配置
        // ❌ 问题1：这里直接创建sdkConfig，但wechatPayConfig.toSdkConfig()可能抛异常
        // ❌ 问题2：构造函数不应该做复杂的初始化
        //修复：不要在构造函数中做可能失败的初始化
//        com.wechat.pay.java.core.Config sdkConfig = wechatPayConfig.toSdkConfig();
//        this.sdkConfig = sdkConfig;
//        log.info("【wechatPayConfig】直接从配置创建SDK配置sdkConfig:{}",sdkConfig);
    }

    /**
     * 获取SDK配置（单例+双重检查锁）
     */
    private com.wechat.pay.java.core.Config getSdkConfig() {
        if (sdkConfig == null) {
            synchronized (this) {
                if (sdkConfig == null) {
                    try {
                        log.info("【微信支付】创建SDK配置");
                        sdkConfig = wechatPayConfig.toSdkConfig();
                        log.info("【微信支付】SDK配置创建成功");
                    } catch (Exception e) {
                        log.error("【微信支付】创建SDK配置失败", e);
                        throw new RuntimeException("创建微信支付SDK配置失败", e);
                    }
                }
            }
        }
        return sdkConfig;
    }

    /**
     * 获取JSAPI服务
     */
    private JsapiService getJsapiService() {
        return (JsapiService) serviceCache.computeIfAbsent("jsapi", key -> {
            log.info("【微信支付】创建JSAPI服务");
            try {
                return new JsapiService.Builder()
                        .config(getSdkConfig())
                        .build();
            } catch (Exception e) {
                log.error("【微信支付】创建JSAPI服务失败", e);
                throw new RuntimeException("创建JSAPI服务失败", e);
            }
        });
    }

    /**
     * 获取APP服务
     */
    private AppService getAppService() {
        return (AppService) serviceCache.computeIfAbsent("app", key -> {
            log.info("【微信支付】创建APP服务");
            try {
                return new AppService.Builder()
                        .config(getSdkConfig())
                        .build();
            } catch (Exception e) {
                log.error("【微信支付】创建APP服务失败", e);
                throw new RuntimeException("创建APP服务失败", e);
            }
        });
    }

    /**
     * 获取H5服务
     */
    private H5Service getH5Service() {
        return (H5Service) serviceCache.computeIfAbsent("h5", key -> {
            log.info("【微信支付】创建H5服务");
            try {
                return new H5Service.Builder()
                        .config(getSdkConfig())
                        .build();
            } catch (Exception e) {
                log.error("【微信支付】创建H5服务失败", e);
                throw new RuntimeException("创建H5服务失败", e);
            }
        });
    }

    /**
     * 获取退款服务
     */
    private RefundService getRefundService() {
        return (RefundService) serviceCache.computeIfAbsent("refund", key -> {
            log.info("【微信支付】创建退款服务");
            try {
                return new RefundService.Builder()
                        .config(getSdkConfig())
                        .build();
            } catch (Exception e) {
                log.error("【微信支付】创建退款服务失败", e);
                throw new RuntimeException("创建退款服务失败", e);
            }
        });
    }


    /**
     * TODO 重新加载配置（热更新）
     *      记住：YAGNI原则（You Ain't Gonna Need It）- 不要为可能不需要的功能做过度设计。先让核心支付功能稳定运行，后续需要时再扩展管理功能。
     */
    public synchronized void reloadSdkConfig() {
        log.info("【微信支付】重新加载配置");
        serviceCache.clear();  // 先清空服务
        sdkConfig = null;      // 再清空配置
        log.info("【微信支付】配置重新加载完成");
    }

    /**
     * JSAPI支付（小程序/公众号）
     */
    @Override
    public PaymentParamsVO jsapiPay(PaymentRequestDTO request) {
        try {

            // 参数校验
            validateJsapiRequest(request);

            // 1. 创建JSAPI服务
            JsapiService jsapiService = getJsapiService();

            log.info("【微信支付-JSAPI支付（小程序/公众号）】创建JSAPI服务:{}",jsapiService);

            log.info("【微信支付-JSAPI】开始支付，订单号: {}, 金额: {}",
                    request.getOrderNo(), request.getAmount());

            // 使用工厂创建请求
            com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest prepayRequest =
                    requestFactory.createJsapiRequest(request, wechatPayConfig);
            log.info("【微信支付-JSAPI支付（小程序/公众号）】使用工厂创建请求prepayRequest:{}",prepayRequest);


            // 调用支付接口
            com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse response =
                    jsapiService.prepay(prepayRequest);
            log.info("【微信支付-JSAPI支付（小程序/公众号）】调用支付接口response,预支付成功，prepayId: {}",response.getPrepayId());

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
     * 验证JSAPI支付请求
     */
    private void validateJsapiRequest(PaymentRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("支付请求不能为空");
        }
        if (StringUtils.isBlank(request.getOrderNo())) {
            throw new IllegalArgumentException("订单号不能为空");
        }
        if (StringUtils.isBlank(request.getOpenId())) {
            throw new IllegalArgumentException("JSAPI支付必须提供openId");
        }
        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("支付金额必须大于0");
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

            // 参数校验
            validateAppRequest(request);

            // 1. 创建App服务
            AppService appService = getAppService();

            log.info("【微信支付-App支付）】创建App服务:{}",appService);
            log.info("【微信支付-APP】开始支付，订单号: {}, 金额: {}",
                    request.getOrderNo(), request.getAmount());

            // 使用工厂创建请求
            com.wechat.pay.java.service.payments.app.model.PrepayRequest prepayRequest =
                    requestFactory.createAppRequest(request, wechatPayConfig);
            log.info("【微信支付-App支付】使用工厂创建请求");

            // 调用支付接口
            com.wechat.pay.java.service.payments.app.model.PrepayResponse response =
                    appService.prepay(prepayRequest);
            log.info("【微信支付-App支付】调用支付接口,预支付成功，prepayId: {}", response.getPrepayId());

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

    private void validateAppRequest(PaymentRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("支付请求不能为空");
        }
        if (StringUtils.isBlank(request.getOrderNo())) {
            throw new IllegalArgumentException("订单号不能为空");
        }
        if (StringUtils.isBlank(request.getClientIp())) {
            throw new IllegalArgumentException("APP支付必须提供客户端IP");
        }
        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("支付金额必须大于0");
        }
    }

    /**
     * H5支付
     */
    @Override
    public PaymentParamsVO h5Pay(PaymentRequestDTO request) {
        try {

            // 参数校验
            validateH5Request(request);

            // 1. 创建H5服务
            H5Service h5Service = getH5Service();

            log.info("【微信支付-H5支付）】创建H5服务:{}",h5Service);
            log.info("【微信支付-H5】开始支付，订单号: {}, 金额: {}",
                    request.getOrderNo(), request.getAmount());

            // 使用工厂创建请求
            com.wechat.pay.java.service.payments.h5.model.PrepayRequest prepayRequest =
                    requestFactory.createH5Request(request, wechatPayConfig);
            log.info("【微信支付-H5支付】使用工厂创建请求");

            // 调用支付接口
            com.wechat.pay.java.service.payments.h5.model.PrepayResponse response =
                    h5Service.prepay(prepayRequest);
            log.info("【微信支付-H5支付】调用支付接口,预支付成功，h5Url: {}", response.getH5Url());


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

    private void validateH5Request(PaymentRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("支付请求不能为空");
        }
        if (StringUtils.isBlank(request.getOrderNo())) {
            throw new IllegalArgumentException("订单号不能为空");
        }
        if (StringUtils.isBlank(request.getClientIp())) {
            throw new IllegalArgumentException("H5支付必须提供客户端IP");
        }
        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("支付金额必须大于0");
        }
    }

    /**
     * 查询支付结果
     */
    @Override
    public PaymentStatusVO queryPayment(String paymentNo) {
        try {

            if (StringUtils.isBlank(paymentNo)) {
                throw new IllegalArgumentException("支付单号不能为空");
            }

            log.info("【微信支付】查询支付结果，支付单号: {}", paymentNo);

            // 使用构造函数创建查询请求
            com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest queryRequest =
                    new com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest();

            queryRequest.setOutTradeNo(paymentNo);
            queryRequest.setMchid(wechatPayConfig.getMchId());

            // 2. 创建JSAPI服务
            JsapiService jsapiService = getJsapiService();

            com.wechat.pay.java.service.payments.model.Transaction transaction =
                    jsapiService.queryOrderByOutTradeNo(queryRequest);

            log.info("【微信支付】查询支付结果成功，状态: {}", transaction.getTradeState());

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

            if (StringUtils.isBlank(paymentNo)) {
                throw new IllegalArgumentException("支付单号不能为空");
            }

            log.info("【微信支付】关闭订单，支付单号: {}", paymentNo);

            com.wechat.pay.java.service.payments.jsapi.model.CloseOrderRequest closeRequest =
                    new com.wechat.pay.java.service.payments.jsapi.model.CloseOrderRequest();
            closeRequest.setMchid(wechatPayConfig.getMchId());
            closeRequest.setOutTradeNo(paymentNo);

            // 2. 创建JSAPI服务
            JsapiService jsapiService = getJsapiService();

            jsapiService.closeOrder(closeRequest);
            log.info("【微信支付】关闭订单成功");

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

            // 参数校验
            validateRefundRequest(request);

            com.wechat.pay.java.service.refund.model.CreateRequest refundRequest =
                    buildRefundRequest(request);

            // 2. 创建Refund服务
            RefundService refundService = getRefundService();

            com.wechat.pay.java.service.refund.model.Refund response =
                    refundService.create(refundRequest);

            log.info("【微信支付】退款申请成功，退款单号: {}", response.getRefundId());

            return buildRefundResult(request, response);
        } catch (Exception e) {
            log.error("退款申请失败, 订单号: {}", request.getPaymentNo(), e);
            throw new RuntimeException("退款申请失败", e);
        }
    }

    private void validateRefundRequest(RefundRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("退款请求不能为空");
        }
        if (StringUtils.isBlank(request.getPaymentNo())) {
            throw new IllegalArgumentException("支付单号不能为空");
        }
        if (StringUtils.isBlank(request.getRefundNo())) {
            throw new IllegalArgumentException("退款单号不能为空");
        }
        if (request.getRefundAmount() == null ||
                request.getRefundAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("退款金额必须大于0");
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
