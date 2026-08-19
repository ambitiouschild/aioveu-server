package com.aioveu.auth.config;

import com.aioveu.auth.model.MemberDetails;
import com.aioveu.common.core.constant.JwtClaimConstants;
import com.aioveu.common.security.core.model.SysUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @Description: TODO JWT 自定义字段配置
 * @Author: 雒世松
 * @Date: 2025/6/5 17:42
 * @param
 * @return:
 **/

@Slf4j
@Configuration
public class JwtEncodingCustomizerConfiguration {

    private final RedisTemplate<String, Object> redisTemplate;

    public JwtEncodingCustomizerConfiguration(RedisTemplate<String, Object> redisTemplate) {
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

            // ✅ 从 Authorization 中取你存进去的 Principal
            Authentication principal = context.getPrincipal();
            log.info("【JwtTokenCustomizer】JWT Customizer 应该从这个 principal 里拿用户principal:{}",principal);
            //👉👉 JWT Customizer 应该从这个 principal 里拿用户
            //✅ 是 → 继续往下走，把 SysUserDetails拿出来写进 JWT
            //“如果不是，instanceof = false，取反为真，进入 if，return 空”
            //所以这里的 return意思是：
            //“我不改 JWT claims，直接结束本次定制逻辑”
            if (!(principal instanceof UsernamePasswordAuthenticationToken authToken)) {
                log.warn("【JWT】Authorization 中不存在 UsernamePasswordAuthenticationToken");
                return;
            }

            log.info("【JwtTokenCustomizer】开始处理令牌定制 +++++++++++++");

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
                addTokenVersion(claims, memberDetails); // ✅
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
        addTokenVersion(claims, userDetails);

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
        // ✅ 补这一行
        addClaim(claims, JwtClaimConstants.Member.AUTHORITIES,
                Set.of("ROLE_USER"));

//        addClaim(claims, JwtClaimConstants.User.AUTHORITIES,
//                AuthorityUtils.authorityListToSet(
//                        memberDetails.getAuthorities()));
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
            SysUserDetails userDetails
    ) {
        Long tokenVersion = userDetails.getTokenVersion();
        if (tokenVersion != null) {
            addClaim(claims, JwtClaimConstants.Token.VERSION, tokenVersion);
        }
    }

    private void addTokenVersion(
            JwtClaimsSet.Builder claims,
            MemberDetails memberDetails
    ) {
        Long tokenVersion = memberDetails.getTokenVersion();
        if (tokenVersion != null) {
            addClaim(claims, JwtClaimConstants.Token.VERSION, tokenVersion);
        }
    }

}
