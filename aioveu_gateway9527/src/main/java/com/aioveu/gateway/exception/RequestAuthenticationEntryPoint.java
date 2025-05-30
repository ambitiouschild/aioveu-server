package com.aioveu.gateway.exception;

import cn.hutool.json.JSONUtil;
import com.aioveu.auth.common.model.ResultCode;
import com.aioveu.auth.common.model.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * @author 雒世松
 * 用于处理没有登录或token过期时的自定义返回结果
 */
@Slf4j
@Component
public class RequestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body= JSONUtil.toJsonStr(new ResultMsg(ResultCode.INVALID_TOKEN.getCode(),ResultCode.INVALID_TOKEN.getMsg(),null));
        log.error("{},{},{}",exchange.getRequest().getPath(),exchange.getRequest().getQueryParams(),body);
        DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
        return response.writeWith(Mono.just(buffer));
    }
}