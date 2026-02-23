package com.aioveu.tenant.api.fallback;

import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.UserAuthInfoWithTenantId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName: TenantFeignFallbackClient
 * @Description TODO 多租户服务远程调用异常后的降级处理类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 20:47
 * @Version 1.0
 **/
@Component
@Slf4j
public class TenantFeignFallbackClient implements TenantFeignClient {

    @Override
    public UserAuthInfoWithTenantId getUserAuthInfoWithTenantId(String username,Long tenantId) {
        log.error("feign远程调用多租户服务异常后的降级方法");
        return new UserAuthInfoWithTenantId();
    }
}
