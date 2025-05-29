package com.aioveu.auth.config;

import com.aioveu.auth.authentication.SecurityConstants;
import com.aioveu.auth.authentication.mobile.MobilePasswordSecurityConfig;
import com.aioveu.auth.authentication.openid.OpenIdAuthenticationSecurityConfig;
import com.aioveu.auth.authentication.sms.SmsCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author 雒世松
 * spring security的安全配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final MobilePasswordSecurityConfig mobileConfig;
    private final SmsCodeSecurityConfig smsConfig;
    private final  OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    public SecurityConfig(UserDetailsService userDetailsService,
                          MobilePasswordSecurityConfig mobileConfig,
                          SmsCodeSecurityConfig smsConfig,
                          openIdAuthenticationSecurityConfig openIdConfig) {
        this.userDetailsService = userDetailsService;
        this.mobileConfig = mobileConfig;
        this.smsConfig = smsConfig;
        this.openIdAuthenticationSecurityConfig = openIdConfig;
    }

    @Bean
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        http
                .securityMatcher("/oauth2/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/oauth2/**"))
                .apply(authorizationServerConfigurer);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}