package com.aioveu.oms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: MockProperties
 * @Description TODO 模拟支付配置
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/23 12:47
 * @Version 1.0
 **/

@Data
@ConfigurationProperties(prefix = "pay.mock")
public class MockProperties {

    /**
     * 是否启用模拟支付
     */
    private Boolean enabled = true;

    /**
     * 模拟支付是否自动成功
     */
    private Boolean autoSuccess = true;

    /**
     * 模拟延迟（毫秒）
     */
    private Integer delay = 1000;

    /**
     * 模拟成功率（0-100）
     */
    private Integer successRate = 100;
}
