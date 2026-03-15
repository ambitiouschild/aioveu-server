package com.aioveu.common.TokenManager.service;


import com.aioveu.common.model.AuthenticationToken;

/**
 * @ClassName: TokenManagerService
 * @Description TODO  Token 管理器
 *                  *  用于生成、解析、校验、刷新 Token
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/12 17:50
 * @Version 1.0
 **/

public interface TokenManagerService {

    /**
     * 生成认证 Token
     *
     * @param authentication 用户认证信息
     * @return 认证 Token 响应
     */
//    AuthenticationToken generateToken(Authentication authentication);

    /**
     * 解析 Token 获取认证信息
     *
     * @param token  Token
     * @return 用户认证信息
     */
//    Authentication parseToken(String token);

    /**
     * 校验 Token 是否有效
     *
     * @param token JWT Token
     * @return 是否有效
     */
//    boolean validateToken(String token);

    /**
     * 校验 刷新 Token 是否有效
     *
     * @param refreshToken JWT Token
     * @return 是否有效
     */
//    boolean validateRefreshToken(String refreshToken);

    /**
     *  刷新 Token
     *
     * @param token 刷新令牌
     * @return 认证 Token 响应
     */
//    AuthenticationToken refreshToken(String token);

    /**
     * 令 Token 失效  默认实现可以是空的，或者抛出不支持的操作异常
     *
     * @param token Token
     */
     void invalidateToken(String token);


    /*
     * 检查令牌是否吊销 ✅
     * ✅ 需要改成 public
     * */
    boolean isTokenRevoked(String token);

    /**
     * 使指定用户的所有会话失效 默认空实现，由具体 TokenManager 决定是否支持按用户下线
     *
     * @param userId 用户ID
     */
    Long invalidateUserSessions(Long userId);


}
