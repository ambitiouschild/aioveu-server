package com.aioveu.common.security.config;


import com.aioveu.common.security.service.TenantLoader;
import com.aioveu.tenant.api.TenantFeignClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @ClassName: CommonTenantFeignAutoConfiguration
 * @Description TODO 租户解析自动装配规则
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 18:53
 * @Version 1.0
 **/

/**
 * ✅ 租户解析自动装配规则：
 *
 * 1. common-security 提供 PublicTenantFilter 与 TenantLoader 接口
 * 2. common-tenant-api 提供 Feign Client 与自动装配
 * 3. 非租户服务仅需引入依赖，无需任何配置
 * 4. aioveu-tenant 显式启用 DB 模式，禁用 Feign 自调用
 *
 * 原则：
 * - 最小化配置
 * - 避免样板代码
 * - 明确服务角色
 */
@Configuration
@ConditionalOnClass(TenantFeignClient.class)
@ConditionalOnBean(TenantFeignClient.class)
@ConditionalOnMissingBean(TenantLoader.class) // ✅ 关键 “只有在还没有 TenantLoader Bean 时，我才生效”
@AutoConfigureAfter(FeignAutoConfiguration.class)
public class CommonTenantFeignAutoConfiguration {

    @Bean
    @Primary
    public TenantLoader feignTenantLoader(TenantFeignClient tenantFeignClient) {
        return clientId -> tenantFeignClient.getTenantIdByClientId(clientId).getData();
    }
}
