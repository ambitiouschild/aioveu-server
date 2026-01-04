package com.aioveu.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTPayload;
import com.nimbusds.jose.JWSObject;
import com.aioveu.common.constant.RedisConstants;
import com.aioveu.common.result.ResultCode;
import com.aioveu.gateway.util.WebFluxUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;

/**
 * @Description: TODO Token 验证全局过滤器   API网关令牌黑名单验证过滤器
 *                      我们有一个全局过滤器，用于验证JWT令牌是否在黑名单中。
 *                      如果令牌在黑名单中，则返回错误响应；否则继续执行过滤器链。
 *                      注意：这个过滤器只检查黑名单，不验证JWT的签名和过期时间，
 *                      因为这些通常由后续的认证过滤器（如Spring Security的JWT过滤器）处理。
 *                          * 核心功能：检查JWT访问令牌是否在黑名单中，防止已注销的令牌继续使用
 *                          *
 *                          * 工作原理：
 *                          * 1. 拦截所有经过API网关的请求
 *                          * 2. 检查请求头中是否包含Bearer Token
 *                          * 3. 解析JWT令牌获取唯一标识(jti)
 *                          * 4. 查询Redis黑名单，检查令牌是否已被注销
 *                          * 5. 黑名单中的令牌拒绝访问，有效令牌放行
 *                          *
 *                          * 安全机制：防止令牌泄露后即使未过期也被禁止使用
 *                          * 典型场景：用户主动注销、令牌泄露、权限变更立即生效
 *                          // 场景1: 用户注销后原令牌立即失效
 *                           // 用户点击注销 -> 服务端将jti加入黑名单 -> 网关拦截该令牌 -> 防止继续访问
 *                           // 场景2: 管理员禁用用户
 *                           // 管理员禁用用户 -> 将该用户的所有令牌jti加入黑名单 -> 立即生效
 *                           // 场景3: 令牌泄露应急处理
 *                           // 检测到异常访问 -> 将可疑令牌加入黑名单 -> 阻止攻击者继续使用
 *
 * @Author: 雒世松
 * @Date: 2025/6/5 16:36
 * @param
 * @return:
 **/

@Component  // 标记为Spring组件，由Spring容器管理
@RequiredArgsConstructor   // Lombok注解，自动生成包含final字段的构造函数
@Slf4j  // SLF4J日志注解
public class TokenValidationGlobalFilter implements GlobalFilter, Ordered {


    // Redis操作模板，用于查询令牌黑名单
    private final RedisTemplate<String, Object> redisTemplate;

    // JWT令牌的标准前缀
    private static final String BEARER_PREFIX = "Bearer ";


    /**
     * TODO 网关过滤器的核心方法 - 对经过网关的所有请求进行令牌黑名单检查
     *          执行流程：
     *          1. 检查请求头是否包含Bearer Token → 2. 解析JWT获取jti → 3. 查询Redis黑名单 → 4. 决定放行或拦截
     *
     * @param exchange 服务器Web交换对象，包含HTTP请求和响应信息
     * @param chain 网关过滤器链，用于继续执行后续过滤器
     * @return Mono<Void> Reactor响应式编程的 Mono对象，表示异步处理结果
     *
     * TODO 注意：此过滤器只检查黑名单，不验证JWT签名和过期时间，这些由下游服务验证
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取HTTP请求和响应对象
        log.info("=====GateWay验证全局过滤器=====");
        log.info("获取HTTP请求和响应对象");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();


        // 步骤1: 从请求头中获取Authorization认证头
        log.info("步骤1: 从请求头中获取Authorization认证头");
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 调试：打印Authorization头
        log.debug("调试：打印Authorization头，Authorization Header: {}", authorization);
        if (authorization != null) {
            log.debug("Authorization Header length: {}", authorization.length());
        }


        // 检查认证头是否存在且格式正确（以Bearer开头）
        if (StrUtil.isBlank(authorization) || !StrUtil.startWithIgnoreCase(authorization, BEARER_PREFIX)) {
            // 如果没有Bearer Token，直接放行（可能是公开接口或其他认证方式）
            log.info("没有Bearer Token或格式不正确，直接放行");
            return chain.filter(exchange);
        }

        try {

            // 步骤2: 提取并解析JWT令牌
            // 去除"Bearer "前缀，获取纯JWT令牌字符串
            log.info("步骤2: 提取并解析JWT令牌");
            log.info("去除\"Bearer \"前缀，获取纯JWT令牌字符串");
            String token = authorization.substring(BEARER_PREFIX.length());

            // 添加详细的令牌信息日志
            log.info("原始令牌: {}", token);
            log.info("令牌长度: {}", token.length());
            log.info("令牌是否包含点: {}", token.contains("."));
            if (token.contains(".")) {
                int dotCount = token.length() - token.replace(".", "").length();
                log.info("令牌中点分隔符数量: {}", dotCount);
                String[] parts = token.split("\\.");
                log.info("令牌分割后部分数量: {}", parts.length);
                for (int i = 0; i < parts.length; i++) {
                    log.info("令牌第{}部分长度: {}", i+1, parts[i].length());
                }
            }

            // 先验证令牌格式
            if (!isValidJwtFormat(token)) {
                log.error("JWT令牌格式无效");
                return WebFluxUtils.writeErrorResponse(response, ResultCode.TOKEN_INVALID);
            }


            // 使用nimbus-jose-jwt库解析JWT令牌（不验证签名，只解析结构）
            log.info("使用nimbus-jose-jwt库解析JWT令牌（不验证签名，只解析结构）");
            JWSObject jwsObject = JWSObject.parse(token);

            // 从JWT负载(payload)中获取令牌唯一标识(jti - JWT ID)
            log.info("从JWT负载(payload)中获取令牌唯一标识(jti - JWT ID)");
            String jti = (String) jwsObject.getPayload().toJSONObject().get(JWTPayload.JWT_ID);
            log.info("获取到的令牌唯一标识jti: {}", jti);


            // 步骤3: 检查令牌是否在黑名单中
            // 构建Redis键：token:blacklist:{jti}
            log.info("步骤3: 检查令牌是否在黑名单中");
            log.info("Redis查询key: {}", RedisConstants.TOKEN_BLACKLIST_PREFIX + jti);
            Boolean isBlackToken = redisTemplate.hasKey(RedisConstants.TOKEN_BLACKLIST_PREFIX + jti);

            // 如果令牌在黑名单中，返回访问禁止错误
            if (Boolean.TRUE.equals(isBlackToken)) {
                log.warn("检测到黑名单令牌访问，jti: {}, 路径: {}", jti, request.getPath());
                return WebFluxUtils.writeErrorResponse(response, ResultCode.TOKEN_ACCESS_FORBIDDEN);
            }


        } catch (ParseException e) {
            // 步骤5: JWT令牌解析异常处理
            log.info("步骤5: JWT令牌解析异常处理");
            log.error("TokenValidationGlobalFilter中解析令牌失败", e);
            log.error("Parsing token failed in TokenValidationGlobalFilter", e);
            return WebFluxUtils.writeErrorResponse(response, ResultCode.TOKEN_INVALID);
        }catch (Exception e) {
            // 捕获其他异常
            log.error("TokenValidationGlobalFilter中发生未知异常", e);
            return WebFluxUtils.writeErrorResponse(response, ResultCode.SYSTEM_EXECUTION_ERROR);
        }

        // 步骤4: 令牌不在黑名单中，继续执行过滤器链
        log.info("步骤4: 令牌不在黑名单中，继续执行过滤器链");
        return chain.filter(exchange);
    }

    // 添加JWT格式验证方法
    private boolean isValidJwtFormat(String token) {
        if (StrUtil.isBlank(token)) {
            log.error("令牌为空");
            return false;
        }

        // 检查是否包含两个点分隔符
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            log.error("JWT令牌格式错误: 应有3部分，实际有{}部分", parts.length);
            return false;
        }

        // 检查各部分是否为空
        for (int i = 0; i < parts.length; i++) {
            if (StrUtil.isBlank(parts[i])) {
                log.error("JWT令牌第{}部分为空", i + 1);
                return false;
            }
        }

        return true;
    }


    /**
     *  TODO 获取过滤器执行顺序
     *
     * @return int 过滤器优先级，数值越小优先级越高
     *
     * 顺序设计考虑：
     * - 优先级较高(-100)：在身份认证之前执行，避免无效令牌继续后续处理
     * - 在网关路由过滤器之前执行，确保安全验证先于业务处理
     * - 但不在最前，给日志记录、限流等更基础的过滤器留出空间
     */
    @Override
    public int getOrder() {


        return -100;  // 较高的优先级，确保在黑名单检查之后再处理业务逻辑
    }


}
