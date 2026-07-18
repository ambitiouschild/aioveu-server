package com.aioveu.common.security.service.Impl;


import com.aioveu.common.result.Result;
import com.aioveu.tenant.api.TenantFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName: TenantQueryService
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 10:36
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantQueryService {

    private final TenantFeignClient tenantFeignClient;

    public Long getTenantIdByClientId(String clientId) {
        try {
            Result<Long> result = tenantFeignClient
                    .getTenantIdByClientId(clientId);

            if (result == null) {
                throw new IllegalStateException("Tenant not found");
            }
            return result.getData();
        } catch (Exception e) {
            log.error("【TenantQueryService】查询 tenantId 失败, clientId:{}", clientId, e);
            throw e; // 交给 Cache / Filter 处理
        }
    }
}
