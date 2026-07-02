package com.aioveu.common.security.filter;

import com.aioveu.common.TokenManager.service.TokenManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName: JwtBlacklistFilter
 * @Description TODO  ✅ 资源服务器：只校验 JWT 是否在黑名单
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/12 21:09
 * @Version 1.0
 **/

/*
👉 黑名单只放在资源服务器
👉 不做重复校验
👉 不写 JSON（交给 Spring Security）
👉 语义清晰、可维护、不出怪问题
下面这个是 “资源服务器里黑名单 Filter 的标准写法”。

*/

@Slf4j
@Component  // ✅ 让 Spring 自动管理  步骤2：确保 JwtBlacklistFilter是 @Component
@RequiredArgsConstructor  // 使用 Lombok 自动生成构造函数
public class JwtBlacklistFilter extends OncePerRequestFilter {

    // ❌ 错误：如果没有 @Autowired 或构造函数注入，这个字段会是 null
    // ✅ 使用 final 字段 + @RequiredArgsConstructor
    private final TokenManagerService tokenManagerService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        // 1️只处理 JWT 认证请求
        //只要你带了 Authorization Header，它就一定查黑名单
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            filterChain.doFilter(request, response);
            return;
        }

        Jwt jwt = jwtAuth.getToken();
        String token = jwt.getTokenValue();
        log.debug("检查令牌吊销状态: token前{}位...", Math.min(token.length(), 20));

        // 检查黑名单
        if (tokenManagerService.isTokenRevoked(token)) {

            log.warn("令牌已被吊销: token前{}位...", Math.min(token.length(), 20));

            // ✅ 抛标准异常，交给 Spring Security 处理
            //1️ 不写 JSON，不抢 Spring Security 的活
            // ✅ 正确构造异常
            throw new OAuth2AuthenticationException(
                    new OAuth2Error(
                            "invalid_token",
                            "The access token was revoked",
                            "https://tools.ietf.org/html/rfc6750#section-3.1"
                    )
            );
        }

        filterChain.doFilter(request, response);
    }

    /**
     * ✅ 可选：放行公开接口（如果你不想在公共接口上查黑名单）
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/public/")
                || uri.startsWith("/open/")
                || uri.startsWith("/actuator/");
    }


}
