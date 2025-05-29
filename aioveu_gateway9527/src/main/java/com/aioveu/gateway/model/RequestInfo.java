package com.aioveu.gateway.model;

import lombok.Data;
import org.springframework.util.AntPathMatcher;

/**
 * @author 雒世松
 * @description 请求参数信息
 */
@Data
public class RequestInfo {

    private String host;

    private String url;

    private String method;

    private String body;

    private String header;

    private Long requestTime;

    public static void main(String[] args) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("GET:/api/api/v1/store-config/*/*", "GET:/api/api/v1/store-config/874/CANCEL_APPOINTMENT_CLASSES_TIME");
        System.out.println(match);
    }
}
