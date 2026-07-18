package com.aioveu.common.security.config;


import com.aioveu.common.security.interceptor.ClientContextFeignInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: FeignInterceptorConfig
 * @Description TODO 注册Feign拦截器（✅ 必须）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/13 0:47
 * @Version 1.0
 **/

/*
* 注册Feign拦截器（✅ 必须）
* 最终生效还需要一步（你很可能漏了）
*   Feign 拦截器 = 基础设施配置
    基础设施 = common 模块
    不是业务模块
* */
//@Configuration
public class FeignInterceptorConfig {

    @Bean
    public RequestInterceptor clientContextFeignInterceptor() {
        return new ClientContextFeignInterceptor();
    }
}
