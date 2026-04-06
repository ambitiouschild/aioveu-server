package com.aioveu.auth.config;

import com.aioveu.auth.model.MemberDetails;
import com.aioveu.common.constant.JwtClaimConstants;
import com.aioveu.auth.model.SysUserDetails;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Collections;
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


    public JwtTokenCustomizerConfig() {
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
            /*
            * 但在您的WechatAuthenticationProvider中，
            * context.getPrincipal()确实是UsernamePasswordAuthenticationToken类型，应该能通过。
            * */
            // ✅ 1. 打印所有关键信息
            log.info("【JwtTokenCustomizer】令牌类型: {}", context.getTokenType().getValue());
            log.info("【JwtTokenCustomizer】令牌类型Class: {}", context.getTokenType().getClass().getName());
            log.info("【JwtTokenCustomizer】是否是 ACCESS_TOKEN: {}", OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()));
            log.info("【JwtTokenCustomizer】是否是 REFRESH_TOKEN: {}", OAuth2TokenType.REFRESH_TOKEN.equals(context.getTokenType()));


            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) &&
                    context.getPrincipal() instanceof UsernamePasswordAuthenticationToken) {

                log.info("【JwtTokenCustomizer】✅ 处理的是ACCESS_TOKEN");
                // Customize headers/claims for access_token
                Optional.ofNullable(context.getPrincipal().getPrincipal()).ifPresent(principal -> {
                    JwtClaimsSet.Builder claims = context.getClaims();


                    if (principal instanceof SysUserDetails userDetails) { // 系统用户添加自定义字段
                        log.info("【JwtTokenCustomizer】✅ 系统用户SysUserDetails添加自定义字段");
                        // 调试日志
                        log.info("【JwtTokenCustomizer】用户详情字段值: userId={}, username={}, deptId={}, dataScope={}, dataScopes={}, tenantId={}, canSwitchTenant={}",
                                userDetails.getUserId(),
                                userDetails.getUsername(),
                                userDetails.getDeptId(),
                                userDetails.getDataScope(),
                                userDetails.getDataScopes(),
                                userDetails.getTenantId(),
                                userDetails.getCanSwitchTenant());

                        log.info("建议使用第一个方案（简单直接的if判断），因为它最清晰易懂，也最容易调试。");
                        // 用户ID - 必须有值
                        if (userDetails.getUserId() != null) {
                            claims.claim(JwtClaimConstants.USER_ID, userDetails.getUserId());
                        } else {
                            log.info("用户ID为空，跳过添加到JWT");
                        }

                        // 用户名 - 必须有值
                        if (StringUtils.hasText(userDetails.getUsername())) {
                            claims.claim(JwtClaimConstants.USERNAME, userDetails.getUsername());
                        } else {
                            log.info("用户名为空，跳过添加到JWT");
                        }

                        // 部门ID - 可以为空
                        if (userDetails.getDeptId() != null) {
                            claims.claim(JwtClaimConstants.DEPT_ID, userDetails.getDeptId());
                        }

                        // 数据权限 - 可以为空，但建议设置默认值
                        if (userDetails.getDataScope() != null) {
                            claims.claim(JwtClaimConstants.DATA_SCOPE, userDetails.getDataScope());
                        } else {
                            // 如果业务允许，可以设置默认值
//                            claims.claim(JwtClaimConstants.DATA_SCOPE, 0);
                            log.info("dataScope为空，跳过添加到JWT,或者使用默认值: 0");
                        }

                        // 数据权限列表 - 可以为空
                        if (userDetails.getDataScopes() != null && !userDetails.getDataScopes().isEmpty()) {
                            claims.claim(JwtClaimConstants.DATA_SCOPES, userDetails.getDataScopes());
                        }else {
                            // 如果业务允许，可以设置默认值
//                            claims.claim(JwtClaimConstants.DATA_SCOPE, 0);
                            log.info("数据权限列表为空，跳过添加到JWT");
                        }


                        // 租户ID - 必须要有值，但可以是默认值
                        if (userDetails.getTenantId() != null) {
                            claims.claim(JwtClaimConstants.TENANT_ID, userDetails.getTenantId());
                        } else {
                            // 设置默认租户ID
//                            claims.claim(JwtClaimConstants.TENANT_ID, 0L);
                            log.info("tenantId为空，跳过添加到JWT,或者使用默认值: 0");
                        }

                        // 是否可以切换租户 - 可以为空
                        if (userDetails.getCanSwitchTenant() != null) {
                            claims.claim(JwtClaimConstants.CAN_SWITCH_TENANT, userDetails.getCanSwitchTenant());
                        } else {
                            // 默认不能切换租户
//                            claims.claim(JwtClaimConstants.CAN_SWITCH_TENANT, false);
                            log.info("canSwitchTenant为空，跳过添加到JWT,或者使用默认值: false");
                        }


                        // 这里存入角色至JWT，解析JWT的角色用于鉴权的位置: ResourceServerConfig#jwtAuthenticationConverter
                        var authorities = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities())
                                .stream()
                                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
                        claims.claim(JwtClaimConstants.AUTHORITIES, authorities);

                    } else if (principal instanceof MemberDetails userDetails) {
                        log.info("【JwtTokenCustomizer】✅ 会员用户MemberDetails添加自定义字段");
                        log.info("【JwtTokenCustomizer】✅ 找到MemberDetails，开始添加租户ID");
                        Long tenantId = userDetails.getTenantId();
                        log.info("【JwtTokenCustomizer】 租户ID值: {}", tenantId);

                        if (tenantId != null) {
                            claims.claim(JwtClaimConstants.TENANT_ID, String.valueOf(tenantId));
                            log.info("【JwtTokenCustomizer】✅ 已添加tenant_id到JWT Claims: {}", tenantId);
                        }

                        // 商城会员添加自定义字段
                        //但您的微信登录使用的是MemberDetails，应该走这个分支
                        claims.claim(JwtClaimConstants.MEMBER_ID, String.valueOf(userDetails.getId()));
                    }
                });
            }
        };
    }

}
