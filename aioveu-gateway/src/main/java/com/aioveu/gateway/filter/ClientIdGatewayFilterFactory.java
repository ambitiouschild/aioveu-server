package com.aioveu.gateway.filter;


import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

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
public class ClientIdGatewayFilterFactory  extends AbstractGatewayFilterFactory<ClientIdGatewayFilterFactory.Config> {

    public ClientIdGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String clientId = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-Client-Id");

            if (!config.getAllowedClientIds().contains(clientId)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {
        private List<String> allowedClientIds;
    }
}
