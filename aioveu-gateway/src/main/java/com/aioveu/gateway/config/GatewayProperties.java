package com.aioveu.gateway.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: GatewayProperties
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/16 21:26
 * @Version 1.0
 **/
@Data
@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {

    /**
     * Auth 服务地址（用于 JWKS）
     * 示例：http://aioveu-auth
     */
    private String endpoint;
}
