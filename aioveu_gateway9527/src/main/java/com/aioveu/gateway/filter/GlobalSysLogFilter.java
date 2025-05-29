package com.aioveu.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.aioveu.gateway.model.RequestInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author 雒世松
 * @description 网关层记录日志
 */
@Component
@Slf4j
public class GlobalSysLogFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 记录请求开始时间
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setRequestTime(System.currentTimeMillis());
        exchange.getAttributes().put(START_TIME, requestInfo.getRequestTime());
        ServerHttpRequest request = exchange.getRequest();
        requestInfo.setUrl(request.getPath().value());
        requestInfo.setHost(request.getURI().getHost());
        requestInfo.setMethod(request.getMethodValue());
        HttpHeaders headers = request.getHeaders();

        //获取请求体
        requestInfo.setHeader(JSON.toJSONString(headers.toSingleValueMap()));
        if (MediaType.APPLICATION_JSON.isCompatibleWith(headers.getContentType())
                ||MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(headers.getContentType())){
            String body = resolveBody(exchange);
            requestInfo.setBody(body);
        } else if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(headers.getContentType())){
            requestInfo.setBody(null);
        }else {
            requestInfo.setBody(JSON.toJSONString(request.getQueryParams().toSingleValueMap()));
        }
        //写入日志
        writeLog(requestInfo);
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                long executeTime = (System.currentTimeMillis() - startTime);
                // 打印执行时间
                log.info(exchange.getRequest().getURI().getRawPath() + " : " + executeTime + "ms");
            }
        }));
    }

    private String resolveBody(ServerWebExchange exchange){
        Object cachedBody = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
        if (cachedBody!=null){
            NettyDataBuffer buffer = (NettyDataBuffer) cachedBody;
            return buffer.toString(Charsets.UTF_8);
        }
        return null;
    }

    private void writeLog(RequestInfo requestInfo){
        log.info("收到一条请求，请求参数：{}", JSON.toJSONString(requestInfo));
        //TODO 日志持久化
    }

    @Override
    public int getOrder() {
        // 设置此过滤器的执行优先级，数值越大，执行越晚
        return 0;
    }
}
