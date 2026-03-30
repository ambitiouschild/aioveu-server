package com.aioveu.auth.service.impl;

import com.aioveu.auth.service.AuthService;
import com.aioveu.auth.service.WxConfigService;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName: WxConfigServiceImpl
 * @Description TODO 微信服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/30 14:08
 * @Version 1.0
 **/
@Slf4j
@Service   // 标识为Spring服务层组件，由Spring容器管理，处理业务逻辑
@RequiredArgsConstructor
public class WxConfigServiceImpl implements WxConfigService {


    private final TenantFeignClient tenantFeignClient;

    /**
     * 根据客户端ID获取微信配置
     */
    @Override
    public TenantWxAppInfo getConfigByClientId(String clientId) {

        log.info("【WxConfigService】开始查询clientId: {}", clientId);

        // 这里需要你实现数据库查询
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("【WxConfigService】查询到的tenantWxAppInfo: {}", tenantWxAppInfo);

        if (tenantWxAppInfo == null) {
//            throw new OAuth2AuthenticationException("无效的客户端ID");
            log.info("【WxConfigService】无效的客户端ID: {}", clientId);
        }

        String wxAppid = tenantWxAppInfo.getWxAppid();
        Long tenantId = tenantWxAppInfo.getTenantId();
        String appSecret = tenantWxAppInfo.getAppSecret();
        log.info("【WxConfigService】查询到租户信息 - wxAppid: {}, tenantId: {}, appSecret: {}", wxAppid, tenantId,appSecret);

        return tenantWxAppInfo;
    }

    /**
     * 根据租户ID获取微信配置
     */
    @Override
    public TenantWxAppInfo getConfigByTenantId(Long tenantId) {

        log.info("开始查询clientId: {}", tenantId);

        // 这里需要你实现数据库查询
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByTenantId(tenantId);

        log.info("查询到的tenantWxAppInfo: {}", tenantWxAppInfo);

        if (tenantWxAppInfo == null) {
//            throw new OAuth2AuthenticationException("无效的客户端ID");
            log.info("无效的租户ID: {}", tenantId);
        }

        String wxAppid = tenantWxAppInfo.getWxAppid();
        String appSecret = tenantWxAppInfo.getAppSecret();
        log.info("查询到租户信息 - wxAppid: {}, tenantId: {}, appSecret: {}", wxAppid, tenantId,appSecret);

        return tenantWxAppInfo;
    }
}
