package com.aioveu.tenant.api;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.common.web.config.FeignDecoderConfig;
import com.aioveu.tenant.api.fallback.TenantFeignFallbackClient;
import com.aioveu.tenant.dto.TenantVO;
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
        configuration = {FeignDecoderConfig.class})

@RequestMapping("/api/v1")  // 添加公共路径前缀
public interface TenantFeignClient {

    /**
     * 根据用户名和租户ID获取认证信息（用于多租户登录）
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return {@link UserAuthInfoWithTenantId}
     */
    @Operation(summary = "根据用户名和租户ID获取认证信息（用于多租户登录）", hidden = true)
    @GetMapping("/users/{username}/{tenantId}/authInfo")
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
    @GetMapping("/users/tenants/{username}")
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
    @PostMapping("/tenants/{tenantId}/switch")
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
    @GetMapping("/tenants/canAccessTenant")
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
    @GetMapping("/tenants/hasTenantSwitchPermission")
    @Log(value = "检查是否具备租户切换权限）", module = LogModuleEnum.TENANT)
    boolean hasTenantSwitchPermission();

}
