package com.aioveu.common.security.core.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Description: TODO ✅ PasswordEncoder 放 security-core：完全正确
 *                          ✅ 推荐写法（只放一份）
 * @Author: 雒世松
 * @Date: 2025/6/5 15:49
 * @param
 * @return:
 **/
@Configuration
public class SecurityBeanConfig {



    /*
    * **  TODO  不是你必须加前缀，而是：
            👉 如果用 DelegatingPasswordEncoder—— 前缀它自动加，你千万别动；
            👉 如果用 BCryptPasswordEncoder—— 根本就没有前缀，你也别加。**
            结论：你永远、永远、永远不需要手动加前缀。
    *
    * */
    /**
     * 密码编码器  放 security-core：完全正确
     * 使用Spring Security的委托密码编码器，支持多种编码格式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
