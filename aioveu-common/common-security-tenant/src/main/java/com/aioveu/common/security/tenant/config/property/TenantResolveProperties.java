package com.aioveu.common.security.tenant.config.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TenantResolveProperties
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/29 18:53
 * @Version 1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = "common.security.tenant")
public class TenantResolveProperties {

    /**
     * 是否启用租户解析
     */
    private boolean enabled = false;

    /**
     * 白名单路径（如 /public/**）
     */
    private List<String> whitelistPaths = new ArrayList<>();

    // getter / setter
}
