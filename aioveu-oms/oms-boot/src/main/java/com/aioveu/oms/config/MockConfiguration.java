package com.aioveu.oms.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @ClassName: WxPayMockConfiguration
 * @Description TODO  模拟支付配置
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/23 12:46
 * @Version 1.0
 **/

@Slf4j
@Configuration
@ConditionalOnClass(MockConfiguration.class)
@EnableConfigurationProperties(MockProperties.class)
@AllArgsConstructor
public class MockConfiguration {

    /**
     * 模拟支付服务
     */
    @Bean
    @Primary  // 设为主要Bean，优先使用
    public MockPayService mockPayService(MockProperties properties) {
        return new MockPayService(properties);
    }
}
