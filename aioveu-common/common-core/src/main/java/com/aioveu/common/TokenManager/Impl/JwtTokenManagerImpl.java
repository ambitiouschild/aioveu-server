package com.aioveu.common.TokenManager.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.aioveu.common.TokenManager.TokenManagerService;
import com.aioveu.common.constant.RedisConstants;
import com.aioveu.common.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;
// ✅ 使用 Spring Framework 自带的
import org.springframework.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: JwtTokenManagerImpl
 * @Description TODO  JWT Token 管理器
 *                      用于生成、解析、校验、刷新 JWT Token
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/12 17:55
 * @Version 1.0
 **/

//✅ 方案2：使用 @RequiredArgsConstructor（Lombok）
//这是一个 Java 编译警告，意思是字段 redisTemplate可能没有被初始化。
// 通常出现在使用 @Autowired或 @Resource注入的字段上，编译器认为这个字段可能在构造函数中使用时还未被 Spring 注入。
/*
* 可能的原因：
1.字段上用了 @Autowired但没有在构造函数中初始化
2.构造函数中引用了这个字段，但注入时机在构造函数之后
3.编译器认为这个字段可能为 null
* */
@Service
@Slf4j
@RequiredArgsConstructor
public class JwtTokenManagerImpl implements TokenManagerService {



    private final RedisTemplate<String, Object> redisTemplate;



    /**
     * 将令牌加入黑名单
     *
     * @param token JWT Token
     */
    @Override
    public void invalidateToken(String token) {
        if (!StringUtils.hasText(token)) {  // 没有内容时返回
            return;
        }

        if (token.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) {
            token = token.substring(SecurityConstants.BEARER_TOKEN_PREFIX.length());
        }
        log.info("【JWT Token管理器】令牌吊销功能 ✅");


        try {
            JWT jwt = JWTUtil.parseToken(token);
            JSONObject payloads = jwt.getPayloads();
            String jti = payloads.getStr(JWTPayload.JWT_ID);
            Integer expirationAt = payloads.getInt(JWTPayload.EXPIRES_AT);

            revokeTokenByJti(jti, expirationAt);
            log.info("【JWT Token管理器】将令牌加入黑名单, jti={}", jti);
        } catch (Exception e) {
            log.error("【JWT Token管理器】吊销令牌失败", e);
        }
    }

    /*
    * // 2. 检查令牌是否吊销 ✅ / 你的 isTokenRevoked 是 private 方法，外部无法调用！
    * // ✅ 需要改成 public
    * */
    @Override
    public  boolean isTokenRevoked(String token) {
        if (!StringUtils.hasText(token)) {
            return true;
        }

        // 移除 Bearer 前缀
        if (token.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) {
            token = token.substring(SecurityConstants.BEARER_TOKEN_PREFIX.length());
        }

        try {
            JWT jwt = JWTUtil.parseToken(token);
            JSONObject payloads = jwt.getPayloads();
            String jti = payloads.getStr(JWTPayload.JWT_ID);

            return isTokenRevokedByJti(jti);
        } catch (Exception e) {
            log.error("检查令牌吊销状态失败", e);
            return true;  // 解析失败视为无效
        }

    }

    /**
     * 通过jti检查令牌是否被吊销
     */
    public boolean isTokenRevokedByJti(String jti) {
        if (!StringUtils.hasText(jti)) {
            log.warn("jti为空，令牌无效");
            return false;  // 空jti视为未吊销（但实际应该无效）
        }

        log.info("通过jti检查令牌是否被吊销");

        String revokedKey = StrUtil.format(RedisConstants.Auth.REVOKED_JTI, jti);
        boolean isRevoked = Boolean.TRUE.equals(redisTemplate.hasKey(revokedKey));
        log.debug("检查令牌吊销状态: jti={}, 是否吊销={}", jti, isRevoked);

        return isRevoked;
    }



    /*
    * 将令牌加入黑名单 内部方法：通过jti吊销令牌（内部方法）
    * */
    private void revokeTokenByJti(String jti, Integer expirationAt) {
        // ✅ 正确：检查是否没有内容
        if (!StringUtils.hasText(jti)) {
            log.warn("【JWT Token管理器】jti为空，无法加入黑名单");
            return;
        }

        String revokedJtiKey = StrUtil.format(RedisConstants.Auth.REVOKED_JTI, jti);

        if (expirationAt != null) {
            int currentTimeSeconds = Convert.toInt(System.currentTimeMillis() / 1000);
            if (expirationAt < currentTimeSeconds) {
                log.info("【JWT Token管理器】令牌已过期，无需加入黑名单: jti={}", jti);
                return;  // 已过期，不需要加入黑名单
            }
            int expirationIn = expirationAt - currentTimeSeconds;
            redisTemplate.opsForValue().set(revokedJtiKey, Boolean.TRUE, expirationIn, TimeUnit.SECONDS);

            log.info("【JWT Token管理器】令牌加入黑名单，设置TTL: jti={}, TTL={}秒", jti, expirationIn);

        } else {
            redisTemplate.opsForValue().set(revokedJtiKey, Boolean.TRUE);

            log.info("【JWT Token管理器】令牌加入黑名单，无TTL: jti={}", jti);
        }
    }

    /**
     * 失效指定用户的所有会话
     * <p>
     * 通过递增用户 tokenVersion，使该用户之前签发的所有 Token 因版本号不匹配而失效。
     * <p>
     * 适用场景：
     * <ul>
     *   <li>用户修改密码</li>
     *   <li>管理员强制下线用户</li>
     *   <li>用户主动踢出所有设备</li>
     *   <li>用户被禁用</li>
     * </ul>
     */
    @Override
    public Long invalidateUserSessions(Long userId) {
        if (userId == null) {
            return 0L;
        }

        String versionKey = StrUtil.format(RedisConstants.Auth.USER_TOKEN_VERSION, userId);

        // 递增版本号
        Long newVersion = redisTemplate.opsForValue().increment(versionKey);


        // 递增版本号，无需设置 TTL（版本号永久有效，避免 TTL 过期导致的安全问题）
        if (newVersion != null) {
            log.info("用户所有会话已失效: userId={}, 新版本={}", userId, newVersion);
            return newVersion;
        } else {
            log.error("递增版本号失败: userId={}", userId);
            return 0L;
        }

    }




}
