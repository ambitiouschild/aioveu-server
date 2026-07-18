package com.aioveu.common.security.config;


import com.aioveu.common.security.interceptor.TenantInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @ClassName: WebConfig
 * @Description TODO WebConfig 注册 TenantInterceptor
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/18 15:25
 * @Version 1.0
 **/

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TenantInterceptor());
    }
}
