package com.aioveu.auth.service;


/**
 * @ClassName: ClientWhitelistService
 * @Description TODO 白名单客户端服务
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 20:31
 * @Version 1.0
 **/

public interface ClientWhitelistService {


    /**
     * 判断 clientId 是否合法
     */
    boolean isValid(String clientId);
}
