package com.aioveu.common.security.tenant.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName: TenantPublicProperties
 * @Description TODO  专门处理属性绑定
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/21 15:38
 * @Version 1.0
 **/
@Component
@ConfigurationProperties(prefix = "aioveu")
@Data
public class TenantPublicProperties{

    /**
     * 公共租户接口路径
     * 必须同时满足：
     * 1. 在 security.whitelist-paths 中
     * 2. 需要 tenantId
     */
    private List<String> whitelistPaths = Collections.emptyList();
}
