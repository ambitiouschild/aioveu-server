package com.aioveu.auth.api;

import com.aioveu.auth.api.fallback.AuthFeignFallbackClient;
import com.aioveu.auth.model.TenantClientInitDTO;
import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.feign.config.FeignDecoderConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: TenantFeignClient
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 20:46
 * @Version 1.0
 **/


@FeignClient(value = "aioveu-auth",
        fallback = AuthFeignFallbackClient.class,
        configuration = {FeignDecoderConfig.class}
        )
//如果服务提供方返回的是 Result<List<TenantVO>>这类包装对象，
// 那么客户端的 Feign 接口方法返回值也需要改为 Result<List<TenantVO>>，或者定义一个对应的 ResponseEntity。
//或者必须解构
//这是一个典型的服务间接口契约不一致导致的序列化/反序列化错误。
// 需要对比并统一服务提供方和消费方关于 getAccessibleTenantsByUsername接口的返回值类型定义。

public interface AuthFeignClient {

    /**
     * 根据租户名初始化客户端
     *
     * @param   dto
     * @return {@link Boolean}
     */
    @Operation(summary = "根据租户名初始化客户端", hidden = true)
    @PostMapping("/inner/clients/init-by-tenant")
    @Log(value = "根据租户名初始化客户端", module = LogModuleEnum.AUTH)
    Result<Boolean> initClientByTenant(@RequestBody TenantClientInitDTO dto);

}
