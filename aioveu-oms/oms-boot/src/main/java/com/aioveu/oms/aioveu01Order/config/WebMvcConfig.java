package com.aioveu.oms.aioveu01Order.config;


import com.aioveu.common.security.interceptor.ClientContextInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: WebMvcConfig
 * @Description TODO 在订单微服务里注册（重点）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 22:46
 * @Version 1.0
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new ClientContextInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/register",
                        "/public/**"
                );
    }

    /*
    * ✅ 这里决定“哪些接口用”

      ✅ 这里决定“哪些接口不用”
    * */
}
