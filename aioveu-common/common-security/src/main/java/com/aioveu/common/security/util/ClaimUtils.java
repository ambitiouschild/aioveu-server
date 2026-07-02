package com.aioveu.common.security.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * @ClassName: GetClaimAsLong
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/3 2:11
 * @Version 1.0
 **/
@Slf4j
public class ClaimUtils {

    public static Long getClaimAsLong(Jwt jwt, String claimName) {
        Object value = jwt.getClaim(claimName);
        if (value instanceof Integer i) {
            return i.longValue();
        }
        if (value instanceof Long l) {
            return l;
        }
        return null;
    }
}
