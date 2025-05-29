package com.aioveu.auth.authentication.openid;

import com.aioveu.auth.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @description OpenId认证配置类
 * @author: 雒世松
 * @date: 2020/5/31 0031 21:13
 */
@Component
public class OpenIdAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Lazy
	@Autowired
	private AuthenticationSuccessHandler sportAuthenticationSuccessHandler;

	@Lazy
	@Autowired
	private AuthenticationFailureHandler sportAuthenticationFailureHandler;

	@Override
	public void configure(HttpSecurity http) {
		OpenIdAuthenticationFilter openIdAuthenticationFilter = new OpenIdAuthenticationFilter();
		openIdAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		openIdAuthenticationFilter.setAuthenticationSuccessHandler(sportAuthenticationSuccessHandler);
		openIdAuthenticationFilter.setAuthenticationFailureHandler(sportAuthenticationFailureHandler);

		OpenIdAuthenticationProvider openIdAuthenticationProvider = new OpenIdAuthenticationProvider(sysUserService, stringRedisTemplate);

		http.authenticationProvider(openIdAuthenticationProvider)
			.addFilterAfter(openIdAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
	}

}
