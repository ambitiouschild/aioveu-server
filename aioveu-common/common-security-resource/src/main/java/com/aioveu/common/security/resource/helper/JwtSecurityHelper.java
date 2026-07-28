package com.aioveu.common.security.resource.helper;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.core.constant.JwtClaimConstants;
import com.aioveu.common.core.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Map;



/**
 * @ClassName: JwtSecurityHelper
 * @Description TODO  JwtSecurityHelper（resource 版）
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/28 15:28
 * @Version 1.0
 **/

@Slf4j
public class JwtSecurityHelper {


    private JwtSecurityHelper() {}


    /**
     * 获取认证对象的详细信息
     *
     * @返回值:
     *   - Authentication Spring Security认证对象
     *   - 包含主体、凭证、权限等完整信息
     *
     * @高级用途:
     *   - 自定义权限验证逻辑
     *   - 获取认证提供者信息
     *   - 访问原始认证细节
     */
    public static Map<String, Object> getTokenAttributes() {
        Authentication authentication = JwtSecurityUtils.getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getClaims();
        }
        return Collections.emptyMap();
    }


    /**
     * 获取JWT令牌的唯一标识符（JTI）
     *
     * @原理说明:
     *   - JTI是JWT标准声明字段，确保每个令牌唯一性
     *   - 用于令牌黑名单、会话管理、防止重放攻击
     *
     * @返回值:
     *   - String 令牌唯一标识，未认证返回null
     *
     * @安全应用:
     *   - 令牌注销: 将JTI加入黑名单
     *   - 防止重放: 验证JTI是否已使用过
     *   - 会话追踪: 关联用户登录会话
     */
    public static String getJti() {
        return (String) getTokenAttributes().get(JwtClaimConstants.Token.JTI);
    }


    /**
     * 从当前HTTP请求中获取JWT Token
     *
     * @原理说明:
     *   - 从HttpServletRequest的Authorization头中提取Token
     *   - 支持Bearer Token格式: "Bearer {token}"
     *   - 自动去除Bearer前缀，返回纯Token字符串
     *
     * @返回值:
     *   - String JWT Token字符串，未找到返回null
     *
     * @获取位置:
     *   - Authorization请求头
     *   - 格式: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     *
     * @使用场景:
     *   - 需要原始Token进行验证、注销等操作
     *   - 记录操作日志时保存Token信息
     *   - 自定义Token处理逻辑
     *
     * @注意: 需要在Web请求上下文中调用（Controller、Interceptor等）
     */
    public static String getToken() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        String auth = attrs.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isNotBlank(auth) && auth.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) {
            return auth.substring(SecurityConstants.BEARER_TOKEN_PREFIX.length()).trim();
        }
        return null;
    }


    /**
     * 获取当前请求的客户端ID (ClientId)
     * <p>
     * 该值通常在 Authorization Server 的 JwtTokenCustomizer 中被注入到 JWT 中。
     *
     * @return ClientId，如果不存在则返回 null
     */
    public static String getClientId() {
        return (String) getTokenAttributes().get(JwtClaimConstants.Client.ID);
    }


    /**
     * 获取JWT令牌的过期时间（Expiration Time）
     *
     * @原理说明:
     *   - exp是JWT标准声明，表示令牌失效的时间戳
     *   - 时间格式为Unix时间戳（秒级）
     *
     * @返回值:
     *   - Long 过期时间戳（秒），未认证返回null
     *
     * @使用场景:
     *   - 令牌有效期检查
     *   - 自动刷新令牌逻辑
     *   - 会话超时提示
     */
    public static Long getExp() {
        return Convert.toLong(getTokenAttributes().get("exp"));
    }

}
