package com.aioveu.pay.aioveuModule.service.WechatPay.utils.weChatPay;

import com.aioveu.pay.aioveuModule.service.WechatPay.config.WeChatPayConfig;
import com.wechat.pay.java.service.payments.app.AppService;
import com.wechat.pay.java.service.payments.h5.H5Service;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.refund.RefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: aioveuWeChatPayGetServiceUtil
 * @Description TODO  微信支付获取支付服务类工具类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/12 18:34
 * @Version 1.0
 **/

@Component
@Slf4j
public class aioveuWeChatPayGetServiceUtil {

    @Autowired
    private WeChatPayConfig wechatPayConfig;
    // 服务缓存
    private final Map<String, Object> serviceCache = new ConcurrentHashMap<>();
    // 自己管理这些服务实例  在每个方法中直接使用

    // 单例配置
    private volatile  com.wechat.pay.java.core.Config sdkConfig;

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
    public JsapiService getJsapiService() {
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
    public AppService getAppService() {
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
    public H5Service getH5Service() {
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
    public RefundService getRefundService() {
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
}
