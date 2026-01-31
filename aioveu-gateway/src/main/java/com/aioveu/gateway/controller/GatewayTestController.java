package com.aioveu.gateway.controller;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @ClassName: GatewayTestController
 * @Description TODO   创建测试端点
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/16 18:09
 * @Version 1.0
 **/

@RestController
@RequestMapping("/gateway/test")
public class GatewayTestController {

    @GetMapping("/ping")
    @Log(value = "网关测试", module = LogModuleEnum.GATEWAY)
    public Mono<String> ping() {
        return Mono.just("Gateway is working at " + System.currentTimeMillis());
    }

    @GetMapping("/routes")
    public Mono<String> routes() {
        return Mono.just("Available routes: /api/v1/auth/**, /api/v1/system/**");
    }
}
