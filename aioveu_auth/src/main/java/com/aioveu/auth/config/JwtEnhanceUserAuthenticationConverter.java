package com.aioveu.auth.config;


import com.aioveu.auth.common.model.SecurityUser;
import com.aioveu.auth.common.model.TokenConstant;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import org.springframework.security.oauth2.jwt.Jwt;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 雒世松
 * 从map中提提取用户信息
 */

//Spring Security 6.x + Spring Boot 3.x  //继承体系调整
public class JwtEnhanceUserAuthenticationConverter extends JwtAuthenticationConverter {


    private final JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    /**
     * 重写抽取用户数据方法
     */
//    @Override
//    public Authentication extractAuthentication(Map<String, ?> map) {
//        if (map.containsKey(USERNAME)) {
//            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
//            String username = MapUtil.getStr(map, USERNAME,"");
//            String userId = MapUtil.getStr(map, TokenConstant.USER_ID,"");
//            Integer gender= MapUtil.getInt(map, TokenConstant.GENDER);
//            String nickName = MapUtil.getStr(map, TokenConstant.NICK_NAME,"");
//            String avatar = MapUtil.getStr(map, TokenConstant.AVATAR,"");
//            String mobile = MapUtil.getStr(map, TokenConstant.MOBILE,"");
//            String email = MapUtil.getStr(map, TokenConstant.EMAIL,"");
//            SecurityUser user = SecurityUser.builder()
//                    .username(username)
//                    .id(userId)
//                    .gender(gender)
//                    .name(nickName)
//                    .head(avatar)
//                    .phone(mobile)
//                    .email(email)
//                    .authorities(authorities)
//                    .build();
//            return new UsernamePasswordAuthenticationToken(user, "", authorities);
//        }
//        return null;
//    }

    public JwtEnhanceAuthenticationConverter() {
        // 配置权限声明解析（根据JWT实际声明名称调整）
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName("authorities");
    }

    /**
     * 提取权限
     */
//    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
//        Object authorities = map.get(AUTHORITIES);
//        if (authorities instanceof String) {
//            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
//        }
//        if (authorities instanceof Collection) {
//            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
//                    .collectionToCommaDelimitedString((Collection<?>) authorities));
//        }
//        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
//    }


    @Override
    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        // 合并默认权限与自定义权限
        Collection<GrantedAuthority> baseAuthorities = authoritiesConverter.convert(jwt);
        Collection<GrantedAuthority> customAuthorities = extractCustomAuthorities(jwt);
        return Stream.concat(baseAuthorities.stream(), customAuthorities.stream())
                .collect(Collectors.toList());
    }

    private Collection<? extends GrantedAuthority> extractCustomAuthorities(Jwt jwt) {
        // 添加自定义权限解析逻辑
        return Collections.emptyList();


    }

    //参数类型变更，用户信息封装
    @Override
    protected AbstractAuthenticationToken createAuthenticationToken(Jwt jwt,
                                                                    Collection<? extends GrantedAuthority> authorities) {
        SecurityUser user = buildSecurityUser(jwt);
        return new JwtAuthenticationToken(jwt, authorities, user.getUsername(), user);
    }

    private SecurityUser buildSecurityUser(Jwt jwt) {
        return SecurityUser.builder()
                .username(jwt.getSubject())
                .id(jwt.getClaim(TokenConstant.USER_ID))
                .gender(jwt.getClaim(TokenConstant.GENDER))
                .name(jwt.getClaim(TokenConstant.NICK_NAME))
                .head(jwt.getClaim(TokenConstant.AVATAR))
                .phone(jwt.getClaim(TokenConstant.MOBILE))
                .email(jwt.getClaim(TokenConstant.EMAIL))
                .authorities(extractAuthorities(jwt))  // 使用实际权限集合
                .build();
    }
}