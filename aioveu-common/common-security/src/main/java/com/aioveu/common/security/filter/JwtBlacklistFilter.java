package com.aioveu.common.security.filter;

import com.aioveu.common.TokenManager.service.TokenManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            log.debug("检查令牌吊销状态: token前{}位...", Math.min(token.length(), 20));

            // 检查黑名单
            if (tokenManagerService.isTokenRevoked(token)) {

                log.warn("令牌已被吊销: token前{}位...", Math.min(token.length(), 20));

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                Map<String, Object> error = Map.of(
                        "error", "invalid_token",
                        "error_description", "The access token was revoked",
                        "error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1"
                );


                try {
                    new ObjectMapper().writeValue(response.getWriter(), error);
                } catch (IOException e) {
                    log.error("写入错误响应失败", e);
                }

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
