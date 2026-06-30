package com.aioveu.gateway.service;


import com.aioveu.tenant.dto.TenantWxAppInfo;
import reactor.core.publisher.Mono;

/**
 * @ClassName: TenantQueryService
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/30 17:58
 * @Version 1.0
 **/

public interface TenantQueryService {

    Mono<TenantWxAppInfo> getTenantWxAppInfoByClientId(String clientId);

    Mono<Long> getTenantIdByClientId(String clientId);

}
