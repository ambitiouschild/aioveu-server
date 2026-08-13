package com.aioveu.common.security.resource.filter;


import com.aioveu.common.core.constant.JwtClaimConstants;
import com.aioveu.common.redis.utils.RedisKeyUtils;
import com.aioveu.common.security.core.config.property.SecurityFilterOrders;
import com.aioveu.common.security.resource.config.property.ResourceSecurityProperties;
import com.aioveu.common.security.resource.utils.ClaimUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
@RequiredArgsConstructor
public class JwtVersionFilter extends OncePerRequestFilter{

    private final ResourceSecurityProperties resourceSecurityProperties;
    private final StringRedisTemplate stringRedisTemplate;

    static {
        System.err.println("✅ JwtVersionFilter loaded by: " + JwtVersionFilter.class.getName());
        System.err.println("✅ Is proxy: " + JwtVersionFilter.class.getName().contains("$$"));
    }

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

        // ✅ 已校验过的直接放行  ✅ 同一 JWT 只校验一次
        if (Boolean.TRUE.equals(
                jwtAuth.getTokenAttributes().get("__version_checked__"))) {
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

        String versionKey = RedisKeyUtils.userTokenVersion(userId);
        String value = stringRedisTemplate.opsForValue().get(versionKey);

        log.error("【RESOURCE-REDIS】{}",
                stringRedisTemplate.getConnectionFactory().getConnection().toString());

        if (value == null) {
            log.warn("Token version 不存在，用户可能被踢下线，userId={}", userId);
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_token", "用户已被强制下线", null)
            );
        }


        /*
        * ✅ 生产系统 必须容忍 Redis 短暂不可用
          ✅ 否则一次 Redis 抖动 = 全站 JWT 用户集体 500
        * */
        Long currentVersion;
        try {
            currentVersion = Long.valueOf(value);
        } catch (Exception e) {
            log.error("Redis 读取 token version 失败，userId={}", userId, e);
            // ✅ 降级：放行（或按你业务策略拒绝）
            filterChain.doFilter(request, response);
            return;
        }

        if (currentVersion == null || !currentVersion.equals(tokenVersion)) {
            log.warn("Token version 失效，拒绝访问，userId={}, tokenVersion={}, currentVersion={}",
                    userId, tokenVersion, currentVersion);
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_token", "用户已被强制下线", null)
            );
        }

        // ✅ 标记已校验  ✅ 校验通过后，标记已检查
        jwtAuth.getTokenAttributes().put("__version_checked__", true);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/actuator") ||
                uri.startsWith("/internal");
    }


}
