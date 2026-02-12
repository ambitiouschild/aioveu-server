package com.aioveu.pay.aioveuModule.service.MockPay.configuration;

import com.aioveu.pay.aioveuModule.service.MockPay.MockRequestFactory.MockRequestFactory;
import com.aioveu.pay.aioveuModule.service.MockPay.config.MockPayConfig;
import com.aioveu.pay.aioveuModule.service.MockPay.service.impl.MockPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: MockConfiguration
 * @Description TODO  模拟支付配置类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 18:40
 * @Version 1.0
 **/

@Slf4j
@Configuration
@EnableConfigurationProperties(MockPayConfig.class)   // 只保留这个
@ConditionalOnProperty(prefix = "pay.mock", name = "enabled", havingValue = "true")
public class MockConfiguration {

    /**
     * 创建模拟支付服务
     */
    @Bean
    public MockPayServiceImpl mockPayService(
            MockRequestFactory requestFactory,
            MockPayConfig mockPayConfig) {
        log.info("正在创建模拟支付服务...");

        // 验证配置
        if (!mockPayConfig.isValid()) {
            log.error("模拟支付配置无效");
            throw new IllegalArgumentException("模拟支付配置无效");
        }

        MockPayServiceImpl service = new MockPayServiceImpl(requestFactory ,mockPayConfig);
        log.info("模拟支付服务创建成功");

        return service;
    }

//    /**
//     * 可选：创建模拟支付工具类
//     */
//    @Bean
//    public MockPayUtils mockPayUtils(MockProperties properties) {
//        return new MockPayUtils(properties);
//    }
}
