package com.aioveu.request.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 动态代理对象
 * @author: 雒世松
 * @date: 2025/02/20 15:24
 */
@Data
public class DynamicProxyIP implements Serializable {

    private String ip;

    private Integer port;

    /**
     * 有效期 单位：秒
     */
    @JsonProperty("ip_remain")
    private Integer ipRemain;

}
