package com.aioveu.common.security.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.aioveu.common.constant.JwtClaimConstants;
import com.aioveu.common.constant.RedisConstants;
import com.aioveu.common.constant.SecurityConstants;
import com.aioveu.common.security.model.SysUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName: TokenService
 * @Description TODO  创建专门的 TokenService（更推荐） 考虑到工具类应该保持简洁，建议创建专门的 Token 服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 20:41
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 将令牌加入黑名单
     */
    public void invalidateToken(String token) {
        if (token == null) {
            return;
        }

        if (token.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) {
            token = token.substring(SecurityConstants.BEARER_TOKEN_PREFIX.length());
        }

        try {
            JWT jwt = JWTUtil.parseToken(token);
            JSONObject payloads = jwt.getPayloads();

            String jti = payloads.getStr(JWTPayload.JWT_ID);
            if (jti == null) {
                log.warn("令牌没有JTI标识，无法加入黑名单");
                return;
            }

            Integer expirationAt = payloads.getInt(JWTPayload.EXPIRES_AT);
            String blacklistTokenKey = StrUtil.format(RedisConstants.Auth.BLACKLIST_TOKEN, jti);

            if (expirationAt != null) {
                int currentTimeSeconds = Convert.toInt(System.currentTimeMillis() / 1000);
                if (expirationAt < currentTimeSeconds) {
                    log.debug("令牌已过期，无需加入黑名单");
                    return;
                }
                int expirationIn = expirationAt - currentTimeSeconds;
                redisTemplate.opsForValue().set(blacklistTokenKey, "", expirationIn, TimeUnit.SECONDS);
                log.debug("令牌已加入黑名单，剩余时间：{}秒", expirationIn);
            } else {
                redisTemplate.opsForValue().set(blacklistTokenKey, "");
                log.debug("永不过期令牌已加入黑名单");
            }
        } catch (Exception e) {
            log.error("令牌加入黑名单失败：{}", e.getMessage());
        }
    }

    /**
     * 检查令牌是否在黑名单中
     */
    public boolean isTokenBlacklisted(String jti) {
        if (jti == null) {
            return false;
        }
        String blacklistTokenKey = StrUtil.format(RedisConstants.Auth.BLACKLIST_TOKEN, jti);
        return redisTemplate.hasKey(blacklistTokenKey);
    }

    /**
     * 解析令牌
     *
     * @param token JWT Token
     * @return Authentication 对象
     */
    public Authentication parseToken(String token) {

        JWT jwt = JWTUtil.parseToken(token);
        JSONObject payloads = jwt.getPayloads();
        SysUserDetails userDetails = new SysUserDetails();
        userDetails.setUserId(payloads.getLong(JwtClaimConstants.USER_ID)); // 用户ID
        userDetails.setDeptId(payloads.getLong(JwtClaimConstants.DEPT_ID)); // 部门ID
        userDetails.setDataScope(payloads.getInt(JwtClaimConstants.DATA_SCOPE)); // 数据权限范围

        userDetails.setUsername(payloads.getStr(JWTPayload.SUBJECT)); // 用户名
        // 角色集合
        Set<SimpleGrantedAuthority> authorities = payloads.getJSONArray(JwtClaimConstants.AUTHORITIES)
                .stream()
                .map(authority -> new SimpleGrantedAuthority(Convert.toStr(authority)))
                .collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
