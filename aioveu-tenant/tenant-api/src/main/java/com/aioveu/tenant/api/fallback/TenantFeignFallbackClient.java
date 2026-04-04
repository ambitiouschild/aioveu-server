package com.aioveu.tenant.api.fallback;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Override
    public List<TenantVO> getAccessibleTenantsByUsername(String username) {
        log.error("根据用户名获取可登录的租户列表失败");
        return null;
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
    @Override
    public Result<TenantVO> switchTenant(
            @Parameter(description = "租户ID") @PathVariable Long tenantId,
            HttpServletRequest request
    ) {
        log.error("切换租户失败");
        return null;
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
    @Override
    public boolean canAccessTenant(Long userId, Long tenantId){
        log.error("检查用户是否可以访问指定租户失败");
        return false;
    }

    /**
     * 检查是否具备租户切换权限
     * <p>
     * 验证是否具备租户切换权限
     * </p>
     * @return true-可切换，false-不可切换
     */
    @Override
    public Result<Boolean> hasTenantSwitchPermission(){
        log.error("检查是否具备租户切换权限失败");
        return Result.failed();
    }

    /**
     * 通过 clientId 获取租户和小程序信息
     */
    @Override
    public TenantWxAppInfo getTenantWxAppInfoByClientId(String  clientId){
        log.error("通过 clientId 获取租户和小程序信息失败");
        return null;
    }

    /**
     * 通过 clientId 获取租户和小程序信息
     */
    @Override
    public TenantWxAppInfo getTenantWxAppInfoByTenantId(Long  tenantId){
        log.error("通过 tenantId 获取租户和小程序信息失败");
        return null;
    }

    /**
     * 获取用户的工作台菜单（包含分类和菜单项）
     */

    @Override
    public List<ManagerMenuCategoryWithItemsVO> getWorkbenchCategoriesWithItems(
            @RequestHeader("X-Tenant-Id") Long tenantId
    ) {
        log.error("获取用户的工作台菜单（包含分类和菜单项）");
        return null;
    }

    /**
     * 根据tenantId查询对应的管理端首页分类数据
     */

    @Override
    public List<ManagerMenuHomeCategoryVo> getManagerMenuHomeCategoryList(
            @RequestHeader("X-Tenant-Id") Long tenantId
    ) {
        log.error("根据tenantId查询对应的管理端首页分类数据,失败");
        return null;

    }

}
