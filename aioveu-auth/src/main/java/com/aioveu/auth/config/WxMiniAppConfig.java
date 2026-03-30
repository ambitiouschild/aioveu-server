package com.aioveu.auth.config;

//import javacn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.aioveu.auth.service.WxConfigService;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.Map;

/**
 * @Description: TODO 微信小程序配置
 *
 * 问题在于您使用的WxMaDefaultConfigImpl类在toString()方法中使用Gson序列化时，
 * 会尝试反射访问java.io.File的私有字段path，这在JDK 9+的模块系统中被禁止了。
 * @Author: 雒世松
 * @Date: 2025/6/5 17:43
 * @param
 * @return:
 **/

//@ConfigurationProperties(prefix = "wx.miniapp")
@Configuration
@Slf4j
public class WxMiniAppConfig {

//    @Setter
//    private String appId;
//
//    @Setter
//    private String appSecret;


    @Autowired
    private WxConfigService wxConfigService; // 新增服务，用于从数据库获取配置

    // 缓存配置，避免每次请求都查数据库
    private final Map<String, WxMaConfig> configCache = new ConcurrentHashMap<>();

//    @Bean  //现有的@Bean注解的wxMaConfig()方法是静态配置
//    public WxMaConfig wxMaConfig() {
//        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
//
//        config.setAppid(appId);
//        config.setSecret(appSecret);
//        return config;
//    }

    /**
     * 根据客户端ID获取配置好的wxMaService
     */

//    @Bean
//    public WxMaService wxMaService(WxMaConfig wxMaConfig) {
//        WxMaService service = new WxMaServiceImpl();
//        service.setWxMaConfig(wxMaConfig);
//        return service;
//    }

    /**
     * 创建一个默认的wxMaService Bean（主要用于依赖注入，实际使用时会被动态配置覆盖）
     * 注意：这里需要设置scope为prototype，让每次获取都是新实例
     */
    @Bean
    @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public WxMaService wxMaService() {
        WxMaService service = new WxMaServiceImpl();
        // 不在这里设置具体配置，由调用方动态设置
        return service;
    }


    /**
     * 获取微信配置（带缓存）
     */
    public WxMaConfig getWxMaConfigByClientId(String clientId) {
        return configCache.computeIfAbsent(clientId, id -> {
            TenantWxAppInfo entity = wxConfigService.getConfigByClientId(id);
            if (entity == null) {
                throw new RuntimeException("未找到客户端配置: " + id);
            }

            WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
            config.setAppid(entity.getWxAppid());
            config.setSecret(entity.getAppSecret());
            // 设置扩展数据，方便获取租户ID
//            config.getExtData().put("tenantId", entity.getTenantId());
//            config.getExtData().put("clientId", entity.getClientId());

            //当 SLF4J 调用 config.toString()或 service.toString()时，会触发 Gson 序列化，
            // 从而访问 java.io.File的私有字段导致错误。
            // 记录详细信息但不触发 toString
            log.info("【WxMiniAppConfig】为客户端ID:{} 创建微信配置，租户ID:{}, appid:{}, appSecret:{}",
                    clientId, entity.getTenantId(), entity.getWxAppid(),entity.getAppSecret());

            return config;
        });
    }

    /**
     * 根据客户端ID获取配置好的wxMaService
     */
    public WxMaService getWxMaServiceByClientId(String clientId) {
        WxMaService service = new WxMaServiceImpl();
        WxMaConfig config = getWxMaConfigByClientId(clientId);
        service.setWxMaConfig(config);

        return service;
    }

    /**
     * 清理指定客户端ID的缓存
     */
    public void clearCacheByClientId(String clientId) {
        configCache.remove(clientId);
    }

    /**
     * 清理所有缓存
     */
    public void clearAllCache() {
        configCache.clear();
    }
}
