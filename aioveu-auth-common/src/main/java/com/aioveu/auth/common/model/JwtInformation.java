package com.aioveu.auth.common.model;

import lombok.Data;

/**
 * @author 雒世松
 * JWT令牌相关的信息实体类
 */
@Data
public class JwtInformation {
    /**
     * JWT令牌唯一ID
     */
    private String jti;

    /**
     * 过期时间，单位秒，距离过期时间还有多少秒
     */
    private Long expireIn;
}
