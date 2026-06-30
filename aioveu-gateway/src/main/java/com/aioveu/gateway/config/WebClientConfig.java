package com.aioveu.gateway.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @ClassName: WebClientConfig
 * @Description TODO 创建 WebClient（只做一次）  这个 Bean 全局只有一份
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/30 19:00
 * @Version 1.0
 **/
@Slf4j
@Configuration
public class WebClientConfig {

    /**
     * ✅ 官方写法
     * 所有需要负载均衡的 WebClient 都用这个 Builder
     */
    @LoadBalanced
    @Bean
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest());
    }

    /**
     * 可选：打印请求日志（方便你排错）
     */
    private ExchangeFilterFunction logRequest() {
        return (request, next) -> {
            log.debug("WebClient request: {} {}", request.method(), request.url());
            return next.exchange(request);
        };
    }
}
