package com.aioveu.common.security.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.aioveu.common.constant.JwtClaimConstants;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

/**
 * @Description: TODO 资源服务器配置
 * @Author: 雒世松
 * @Date: 2025/6/5 16:10
 * @param
 * @return:
 **/

@ConfigurationProperties(prefix = "security")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class ResourceServerConfig {

    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;


    /**
     * 白名单路径列表
     */
    @Setter
    private List<String> whitelistPaths;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        log.info("whitelist path:{}", JSONUtil.toJsonStr(whitelistPaths));
        http.authorizeHttpRequests((requests) ->
                        {
                            if (CollectionUtil.isNotEmpty(whitelistPaths)) {
                                for (String whitelistPath : whitelistPaths) {
                                    requests.requestMatchers(mvcMatcherBuilder.pattern(whitelistPath)).permitAll();
                                }
                            }
                            requests.anyRequest().authenticated();
                        }
                )
                .csrf(AbstractHttpConfigurer::disable)
        ;
        http.oauth2ResourceServer(resourceServerConfigurer ->
                resourceServerConfigurer
                        .jwt(jwtConfigurer -> jwtAuthenticationConverter())
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
        );
        return http.build();
    }

    /**
     * 不走过滤器链的放行配置
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                AntPathRequestMatcher.antMatcher("/webjars/**"),
                AntPathRequestMatcher.antMatcher("/doc.html"),
                AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                AntPathRequestMatcher.antMatcher("/swagger-ui/**")
        );
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    /**
     * 自定义JWT Converter
     *
     * @return Converter
     * @see JwtAuthenticationProvider#setJwtAuthenticationConverter(Converter)
     */
    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(Strings.EMPTY);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(JwtClaimConstants.AUTHORITIES);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }


}
