package com.aioveu.auth.config;

import cn.hutool.core.map.MapUtil;
import com.aioveu.auth.common.model.SecurityUser;
import com.aioveu.auth.common.model.TokenConstant;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author 雒世松
 * 从map中提提取用户信息
 */
public class JwtEnhanceUserAuthenticationConverter extends JwtAuthenticationConverter {


    private final JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public JwtEnhanceAuthenticationConverter() {
        // 配置权限声明名称（默认为 "scope" 或 "scp"）
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName("authorities");
    }

    /**
     * 重写抽取用户数据方法
     */
//    @Override
    public Authentication extractAuthentication(Jwt jwt) {
        // 解析权限
        Collection<GrantedAuthority> authorities = authoritiesConverter.convert(jwt);

        // 构建自定义用户对象
        SecurityUser user = SecurityUser.builder()
                .username(jwt.getSubject())
                .id(jwt.getClaim(TokenConstant.USER_ID))
                .gender(jwt.getClaim(TokenConstant.GENDER))
                .name(jwt.getClaim(TokenConstant.NICK_NAME))
                .head(jwt.getClaim(TokenConstant.AVATAR))
                .phone(jwt.getClaim(TokenConstant.MOBILE))
                .email(jwt.getClaim(TokenConstant.EMAIL))
                .authorities(authorities)
                .build();

        return new JwtAuthenticationToken(jwt, authorities, user.getName());
    }

    /**
     * 提取权限
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}