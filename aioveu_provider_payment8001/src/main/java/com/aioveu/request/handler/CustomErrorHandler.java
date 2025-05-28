package com.aioveu.request.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/1/27 17:23
 */
@Slf4j
public class CustomErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return true;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        if (clientHttpResponse.getStatusCode() != HttpStatus.OK) {
            log.error(clientHttpResponse.getStatusCode() + ":" + clientHttpResponse.getStatusText());
        }
    }
}
