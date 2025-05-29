package com.aioveu.auth.authentication.openid;

import com.aioveu.auth.authentication.SecurityConstants;
import com.aioveu.auth.service.SysUserService;
import com.aioveu.auth.utils.MobileNumberValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

/**
 * @description OpenId 认证验证器 用来对验证用户信息
 * @author: 雒世松
 * @date: 2020/5/31 0031 21:13
 */
@Slf4j
public class OpenIdAuthenticationProvider implements AuthenticationProvider {

	private final SysUserService sysUserService;

	private final StringRedisTemplate stringRedisTemplate;

	public OpenIdAuthenticationProvider(SysUserService sysUserService, StringRedisTemplate stringRedisTemplate) {
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
		OpenIdAuthenticationToken authenticationToken = (OpenIdAuthenticationToken) authentication;
		String wxLoginCode = authenticationToken.getProviderId();
		String openId = authenticationToken.getPrincipal().toString();
		String code = stringRedisTemplate.opsForValue().get(SecurityConstants.OPEN_ID_LOGIN_CODE + openId);
		if (StringUtils.isEmpty(code)) {
			log.error("登录:{}微信登录码已失效", openId);
			throw new InternalAuthenticationServiceException("登录超时, 请重新操作");
		}
		if (!wxLoginCode.equals(code)) {
			log.error("登录:{}微信登录码已失效", openId);
			throw new InternalAuthenticationServiceException("登录超时, 请重新操作");
		}

		String mobile = authenticationToken.getMobile();
		UserDetails user = sysUserService.getByOpenId(authenticationToken.getPrincipal() + "");
		if (user == null) {
			log.error(authenticationToken.getPrincipal() + "未注册");
			if (!StringUtils.isEmpty(mobile)) {
				if (!MobileNumberValidator.isValidMobileNumber(mobile)) {
					log.error("手机号码非法:{}", mobile);
					throw new InternalAuthenticationServiceException("手机号码:" + mobile + "错误");
				}
				user = sysUserService.getByPhone(mobile);
				if (user == null) {
					user = sysUserService.quickRegisterByPhone(mobile);
				}
			}
		}
		if (user == null) {
			throw new InternalAuthenticationServiceException("您还未注册, 请先完成注册");
		}
		
		OpenIdAuthenticationToken authenticationResult = new OpenIdAuthenticationToken(user, user.getAuthorities());
		
		authenticationResult.setDetails(authenticationToken.getDetails());

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
		return OpenIdAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
