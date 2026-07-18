package com.aioveu.ums.aioveu01Member.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class PublicSecurityConfig {

    @Bean
    @Order(1) // ✅ 优先级最高
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // ✅ 只拦截这一个接口
                .securityMatcher(
                        "/aioveu/api/v8/app/ums/members/openIdAndTenantId/**"
                )

                // ✅ 授权：全部放行
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // ✅ 关键：彻底关闭 JWT 校验
                .oauth2ResourceServer(oauth2 -> oauth2.disable())

                // ✅ 不需要 CSRF
                .csrf(csrf -> csrf.disable())

                // ✅ 无状态
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}
