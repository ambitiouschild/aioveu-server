package com.aioveu.common.redis.utils;

import com.aioveu.common.core.constant.RedisConstants;

/**
 * @Description: TODO RedisKeyUtils 工具类
 * @Author: 雒世松
 * @Date: 2026/8/13 21:49
 * @param
 * @return:
 **/

public class RedisKeyUtils {

    public static String userTokenVersion(Long userId) {
        return RedisConstants.Auth.USER_TOKEN_VERSION + userId;
    }

    public static String accessToken(String token) {
        return RedisConstants.Auth.ACCESS_TOKEN_USER + token;
    }

    public static String blacklistToken(String jti) {
        return RedisConstants.Auth.BLACKLIST_TOKEN + jti;
    }
}
