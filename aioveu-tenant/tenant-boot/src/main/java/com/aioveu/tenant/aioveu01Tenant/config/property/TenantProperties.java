package com.aioveu.tenant.aioveu01Tenant.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TenantProperties
 * @Description TODO 多租户配置属性
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 14:20
 * @Version 1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {

    /**
     * 租户字段名
     * 默认：tenant_id
     */
    private String column = "tenant_id";

    /**
     * 忽略多租户过滤的表名列表
     * 系统表、租户表等不需要租户隔离的表
     */
    private List<String> ignoreTables = new ArrayList<>();
}
