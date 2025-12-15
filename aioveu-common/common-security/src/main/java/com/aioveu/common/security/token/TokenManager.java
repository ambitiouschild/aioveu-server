package com.aioveu.common.security.token;

import com.aioveu.common.security.model.AuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @ClassName: TokenManager
 * @Description TODO  Token 管理器  用于生成、解析、校验、刷新 Token
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 14:34
 * @Version 1.0
 **/

@Service
// 或 @Component
// 或 @Repository
public interface TokenManager {

    /**
     * 生成认证 Token
     *
     * @param authentication 用户认证信息
     * @return 认证 Token 响应
     */
    AuthenticationToken generateToken(Authentication authentication);

    /**
     * 解析 Token 获取认证信息
     *
     * @param token  Token
     * @return 用户认证信息
     */
    Authentication parseToken(String token);

    /**
     * 校验 Token 是否有效
     *
     * @param token JWT Token
     * @return 是否有效
     */
    boolean validateToken(String token);

    /**
     * 校验 刷新 Token 是否有效
     *
     * @param refreshToken JWT Token
     * @return 是否有效
     */
    boolean validateRefreshToken(String refreshToken);

    /**
     *  刷新 Token
     *
     * @param token 刷新令牌
     * @return 认证 Token 响应
     */
    AuthenticationToken refreshToken(String token);

    /**
     * 令 Token 失效
     *
     * @param token JWT Token
     */
    default void invalidateToken(String token) {
        // 默认实现可以是空的，或者抛出不支持的操作异常
        // throw new UnsupportedOperationException("Not implemented");
    }
}
