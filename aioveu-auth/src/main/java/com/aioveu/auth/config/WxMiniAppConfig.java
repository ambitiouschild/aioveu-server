package com.aioveu.auth.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

@ConfigurationProperties(prefix = "wx.miniapp")
@Configuration
public class WxMiniAppConfig {

    @Setter
    private String appId;

    @Setter
    private String appSecret;

    @Bean
    public WxMaConfig wxMaConfig() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(appId);
        config.setSecret(appSecret);
        return config;
    }

    @Bean
    public WxMaService wxMaService(WxMaConfig wxMaConfig) {
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(wxMaConfig);
        return service;
    }
}
