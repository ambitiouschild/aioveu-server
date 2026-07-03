package com.aioveu.common.web.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Enumeration;

/**
 * @Description: TODO Feign相关配置类
 *                      FeignConfig是“纯 Web MVC + Servlet”写法
 *                      ✅ 这个写法“可以给 API 用”
                         * ❌ 但前提是：这个 API 只在「Spring MVC 服务」里用
                         * ❌ 绝对不能让 Gateway / WebFlux 依赖到它
 * @Author: 雒世松
 * @Date: 2025/6/5 16:22
 * @param
 * @return:
 **/


//凡是“碰 HttpServletRequest / DispatcherServlet / RequestContextHolder的 FeignConfig，只能活在 Web MVC 世界
//它就是为 API 服务生的，不是给 Gateway 用的
@Configuration
public class FeignConfig {
    /**
     * 让DispatcherServlet向子线程传递RequestContext
     *
     * @param servlet servlet
     * @return 注册bean
     */
    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(DispatcherServlet servlet) {
        servlet.setThreadContextInheritable(true);
        return new ServletRegistrationBean<>(servlet, "/**");
    }

    /**
     * 覆写拦截器，在feign发送请求前取出原来的header并转发
     *
     * @return 拦截器
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return (template) -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
                HttpServletRequest request = attributes.getRequest();
                // 获取请求头
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        // 忽略content-length。因为在复制请求头到新请求时，原始的content-length可能不再准确。
                        if (!"content-length".equalsIgnoreCase(name)) {
                            String values = request.getHeader(name);
                            // 将请求头保存到模板中，除了 Content-Length
                            template.header(name, values);
                        }
                    }
                }
            }
        };
    }

}

