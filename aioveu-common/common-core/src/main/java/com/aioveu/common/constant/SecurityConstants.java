package com.aioveu.common.constant;

/**
 * @ClassName: SecurityConstants
 * @Description TODO  安全模块常量
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 15:10
 * @Version 1.0
 **/
public interface SecurityConstants {

// ========== HTTP 认证头常量 ==========
    /**
     * 登录路径
     */
    String LOGIN_PATH = "/api/v1/auth/login";


    /**
     * 认证请求头名称
     */
    String AUTH_HEADER = "Authorization";

    /**
     * JWT Token 前缀
     */
    String BEARER_TOKEN_PREFIX  = "Bearer ";

    /**
     * 角色前缀，用于区分 authorities 角色和权限， ROLE_* 角色 、没有前缀的是权限
     */
    String ROLE_PREFIX = "ROLE_";

}
