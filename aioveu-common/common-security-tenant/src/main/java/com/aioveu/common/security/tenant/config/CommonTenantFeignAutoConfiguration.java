package com.aioveu.common.security.tenant.config;

import com.aioveu.common.security.tenant.service.TenantLoader;
import com.aioveu.tenant.api.TenantFeignClient;
import lombok.extern.slf4j.Slf4j;
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
 * @Description TODO 租户解析自动装配规则  它和 TenantAutoConfiguration是一对“互补关系”，不是竞争关系。
 *                              1️它属于「资源服务侧的自动装配」
 *                              2️它解决的是「非租户服务 / 资源服务」的问题
 *                                非租户服务仅需引入依赖，无需任何配置
 *                                 这是“教科书级”的 Spring Boot 自动装配写法
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
@Slf4j
@Configuration
@ConditionalOnClass(TenantFeignClient.class)    //类路径有 Feign Client 才考虑
@ConditionalOnBean(TenantFeignClient.class)     //Spring 容器里真的有这个 Bean
@ConditionalOnMissingBean(TenantLoader.class) // ✅ 关键 “只有在还没有 TenantLoader Bean 时，我才生效” 防止和 DB 版冲突（最关键）
@AutoConfigureAfter(FeignAutoConfiguration.class) //确保 Feign 已就绪
public class CommonTenantFeignAutoConfiguration {

    @Bean
    @Primary
    public TenantLoader feignTenantLoader(TenantFeignClient tenantFeignClient) {
        return clientId -> {
            log.debug("[FeignTenantLoader] resolve tenantId by clientId={}", clientId);
            return tenantFeignClient.getTenantIdByClientId(clientId).getData();
        };
    }
}
