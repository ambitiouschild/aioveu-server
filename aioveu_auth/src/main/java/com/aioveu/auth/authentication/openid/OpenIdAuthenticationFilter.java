package com.aioveu.auth.authentication.openid;

import com.aioveu.auth.authentication.SecurityConstants;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * @description OpenId认证过滤器 用来拦截请求 并匹配请求是否符合要求
 * @author: 雒世松
 * @date: 2020/5/31 0031 21:13
 */
public class OpenIdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private String openIdParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_OPENID;

	private String providerIdParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_PROVIDERID;

	private String mobileParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;

	private boolean postOnly = true;

	public OpenIdAuthenticationFilter() {
		super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_OPENID, HttpMethod.POST.name()));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && !HttpMethod.POST.matches(request.getMethod())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String openId = obtainOpenId(request);
		String providerId = obtainProviderId(request);
		String mobile = obtainMobile(request);

		if (openId == null) {
			openId = "";
		}
		if (providerId == null) {
			providerId = "";
		}
		openId = openId.trim();
		providerId = providerId.trim();

		OpenIdAuthenticationToken authRequest = new OpenIdAuthenticationToken(openId, providerId, mobile);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}


	/**
	 * 获取openId
	 */
	protected String obtainOpenId(HttpServletRequest request) {
		return request.getParameter(openIdParameter);
	}
	
	/**
	 * 获取提供商id
	 */
	protected String obtainProviderId(HttpServletRequest request) {
		return request.getParameter(providerIdParameter);
	}

	/**
	 * 获取手机号
	 * @param request
	 * @return
	 */
	protected String obtainMobile(HttpServletRequest request) {
		return request.getParameter(mobileParameter);
	}

	/**
	 * Provided so that subclasses may configure what is put into the
	 * authentication request's details property.
	 *
	 * @param request
	 *            that an authentication request is being created for
	 * @param authRequest
	 *            the authentication request object that should have its details
	 *            set
	 */
	protected void setDetails(HttpServletRequest request, OpenIdAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	/**
	 * Sets the parameter name which will be used to obtain the username from
	 * the login request.
	 *
	 * @param openIdParameter
	 *            the parameter name. Defaults to "username".
	 */
	public void setOpenIdParameter(String openIdParameter) {
		Assert.hasText(openIdParameter, "Username parameter must not be empty or null");
		this.openIdParameter = openIdParameter;
	}


	/**
	 * Defines whether only HTTP POST requests will be allowed by this filter.
	 * If set to true, and an authentication request is received which is not a
	 * POST request, an exception will be raised immediately and authentication
	 * will not be attempted. The <tt>unsuccessfulAuthentication()</tt> method
	 * will be called as if handling a failed authentication.
	 * <p>
	 * Defaults to <tt>true</tt> but may be overridden by subclasses.
	 */
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public final String getOpenIdParameter() {
		return openIdParameter;
	}

	public String getProviderIdParameter() {
		return providerIdParameter;
	}

	public void setProviderIdParameter(String providerIdParameter) {
		this.providerIdParameter = providerIdParameter;
	}

}
