package com.aioveu.gateway.util;

import cn.hutool.json.JSONUtil;
import com.aioveu.common.result.Result;
import com.aioveu.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @Description: TODO WebFlux 响应处理器
 * @Author: 雒世松
 * @Date: 2025/6/5 16:37
 * @param
 * @return:
 **/

@Slf4j
public class WebFluxUtils {

    public static Mono<Void> writeErrorResponse(ServerHttpResponse response, ResultCode resultCode) {
        HttpStatus status = determineHttpStatus(resultCode);
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().setAccessControlAllowOrigin("*");
        response.getHeaders().setCacheControl("no-cache");

        String responseBody = JSONUtil.toJsonStr(Result.failed(resultCode));
        DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer))
                .doOnError(error -> {
                    DataBufferUtils.release(buffer);
                    log.error("Error writing response: {}", error.getMessage());
                });
    }

    private static HttpStatus determineHttpStatus(ResultCode resultCode) {
        return switch (resultCode) {
            case ACCESS_UNAUTHORIZED, TOKEN_INVALID -> HttpStatus.UNAUTHORIZED;
            case TOKEN_ACCESS_FORBIDDEN -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.BAD_REQUEST;
        };
    }


}
