package com.aioveu.auth.authentication;

/**
 * @description 认证定义相关常量
 * @author: 雒世松
 * @date: 2020/5/31 0031 21:13
 */
public interface SecurityConstants {

	/**
	 * 默认的OPENID登录请求处理url
	 */
	String DEFAULT_SIGN_IN_PROCESSING_URL_OPENID = "/authentication/openid";

	/**
	 * openid参数名
	 */
	String DEFAULT_PARAMETER_NAME_OPENID = "openId";

	/**
	 * providerId参数名
	 */
	String DEFAULT_PARAMETER_NAME_PROVIDERID = "providerId";
	
	/**
	 * 默认的处理验证码的url前缀
	 */
	String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";
	/**
	 * 当请求需要身份认证时，默认跳转的url
	 *
	 */
	String DEFAULT_UNAUTHENTICATION_URL = "/authentication/require";
	/**
	 * 默认的用户名密码登录请求处理url
	 */
	String DEFAULT_SIGN_IN_PROCESSING_URL_FORM = "/authentication/form";
	/**
	 * 默认的手机验证码登录请求处理url
	 */
	String DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE = "/authentication/mobile";

	/**
	 * 默认登录页面
	 *
	 * @see SecurityController
	 */
	String DEFAULT_SIGN_IN_PAGE_URL = "/imooc-signIn.html";
	/**
	 * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";
	/**
	 * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_CODE_SMS = "SPORT_PHONE_CODE_LOGIN_";
	/**
	 * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
	 */
	String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";

	/**
	 * 发送登录验证码的参数
	 */
	String DEFAULT_PARAMETER_NAME_CODE = "code";


	/**
	 * session失效默认的跳转地址
	 */
	String DEFAULT_SESSION_INVALID_URL = "/imooc-session-invalid.html";
	/**
	 * 获取第三方用户信息的url
	 */
	String DEFAULT_SOCIAL_USER_INFO_URL = "/social/user";

	/**
	 * openId登录微信code存储前缀
	 */
	String OPEN_ID_LOGIN_CODE = "SPORT_OPEN_ID_LOGIN_";
}
