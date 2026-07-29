package com.aioveu.tenant.aioveu14OauthClientWxApp.config;

import com.aioveu.common.security.tenant.service.TenantLoader;
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



    /*
    *       ✅ PublicTenantResolver是“所有服务统一的调用入口”
            ✅ 它不关心配置是谁写的
            ✅ **它通过 TenantLoader这个“配置驱动的 SPI”来决定：
            ‑ 是走 Feign
            ‑ 还是走本地 DB**
            ❌ 它自己不应该再读任何 security.tenant.*开关
    *
    *
    * */
    @Bean
    @Primary
    public TenantLoader dbTenantLoader(OauthClientWxAppService oauthClientWxAppService) {
        return clientId -> oauthClientWxAppService.getTenantIdByClientId(clientId);
    }
}
