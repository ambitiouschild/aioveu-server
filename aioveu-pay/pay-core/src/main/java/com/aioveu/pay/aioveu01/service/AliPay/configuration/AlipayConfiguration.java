//package com.aioveu.pay.aioveuModule.service.AliPay.configuration;
//
//import com.alipay.api.*;
//import io.micrometer.common.util.StringUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @ClassName: AlipayConfiguration
// * @Description TODO  支付宝客户端配置
// * @Author 可我不敌可爱
// * @Author 雒世松
// * @Date 2026/2/10 17:58
// * @Version 1.0
// **/
//
//@Configuration
//@Slf4j
//public class AlipayConfiguration {
//
//    @Autowired
//    private AlipayConfig alipayConfig;
//
//    /**
//     * 创建默认的AlipayClient
//     */
//    @Bean
//    public AlipayClient alipayClient() {
//        try {
//            CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
//            certAlipayRequest.setServerUrl(alipayConfig.getServerUrl());
//            certAlipayRequest.setAppId(alipayConfig.getAppId());
////            certAlipayRequest.setPrivateKey(alipayConfig.getAppPrivateKey());
//            certAlipayRequest.setFormat(alipayConfig.getFormat());
//            certAlipayRequest.setCharset(alipayConfig.getCharset());
//            certAlipayRequest.setSignType(alipayConfig.getSignType());
//
//            // 设置代理
//            if (StringUtils.isNotBlank(alipayConfig.getProxyHost())) {
//                certAlipayRequest.setProxyHost(alipayConfig.getProxyHost());
//                certAlipayRequest.setProxyPort(alipayConfig.getProxyPort());
//            }
//
//            // 证书模式
//            if (alipayConfig.isUseCert()) {
//                certAlipayRequest.setCertPath(alipayConfig.getAppCertPath());
//                certAlipayRequest.setAlipayPublicCertPath(alipayConfig.getAlipayCertPath());
//                certAlipayRequest.setRootCertPath(alipayConfig.getAlipayRootCertPath());
////                certAlipayRequest.setEncryptKey(alipayConfig.getEncryptKey());  //
//                certAlipayRequest.setEncryptType(alipayConfig.getEncryptType());
//
//                return new DefaultAlipayClient(certAlipayRequest);
//            }
//            // 公钥模式
//            else {
//                return new DefaultAlipayClient(
//                        alipayConfig.getServerUrl(),
//                        alipayConfig.getAppId(),
//                        alipayConfig.getAppPrivateKey(),
//                        alipayConfig.getFormat(),
//                        alipayConfig.getCharset(),
//                        alipayConfig.getAlipayPublicKey(),
//                        alipayConfig.getSignType()
//                );
//            }
//
//        } catch (AlipayApiException e) {
//            log.error("支付宝客户端初始化失败", e);
//            throw new RuntimeException("支付宝客户端初始化失败", e);
//        }
//    }
//
////    /**
////     * 创建AlipayTradeService（如果使用Easysdk）
////     */
////    @Bean
////    public com.alipay.easysdk.factory.Factory alipayEasySdkFactory() {
////        try {
////            com.alipay.easysdk.kernel.Config config = new com.alipay.easysdk.kernel.Config();
////            config.protocol = "https";
////            config.gatewayHost = alipayConfig.getServerUrl()
////                    .replace("https://", "")
////                    .replace("/gateway.do", "");
////            config.signType = alipayConfig.getSignType();
////            config.appId = alipayConfig.getAppId();
////
////            if (alipayConfig.isUseCert()) {
////                // 证书模式
////                config.merchantCertPath = alipayConfig.getAppCertPath();
////                config.alipayCertPath = alipayConfig.getAlipayCertPath();
////                config.alipayRootCertPath = alipayConfig.getAlipayRootCertPath();
////                config.certEnvironment = "PRODUCTION";
////            } else {
////                // 公钥模式
////                config.alipayPublicKey = alipayConfig.getAlipayPublicKey();
////                config.merchantPrivateKey = alipayConfig.getAppPrivateKey();
////            }
////
////            config.notifyUrl = alipayConfig.getNotifyUrl();
////
////            Factory.setOptions(config);
////            return Factory.getInstance();
////
////        } catch (Exception e) {
////            log.error("支付宝EasySDK初始化失败", e);
////            throw new RuntimeException("支付宝EasySDK初始化失败", e);
////        }
////    }
//}
