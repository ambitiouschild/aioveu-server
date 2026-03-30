package com.aioveu.auth.service;

import com.aioveu.tenant.dto.TenantWxAppInfo;

/**
 * @ClassName: WxConfigService
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/30 13:59
 * @Version 1.0
 **/

public interface WxConfigService {

    /**
     * 根据客户端ID获取微信配置
     */
    TenantWxAppInfo getConfigByClientId(String clientId);

    /**
     * 根据租户ID获取微信配置
     */
    TenantWxAppInfo getConfigByTenantId(Long tenantId);

}
