package com.aioveu.order.config;


import com.aioveu.common.security.interceptor.ClientContextFeignInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: OrderFeignConfig
 * @Description TODO FeignConfig 的正确写法（推荐）
 *  *                   Feign 配置
 *  *                   负责注册 ClientContextFeignInterceptor
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 22:59
 * @Version 1.0
 **/
@Configuration
public class OrderFeignConfig {

    /**
     * 注册 ClientId 透传拦截器
     */
    @Bean
    public RequestInterceptor clientContextFeignInterceptor() {
        return new ClientContextFeignInterceptor();
    }
}
