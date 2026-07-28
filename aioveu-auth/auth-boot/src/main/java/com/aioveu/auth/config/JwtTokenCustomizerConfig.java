package com.aioveu.auth.config;

import com.aioveu.auth.model.MemberDetails;
import com.aioveu.common.core.constant.JwtClaimConstants;
import com.aioveu.common.security.core.model.SysUserDetails;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description: TODO JWT 自定义字段配置
 * @Author: 雒世松
 * @Date: 2025/6/5 17:42
 * @param
 * @return:
 **/

@Slf4j
@Configuration
public class JwtTokenCustomizerConfig {

    private final RedisTemplate<String, Object> redisTemplate;

    public JwtTokenCustomizerConfig(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        log.info("=== 【JwtTokenCustomizer】JwtTokenCustomizerConfig 被创建 ===");
    }

    /**
     * JWT 自定义字段
     * @see <a href="https://docs.spring.io/spring-authorization-server/reference/guides/how-to-custom-claims-authorities.html">Add custom claims to JWT access tokens</a>
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {

            log.info("=== 【JwtTokenCustomizer】开始处理令牌定制 JwtTokenCustomizer 被调用 ===");


            // 只处理 access_token
            if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                return;
            }

            if (!(context.getPrincipal() instanceof UsernamePasswordAuthenticationToken authToken)) {
                return;
            }

            JwtClaimsSet.Builder claims = context.getClaims();

            // ---------- 1. 公共字段 ----------
            addClaim(claims, JwtClaimConstants.Client.ID,
                    context.getRegisteredClient().getClientId());


            // ---------- 2. 系统用户 ----------
            if (authToken.getPrincipal() instanceof SysUserDetails userDetails) {
                addSysUserClaims(claims, userDetails, authToken);
                return;
            }

            // ---------- 3. 会员用户 ----------
            if (authToken.getPrincipal() instanceof MemberDetails memberDetails) {
                addMemberClaims(claims, memberDetails);
            }



        };
    }

    /* ========================= 系统用户 ========================= */

    private void addSysUserClaims(
            JwtClaimsSet.Builder claims,
            SysUserDetails userDetails,
            UsernamePasswordAuthenticationToken authToken
    ) {

        log.info("【JWT】username value = {}, class = {}",
                userDetails.getUsername(),
                userDetails.getUsername() == null ? "null" : userDetails.getUsername().getClass().getName()
        );

        addClaim(claims, JwtClaimConstants.User.ID, userDetails.getUserId());
        addClaim(claims, JwtClaimConstants.User.USERNAME, userDetails.getUsername());
        addClaim(claims, JwtClaimConstants.User.DEPT_ID, userDetails.getDeptId());
        addClaim(claims, JwtClaimConstants.User.DATA_SCOPE, userDetails.getDataScope());
        addClaim(claims, JwtClaimConstants.User.DATA_SCOPES, userDetails.getDataScopes());
        addClaim(claims, JwtClaimConstants.Tenant.ID, userDetails.getTenantId());
        addClaim(claims, JwtClaimConstants.Tenant.CAN_SWITCH, userDetails.getCanSwitchTenant());

        // token_version（从 details 读取）
        addTokenVersion(claims, authToken);

        // 权限
        addClaim(claims, JwtClaimConstants.User.PERMS, userDetails.getPerms());

        // 角色
        var authorities = AuthorityUtils
                .authorityListToSet(authToken.getAuthorities());
        addClaim(claims, JwtClaimConstants.User.AUTHORITIES, authorities);
    }

    /* ========================= 会员用户 ========================= */

    private void addMemberClaims(
            JwtClaimsSet.Builder claims,
            MemberDetails memberDetails
    ) {
        addClaim(claims, JwtClaimConstants.Tenant.ID, memberDetails.getTenantId());
        addClaim(claims, JwtClaimConstants.Member.ID, memberDetails.getId());
        addClaim(claims, JwtClaimConstants.Member.OPENID, memberDetails.getOpenId());
    }


    /* ========================= 工具方法 ========================= */

    /**
     * ✅ 安全写入 JWT Claim（杜绝 null 炸）
     */
    private void addClaim(JwtClaimsSet.Builder builder, String name, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof Collection<?> c && c.isEmpty()) {
            return;
        }
        builder.claim(name, value);
    }

    /**
     * ✅ 从 Authentication.details 读取 token_version
     */
    private void addTokenVersion(
            JwtClaimsSet.Builder claims,
            UsernamePasswordAuthenticationToken authToken
    ) {
        Object details = authToken.getDetails();
        if (!(details instanceof Map<?, ?> map)) {
            return;
        }

        Object version = map.get(JwtClaimConstants.Token.VERSION);
        if (version instanceof Number) {
            addClaim(claims, JwtClaimConstants.Token.VERSION,
                    ((Number) version).longValue());
        }
    }

}
