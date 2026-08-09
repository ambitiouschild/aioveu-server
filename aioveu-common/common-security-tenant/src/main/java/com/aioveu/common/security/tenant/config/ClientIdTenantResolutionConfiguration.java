package com.aioveu.common.security.tenant.config;

import com.aioveu.common.security.core.config.property.SecurityFilterOrders;
import com.aioveu.common.security.tenant.filter.ClientIdTenantResolutionFilter;
import com.aioveu.common.security.tenant.service.TenantLoader;
import com.aioveu.tenant.api.TenantFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
/**
 * @ClassName: ClientIdTenantResolutionConfiguration
 * @Description TODO ClientIdTenantResolutionConfiguration
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/8/9 18:53
 * @Version 1.0
 **/

@Slf4j
@Configuration
public class ClientIdTenantResolutionConfiguration {

    @Bean
    public FilterRegistrationBean<ClientIdTenantResolutionFilter> clientIdTenantFilter(
            ClientIdTenantResolutionFilter filter
    ) {
        FilterRegistrationBean<ClientIdTenantResolutionFilter> bean =
                new FilterRegistrationBean<>(filter);

        bean.setName("clientIdTenantResolutionFilter");
        bean.addUrlPatterns("/public/*");
        bean.setOrder(SecurityFilterOrders.PUBLIC_TENANT_FILTER); // -210

        return bean;
    }
}
