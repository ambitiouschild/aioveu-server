package com.aioveu.common.security.filter;


import com.aioveu.common.constant.JwtClaimConstants;
import com.aioveu.common.security.config.property.SecurityProperties;
import com.aioveu.common.security.util.ClaimUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
 * @ClassName: JwtVersionFilter
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/3 0:02
 * @Version 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtVersionFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            filterChain.doFilter(request, response);
            return;
        }

        Jwt jwt = jwtAuth.getToken();

        Long userId = ClaimUtils.getClaimAsLong(jwt, JwtClaimConstants.User.ID);
        Long tokenVersion = ClaimUtils.getClaimAsLong(jwt, JwtClaimConstants.Token.VERSION);

        if (userId == null || tokenVersion == null) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_token", "Token 非法", null)
            );
        }

        String versionKey = String.format("auth:user:token:version:%d", userId);
        Long currentVersion = (Long) redisTemplate.opsForValue().get(versionKey);

        if (currentVersion == null || !currentVersion.equals(tokenVersion)) {
            log.warn("用户已被强制下线: userId={}", userId);
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_token", "用户已被强制下线", null)
            );
        }

        filterChain.doFilter(request, response);
    }


    /**
     * ✅ 可选：放行公开接口（如果你不想在公共接口上查黑名单）
     * 第一步：白名单接口 必须跳过你的三个 Filter
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher matcher = new AntPathMatcher();

        return securityProperties.getWhitelistPaths().stream()
                .anyMatch(path -> matcher.match(path, request.getRequestURI()));
    }

}
