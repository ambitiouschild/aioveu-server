package com.aioveu.ums.aioveu01Member.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


/**
 * ✅ 公共会员接口安全链
 *
 * 用途：
 * - 微信端 openId + tenantId 查询
 * - 禁止 JWT 解析（避免签名失败 / JWKS 调用）
 *
 * 注意：
 * - 必须 @Order(1)，优先于 ResourceServerConfiguration
 * - 禁止改为 permitAll() 而保留 oauth2ResourceServer()
 */
@Configuration
public class UmsPublicApiSecurityConfiguration {


    //优化 2：路径用常量（防止魔法字符串）
    public static final String PUBLIC_MEMBER_PATH =
            "/aioveu/api/v8/app/ums/members/openIdAndTenantId/**";


    @Bean
    @Order(1) // ✅ 优先级最高  👉 这是“真·公共接口”的教科书写法
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // ✅ 只拦截这一个接口
                .securityMatcher(
                        PUBLIC_MEMBER_PATH
                )

                // ✅ 授权：全部放行  ✅ 真正放行（不是“放行但校验”）
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // ✅ 关键：彻底关闭 JWT 校验
                .oauth2ResourceServer(oauth2 -> oauth2.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                // 明确允许匿名 ❌ anonymous().enable()在 Spring Security 6 已经不存在了  ✅ 匿名访问现在是“默认开启”的

                // ✅ 不需要 CSRF
                .csrf(csrf -> csrf.disable())

                // ✅ 无状态
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}
