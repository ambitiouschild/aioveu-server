package com.aioveu.gateway.filter;


import com.aioveu.gateway.service.ClientWhitelistWithRedisService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ClientIdGatewayFilterFactory
 * @Description TODO 方式二（更规范）：路由级 GatewayFilter（企业常用）
 *                      只对某些路由生效（比如只允许内部 client_id 访问某服务）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/3 16:13
 * @Version 1.0
 **/
/*
*
* spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://aioveu-user
          predicates:
            - Path=/aioveu-user/**
          filters:
            - name: ClientIdGatewayFilter
              args:
                allowedClientIds: system,app
*
*
* */
@Component
public class ClientIdGatewayFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<ClientIdGatewayFilterGatewayFilterFactory.Config> {


    private final ReactiveJwtDecoder jwtDecoder;
    private final ClientWhitelistWithRedisService clientWhitelistWithRedisService;

    public ClientIdGatewayFilterGatewayFilterFactory(
            @Qualifier("gatewayJwtDecoder") @Lazy ReactiveJwtDecoder jwtDecoder,
            ClientWhitelistWithRedisService clientWhitelistWithRedisService
    ) {
        super(Config.class);
        this.jwtDecoder = jwtDecoder;
        this.clientWhitelistWithRedisService = clientWhitelistWithRedisService;
    }



    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    @Override
    public GatewayFilter apply(Config config) {
// ✅ Config 只在这里使用
        return new ClientIdGatewayFilter(
                jwtDecoder,
                clientWhitelistWithRedisService
        );
    }

    @Data
    public static class Config {
        // 以后可以在 yml 里用
        // args:
        //   allowedClientIds: app,system
    }
}
