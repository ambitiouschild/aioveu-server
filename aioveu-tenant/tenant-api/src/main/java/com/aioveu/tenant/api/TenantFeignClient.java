package com.aioveu.tenant.api;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.common.web.config.FeignDecoderConfig;
import com.aioveu.tenant.api.fallback.TenantFeignFallbackClient;
import com.aioveu.tenant.dto.TenantVO;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import com.aioveu.tenant.dto.UserAuthInfoWithTenantId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
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


@FeignClient(value = "aioveu-tenant",
        fallback = TenantFeignFallbackClient.class,
        configuration = {FeignDecoderConfig.class}
        )
//如果服务提供方返回的是 Result<List<TenantVO>>这类包装对象，
// 那么客户端的 Feign 接口方法返回值也需要改为 Result<List<TenantVO>>，或者定义一个对应的 ResponseEntity。
//或者必须解构
//这是一个典型的服务间接口契约不一致导致的序列化/反序列化错误。
// 需要对比并统一服务提供方和消费方关于 getAccessibleTenantsByUsername接口的返回值类型定义。

//@RequestMapping("/api/v1")  // FeignClient 接口上不允许使用 @RequestMapping注解。需要在每个方法上写完整路径。
public interface TenantFeignClient {

    /**
     * 根据用户名和租户ID获取认证信息（用于多租户登录）
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return {@link UserAuthInfoWithTenantId}
     */
    @Operation(summary = "根据用户名和租户ID获取认证信息（用于多租户登录）", hidden = true)
    @GetMapping("/api/v1/users/{username}/{tenantId}/authInfo")
    @Log(value = "根据用户名和租户ID获取认证信息（用于多租户登录）", module = LogModuleEnum.TENANT)
    UserAuthInfoWithTenantId getUserAuthInfoWithTenantId(@PathVariable String username,@PathVariable Long tenantId);


    /**
     * 获取当前用户的租户列表
     * <p>
     * 根据当前登录用户查询其所属的所有租户
     * </p>
     *
     * @return 租户列表
     */
    @Operation(summary = "新增:根据用户名获取可登录的租户列表")
    @GetMapping("/api/v1/users/tenants/{username}")
    @Log(value = "新增：根据用户名获取可登录的租户列表）", module = LogModuleEnum.TENANT)
    List<TenantVO> getAccessibleTenantsByUsername(@PathVariable String username);

    /**
     * 切换租户
     * <p>
     * 切换当前用户的租户上下文，需要验证用户是否有权限访问该租户
     * </p>
     *
     * @param tenantId 目标租户ID
     * @return 切换结果
     */
    @Operation(summary = "切换租户")
    @PostMapping("/api/v1/tenants/{tenantId}/switch")
    @Log(value = "新增：根据用户名获取可登录的租户列表）", module = LogModuleEnum.TENANT)
    Result<TenantVO> switchTenant(
            @Parameter(description = "租户ID") @PathVariable Long tenantId,
            HttpServletRequest request
    );

    /**
     * 检查用户是否可以访问指定租户
     * <p>
     * 验证该用户名在目标租户下是否存在账户
     * </p>
     *
     * @param userId   用户ID
     * @param tenantId 租户ID
     * @return true-可访问，false-不可访问
     */
    @Operation(summary = "检查用户是否可以访问指定租户")
    @GetMapping("/api/v1/tenants/canAccessTenant")
    @Log(value = "检查用户是否可以访问指定租户）", module = LogModuleEnum.TENANT)
    boolean canAccessTenant(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "租户ID") @RequestParam Long tenantId
    );

    /**
     * 检查是否具备租户切换权限
     * <p>
     * 验证是否具备租户切换权限
     * </p>
     * @return true-可切换，false-不可切换
     */
    @Operation(summary = "检查是否具备租户切换权限")
    @GetMapping("/api/v1/tenants/hasTenantSwitchPermission")
    @Log(value = "检查是否具备租户切换权限）", module = LogModuleEnum.TENANT)
    Result<Boolean> hasTenantSwitchPermission();

    @Operation(summary = "通过 clientId 获取租户和小程序信息")
    @GetMapping("/api/v1/oauth-client-wx-app/getTenantWxAppInfoByClientId") // ✅ 应该改为GET
    @Log(value = "通过 clientId 获取租户和小程序信息）", module = LogModuleEnum.TENANT)
    TenantWxAppInfo getTenantWxAppInfoByClientId(
            @Parameter(description = "clientId") @RequestParam("clientId") String  clientId
    );


    @Operation(summary = "通过 tenantId 获取租户和小程序信息")
    @GetMapping("/api/v1/oauth-client-wx-app/getTenantWxAppInfoByTenantId") // ✅ 应该改为GET
    @Log(value = "通过 tenantId 获取租户和小程序信息）", module = LogModuleEnum.TENANT)
    TenantWxAppInfo getTenantWxAppInfoByTenantId(
            @Parameter(description = "clientId") @RequestParam("clientId") Long  tenantId
    );

}
