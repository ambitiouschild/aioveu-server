package com.aioveu.tenant.api;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.common.web.config.FeignDecoderConfig;
import com.aioveu.tenant.api.fallback.TenantFeignFallbackClient;
import com.aioveu.tenant.dto.UserAuthInfoWithTenantId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ClassName: TenantFeignClient
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 20:46
 * @Version 1.0
 **/


@FeignClient(value = "aioveu-tenant",
        fallback = TenantFeignFallbackClient.class,
        configuration = {FeignDecoderConfig.class})
public interface TenantFeignClient {

    /**
     * 根据用户名和租户ID获取认证信息（用于多租户登录）
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return {@link UserAuthInfoWithTenantId}
     */
    @Operation(summary = "根据用户名和租户ID获取认证信息（用于多租户登录）", hidden = true)
    @GetMapping("/{username}/{tenantId}/authInfo")
    @Log(value = "根据用户名和租户ID获取认证信息（用于多租户登录）", module = LogModuleEnum.USER)
    UserAuthInfoWithTenantId getUserAuthInfoWithTenantId(@PathVariable String username,@PathVariable Long tenantId);


}
