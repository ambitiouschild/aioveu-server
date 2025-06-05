package com.aioveu.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.aioveu.common.result.ResultCode;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @Description: TODO 自定义网关流控异常
 * @Author: 雒世松
 * @Date: 2025/6/5 16:36
 * @param
 * @return:
 **/

@Configuration
public class SentinelConfig {

    @PostConstruct
    private void initBlockHandler() {
        GatewayCallbackManager.setBlockHandler(
                (exchange, t) ->
                        ServerResponse
                                .status(HttpStatus.TOO_MANY_REQUESTS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(
                                        BodyInserters.fromValue(ResultCode.FLOW_LIMITING.toString())
                                )
        );
    }
}
