package com.aioveu.common.security.filter;

import com.aioveu.common.TokenManager.TokenManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName: JwtBlacklistFilter
 * @Description TODO  黑名单检查过滤器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/12 21:09
 * @Version 1.0
 **/

@Component
public class JwtBlacklistFilter extends OncePerRequestFilter {

    private final TokenManagerService tokenManagerService;

    public JwtBlacklistFilter(TokenManagerService tokenManagerService) {
        this.tokenManagerService = tokenManagerService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // 检查黑名单
            if (tokenManagerService.isTokenRevoked(token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                Map<String, Object> error = Map.of(
                        "error", "invalid_token",
                        "error_description", "The access token was revoked",
                        "error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1"
                );

                new ObjectMapper().writeValue(response.getWriter(), error);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
