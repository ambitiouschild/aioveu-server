package com.aioveu.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;
/**
 * @ClassName: RateLimiterConfig
 * @Description TODO 限流解析器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/16 18:08
 * @Version 1.0
 **/

@Configuration
public class RateLimiterConfig {

//    这是 Spring Cloud Gateway 的限流配置问题。有两个 KeyResolverbean，但 Gateway 只需要一个。
    @Bean
    @Primary  // 添加这个注解
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> {
            String remoteAddr = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            return Mono.just(remoteAddr);
        };
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
            if (userId == null) {
                userId = "anonymous";
            }
            return Mono.just(userId);
        };
    }
}
