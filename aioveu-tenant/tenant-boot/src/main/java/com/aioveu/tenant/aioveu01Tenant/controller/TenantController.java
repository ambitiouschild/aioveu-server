package com.aioveu.tenant.aioveu01Tenant.controller;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.common.tenant.TenantContextHolder;
import com.aioveu.tenant.aioveu01Tenant.model.form.TenantCreateForm;
import com.aioveu.tenant.aioveu01Tenant.model.form.TenantForm;
import com.aioveu.tenant.aioveu01Tenant.model.query.TenantQuery;
import com.aioveu.tenant.aioveu01Tenant.model.vo.TenantCreateResultVO;
import com.aioveu.tenant.aioveu01Tenant.model.vo.TenantPageVO;
import com.aioveu.tenant.aioveu01Tenant.model.vo.TenantVO;
import com.aioveu.tenant.aioveu01Tenant.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: TenantController
 * @Description TODO  租户管理控制器
 *                      * <p>
 *                      * 提供租户切换、查询等功能
 *                      * </p>
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 14:46
 * @Version 1.0
 **/
@Tag(name = "01.租户管理接口")
@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
@Slf4j
public class TenantController {

    private final TenantService tenantService;


    /**
     * 获取当前用户的租户列表
     * <p>
     * 根据当前登录用户查询其所属的所有租户
     * </p>
     *
     * @return 租户列表
     */
    @Operation(summary = "获取当前用户可访问的租户列表")
    @GetMapping("/options")
    @Log(value = "根据用户名获取可登录的租户列表）", module = LogModuleEnum.USER)
    public Result<List<TenantVO>> getAccessibleTenants() {
        Long userId = SecurityUtils.getUserId();
        List<TenantVO> tenantList = tenantService.getAccessibleTenants(userId);
        log.debug("用户 {} 可访问 {} 个租户", userId, tenantList.size());
        return Result.success(tenantList);
    }

    /**
     * 获取当前租户信息
     *
     * @return 当前租户信息
     */
    @Operation(summary = "获取当前租户信息")
    @GetMapping("/current")
    public Result<TenantVO> getCurrentTenant() {
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            return Result.success(null);
        }

        TenantVO tenant = tenantService.getTenantById(tenantId);
        return Result.success(tenant);
    }

    @Operation(summary = "租户分页列表")
    @GetMapping
    @PreAuthorize("@ss.hasPerm('sys:tenant:list')")
    public PageResult<TenantPageVO> getTenantPage(TenantQuery queryParams) {
        return PageResult.success(tenantService.getTenantPage(queryParams));
    }

    @Operation(summary = "获取租户表单数据")
    @GetMapping("/{tenantId}/form")
    @PreAuthorize("@ss.hasPerm('sys:tenant:update')")
    public Result<TenantForm> getTenantForm(
            @Parameter(description = "租户 ID") @PathVariable Long tenantId
    ) {
        TenantForm formData = tenantService.getTenantForm(tenantId);
        return Result.success(formData);
    }

    @Operation(summary = "新增租户并初始化默认数据")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:tenant:create')")
    public Result<TenantCreateResultVO> createTenant(@RequestBody @Valid TenantCreateForm form) {
        TenantCreateResultVO result = tenantService.createTenantWithInit(form);
        return Result.success(result);
    }

    @Operation(summary = "修改租户")
    @PutMapping("/{tenantId}")
    @PreAuthorize("@ss.hasPerm('sys:tenant:update')")
    public Result<?> updateTenant(
            @Parameter(description = "租户ID") @PathVariable Long tenantId,
            @RequestBody @Valid TenantForm formData
    ) {
        boolean result = tenantService.updateTenant(tenantId, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除租户")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:tenant:delete')")
    public Result<Void> deleteTenants(
            @Parameter(description = "租户ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        tenantService.deleteTenants(ids);
        return Result.success();
    }

    @Operation(summary = "修改租户状态")
    @PutMapping("/{tenantId}/status")
    @PreAuthorize("@ss.hasPerm('sys:tenant:change-status')")
    public Result<?> updateTenantStatus(
            @Parameter(description = "租户ID") @PathVariable Long tenantId,
            @Parameter(description = "状态(1:启用;0:禁用)") @RequestParam Integer status
    ) {
        boolean result = tenantService.updateTenantStatus(tenantId, status);
        return Result.judge(result);
    }

    @Operation(summary = "获取租户菜单ID集合")
    @GetMapping("/{tenantId}/menuIds")
    @PreAuthorize("@ss.hasPerm('sys:tenant:plan-assign')")
    public Result<List<Long>> getTenantMenuIds(
            @Parameter(description = "租户ID") @PathVariable Long tenantId
    ) {
        List<Long> menuIds = tenantService.getTenantMenuIds(tenantId);
        return Result.success(menuIds);
    }

    @Operation(summary = "更新租户菜单")
    @PutMapping("/{tenantId}/menus")
    @PreAuthorize("@ss.hasPerm('sys:tenant:plan-assign')")
    public Result<Void> updateTenantMenus(
            @Parameter(description = "租户ID") @PathVariable Long tenantId,
            @RequestBody List<Long> menuIds
    ) {
        tenantService.updateTenantMenus(tenantId, menuIds);
        return Result.success();
    }

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
    @PostMapping("/{tenantId}/switch")
    public Result<TenantVO> switchTenant(
            @Parameter(description = "租户ID") @PathVariable Long tenantId,
            HttpServletRequest request
    ) {
        Long userId = SecurityUtils.getUserId();
        Long fromTenantId = SecurityUtils.getTenantId();

        log.info("用户 {} 请求切换租户：{} -> {}", userId, fromTenantId, tenantId);

        // 验证用户是否可以访问该租户
        if (!tenantService.canAccessTenant(userId, tenantId)) {
            log.warn("用户 {} 无权访问租户 {}", userId, tenantId);
            return Result.failed("无权访问该租户");
        }

        // 验证租户是否存在且正常
        TenantVO tenant = tenantService.getTenantById(tenantId);
        if (tenant == null) {
            log.warn("用户 {} 尝试切换到不存在的租户 {}", userId, tenantId);
            return Result.failed("租户不存在");
        }
        if (tenant.getStatus() == null || tenant.getStatus() != 1) {
            log.warn("用户 {} 尝试切换到已禁用的租户 {}", userId, tenantId);
            return Result.failed("租户已禁用");
        }



        Long oldTenantId = TenantContextHolder.getTenantId();
        log.info("切换前租户上下文ID:{}", oldTenantId);

        // 设置新的租户上下文
        TenantContextHolder.setTenantId(tenantId);


        log.info("用户 {} 成功切换租户：{} -> {}", userId, fromTenantId, tenantId);

        Long currentTenantId = TenantContextHolder.getTenantId();
        log.info("当前租户上下文ID:{}", currentTenantId);

        return Result.success(tenant);
    }


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
    @GetMapping("/canAccessTenant")
    @Log(value = "检查用户是否可以访问指定租户）", module = LogModuleEnum.TENANT)
    public boolean canAccessTenant(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "租户ID") @RequestParam Long tenantId)
    {

        return tenantService.canAccessTenant(userId, tenantId);
    }


    /**
     * 检查是否具备租户切换权限
     * <p>
     * 验证是否具备租户切换权限
     * </p>
     * @return true-可切换，false-不可切换
     */
    @Operation(summary = "检查是否具备租户切换权限")
    @GetMapping("/hasTenantSwitchPermission")
    @Log(value = "检查是否具备租户切换权限）", module = LogModuleEnum.TENANT)
    public boolean hasTenantSwitchPermission()
    {
        return tenantService.hasTenantSwitchPermission();
    }

}
