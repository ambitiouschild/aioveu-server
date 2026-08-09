package com.aioveu.common.security.resource.filter;

import com.aioveu.common.core.TokenManager.service.TokenManagerService;
import com.aioveu.common.security.core.config.property.SecurityFilterOrders;
import com.aioveu.common.security.resource.config.property.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @ClassName: JwtAuthSkippingFilter
 * @Description TODO  JwtAuthSkippingFilter
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/8/9 11:21
 * @Version 1.0
 **/

@Slf4j
@Component  // ✅ 让 Spring 自动管理  步骤2：确保 JwtAuthSkippingFilter @Component
@RequiredArgsConstructor  // 使用 Lombok 自动生成构造函数
public class JwtAuthSkippingFilter extends OncePerRequestFilter implements Ordered {



    @Override
    public int getOrder() {
        //（静态引用）
        return SecurityFilterOrders.JWT_AUTH_SKIPPING;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        filterChain.doFilter(request, response);
    }

    /**
     * ✅ 可选：放行公开接口（如果你不想在公共接口上查黑名单）
     * 第一步：白名单接口 必须跳过你的三个 Filter
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String uri = request.getRequestURI();
        return uri.startsWith("/aioveu/api/v8/admin/tenant/users/")
                && (
                uri.contains("/UserAuthCredentials")
                        || uri.endsWith("/tenants/" + extractUsername(uri))
        );
    }


}
