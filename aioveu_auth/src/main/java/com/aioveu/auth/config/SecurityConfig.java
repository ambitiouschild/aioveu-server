package com.high.sport.auth.config;

import com.aioveu.auth.authentication.SecurityConstants;
import com.aioveu.auth.authentication.mobile.MobilePasswordSecurityConfig;
import com.aioveu.auth.authentication.openid.OpenIdAuthenticationSecurityConfig;
import com.aioveu.auth.authentication.sms.SmsCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//一个继承自WebSecurityConfigurerAdapter的安全配置类，这已经过时
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/*/**
 * @Description: TODO Spring Security 6.x的变化。主要的区别是WebSecurityConfigurerAdapter已经被弃用，现在推荐使用基于组件的配置，即通过创建SecurityFilterChain Bean来配置安全规则。同时，AuthenticationManager现在通过AuthenticationConfiguration来获取，而不是覆盖authenticationManagerBean方法。
 * @Author: 雒世松
 * @Date: 2025/5/29 17:55
 * @param null
 * @return:
 **/

//Spring Security 6.x+ 安全配置（兼容 Spring Boot 3.x）
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    //将configure(HttpSecurity)方法转换为返回SecurityFilterChain Bean的方法
    //处理不同的认证方式,手机密码、短信验证码和OpenID登录。

    //用户详情接口

    private UserDetailsService userDetailsService;
    //手机密码安全接口

    private MobilePasswordSecurityConfig mobilePasswordSecurityConfig;
    //短信编码安全接口

    private SmsCodeSecurityConfig smsCodeSecurityConfig;
    //短信编码安全接口

    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;


    //重写继承类方法
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //todo 允许表单登录
//        http
//                //注入自定义的授权配置类
//                .apply(mobilePasswordSecurityConfig)
//                .and()
//                .apply(smsCodeSecurityConfig)
//                .and()
//                .apply(openIdAuthenticationSecurityConfig)
//                .and()
//                .authorizeRequests()
//                //注销的接口需要放行
//                //antMatchers，这在Spring Security 6.x中已经被requestMatchers取代。需要将antMatchers替换为requestMatchers，并调整相应的路径匹配方式。
//                .antMatchers("/oauth/logout", SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_OPENID).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginProcessingUrl("/login")
//                .permitAll()
//                .and()
//                //原来的.csrf().disable()现在应该使用.csrf(AbstractHttpConfigurer::disable)。
//                .csrf()
//                .disable();
//    }

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          MobilePasswordSecurityConfig mobilePasswordSecurityConfig,
                          SmsCodeSecurityConfig smsCodeSecurityConfig,
                          OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig) {
        this.userDetailsService = userDetailsService;
        this.mobilePasswordSecurityConfig = mobilePasswordSecurityConfig;
        this.smsCodeSecurityConfig = smsCodeSecurityConfig;
        this.openIdAuthenticationSecurityConfig = openIdAuthenticationSecurityConfig;
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        return new JwtEnhanceAuthenticationConverter();
    }
//    //重写继承类方法
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //从数据库中查询用户信息
//        auth.userDetailsService(userDetailsService);
//    }

    /**
     * 安全过滤器链配置 新版组件式配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 应用自定义安全配置
        applyCustomSecurityConfigs(http);

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( // 新版 (6.x)路径匹配语法升级
                                "/oauth/logout",
                                SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_OPENID
                        ).permitAll()  // 放行注销和OpenID登录端点
                        .anyRequest().authenticated()  // 其他请求需要认证
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                //配置方法改进新版lambda表达式
                .formLogin(form -> form
                        .loginProcessingUrl("/login")  // 登录处理端点
                        .permitAll()  // 允许所有人访问登录页
                )
                .csrf(AbstractHttpConfigurer::disable);  // 禁用CSRF（根据业务需求决定）

        return http.build();
    }


    /**
     * 应用自定义安全配置
     */
    private void applyCustomSecurityConfigs(HttpSecurity http) throws Exception {
        mobilePasswordSecurityConfig.configure(http);
        smsCodeSecurityConfig.configure(http);
        openIdAuthenticationSecurityConfig.configure(http);
    }


    /**
     * AuthenticationManager对象在OAuth2认证服务中要使用，提前放入IOC容器中
     * Oauth的密码模式需要
     */

    //AuthenticationManager的配置也需要调整。原来的configure(AuthenticationManagerBuilder)方法用于设置UserDetailsService，现在应该通过定义一个DaoAuthenticationProvider Bean，并将其添加到AuthenticationManager中。同时，需要确保PasswordEncoder被正确配置。
    //这在新版本中不再需要，因为AuthenticationManager可以通过注入AuthenticationConfiguration来获取。因此，需要删除这个方法，并改为通过@Bean方法提供AuthenticationManager。
    //重写继承类方法
//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    /**
     * 认证管理器（替换旧版 authenticationManagerBean） 认证管理器获取方式
     */
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(daoProvider);
    }

    /**
     * 密码编码器,密码编码器显式配置// 必须显式声明（不再自动创建）
     *
     * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * DAO 认证提供者（兼容旧版 configure(AuthenticationManagerBuilder)）
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}