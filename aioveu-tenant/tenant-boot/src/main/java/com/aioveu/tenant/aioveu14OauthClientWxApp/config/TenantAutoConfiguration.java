package com.aioveu.tenant.aioveu14OauthClientWxApp.config;


import com.aioveu.common.security.service.TenantLoader;
import com.aioveu.tenant.aioveu14OauthClientWxApp.service.OauthClientWxAppService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @ClassName: TenantAutoConfiguration
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 18:27
 * @Version 1.0
 **/

/**
 * ✅ 架构合理性说明（对标大厂中台多租户模型）：
 *
 * 1. Filter 位于 common-security，统一入口
 * 2. TenantLoader 接口解耦查询方式
 * 3. aioveu-tenant 提供 DB 实现（权威数据源）
 * 4. 其他服务通过 Feign 实现（跨服务查询）
 * 5. 自动装配保证单 JVM 内唯一 TenantLoader
 * 6. 禁止自调用 / Filter 内 Feign
 * 7. Gateway 仅负责 Header 注入
 *
 * 符合：
 * - Spring Boot 自动装配规范
 * - 微服务单一职责
 * - 中台多租户隔离模型
 *
 * 可长期演进，适合生产环境。
 */
/**
 * ⚠️ TenantLoader Bean 冲突解决方案：
 *
 * - Feign 版 TenantLoader：
 *     @ConditionalOnMissingBean
 *     作为默认实现，供非租户服务使用
 *
 * - DB 版 TenantLoader：
 *     @ConditionalOnProperty(name="tenant.mode", havingValue="db")
 *     作为 aioveu-tenant 的显式实现
 *
 * 原则：
 * - 一个 JVM 只有一个 TenantLoader
 * - 租户服务优先使用 DB
 * - 非租户服务自动降级到 Feign
 */
@Configuration
@ConditionalOnProperty(
        name = "tenant.mode",
        havingValue = "db",
        matchIfMissing = false
)
public class TenantAutoConfiguration {

    @Bean
    @Primary
    public TenantLoader dbTenantLoader(OauthClientWxAppService oauthClientWxAppService) {
        return clientId -> oauthClientWxAppService.getTenantIdByClientId(clientId);
    }
}
