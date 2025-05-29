package com.aioveu.auth.authentication.sms;

import com.aioveu.auth.authentication.SecurityConstants;
import com.aioveu.auth.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

/**
 * 短信登录验证逻辑
 * 
 * 由于短信验证码的验证在过滤器里已完成，这里直接读取用户信息即可。
 * 
 * @author zhailiang
 *
 */
@Slf4j
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

	private final SysUserService sysUserService;

	private final StringRedisTemplate stringRedisTemplate;

	public SmsCodeAuthenticationProvider(SysUserService sysUserService, StringRedisTemplate stringRedisTemplate) {
		this.sysUserService = sysUserService;
		this.stringRedisTemplate = stringRedisTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;

		String phone = (String) authenticationToken.getPrincipal();
		String loginCode = authenticationToken.getCredentials() + "";
		if (StringUtils.isEmpty(loginCode)) {
			log.error("登录:{}验证码为空", phone);
			throw new InternalAuthenticationServiceException("验证码不能为空!");
		}
		String code = stringRedisTemplate.opsForValue().get(SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_SMS + phone);
		if (StringUtils.isEmpty(code)) {
			log.error("登录:{}验证码已失效", phone);
			throw new InternalAuthenticationServiceException("验证码已失效, 请重新获取!");
		}
		if (!loginCode.equals(code)) {
			log.error("登录:{}验证码:{}和服务器验证码:{}不匹配", phone, loginCode, code);
			throw new InternalAuthenticationServiceException("验证码不正确!");
		}

		UserDetails user = sysUserService.getByPhone(phone);

		if (user == null) {
			// 用户不存在，自动注册用户
			user = sysUserService.quickRegisterByPhone(phone);
		}
		if (user == null) {
			throw new InternalAuthenticationServiceException("用户注册失败");
		}
		SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, loginCode, user.getAuthorities());
		authenticationResult.setDetails(authenticationToken.getDetails());

		stringRedisTemplate.opsForValue().getOperations().delete(SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_SMS + phone);
		return authenticationResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
