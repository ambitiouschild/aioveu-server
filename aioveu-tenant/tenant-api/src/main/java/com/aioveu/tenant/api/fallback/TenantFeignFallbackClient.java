package com.aioveu.tenant.api.fallback;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
public class TenantFeignFallbackClient implements FallbackFactory<TenantFeignClient> {


    @Override
    public TenantFeignClient create(Throwable cause) {

        return new TenantFeignClient() {

            @Override
            public TenantWxAppInfo getTenantWxAppInfoByClientId(String clientId) {
                log.error("Feign fallback: getTenantWxAppInfoByClientId, clientId={}", clientId, cause);
                log.error("通过 clientId 获取租户和小程序信息失败");
                return new TenantWxAppInfo(); // ✅ 绝不能 return null
            }

            @Override
            public TenantWxAppInfo getTenantWxAppInfoByTenantId(Long tenantId) {
                log.error("Feign fallback: getTenantWxAppInfoByTenantId, tenantId={}", tenantId, cause);
                log.error("通过 clientId 获取租户和小程序信息失败");
                return new TenantWxAppInfo();
            }

            @Override
            public Long getTenantIdByClientId(String clientId) {
                log.error("Feign fallback: getTenantIdByClientId, tenantId={}", clientId, cause);
                log.error("通过 clientId 获取tenantId失败");
                return null;
            }

            @Override
            public UserAuthInfoWithTenantId getUserAuthInfoWithTenantId(String username, Long tenantId) {
                log.error("Feign fallback: getUserAuthInfoWithTenantId", cause);
                log.error("feign远程调用多租户服务异常后的降级方法");
                return new UserAuthInfoWithTenantId();
            }

            @Override
            public List<TenantVO> getAccessibleTenantsByUsername(String username) {
                log.error("Feign fallback: getAccessibleTenantsByUsername", cause);
                log.error("根据用户名获取可登录的租户列表失败");
                return Collections.emptyList();
            }

            @Override
            public Result<TenantVO> switchTenant(Long tenantId) {
                log.error("Feign fallback: switchTenant", cause);
                log.error("切换租户失败");
                return Result.failed("租户切换失败");
            }

            @Override
            public boolean canAccessTenant(Long userId, Long tenantId) {
                log.error("Feign fallback: canAccessTenant", cause);
                log.error("检查用户是否可以访问指定租户失败");
                return false;
            }

            @Override
            public Result<Boolean> hasTenantSwitchPermission() {
                log.error("Feign fallback: hasTenantSwitchPermission", cause);
                log.error("检查是否具备租户切换权限失败");
                return Result.failed("无租户切换权限");
            }

            @Override
            public List<ManagerMenuCategoryWithItemsVO> getWorkbenchCategoriesWithItems(Long tenantId) {
                log.error("Feign fallback: getWorkbenchCategoriesWithItems", cause);
                log.error("获取用户的工作台菜单（包含分类和菜单项）失败");
                return Collections.emptyList();
            }

            @Override
            public List<ManagerMenuHomeCategoryVo> getManagerMenuHomeCategoryList(Long tenantId) {
                log.error("Feign fallback: getManagerMenuHomeCategoryList", cause);
                log.error("根据tenantId查询对应的管理端首页分类数据,失败");
                return Collections.emptyList();
            }

            @Override
            public List<ManagerMenuHomeBannerVo> getManagerMenuHomeBanners(Long tenantId) {
                log.error("Feign fallback: getManagerMenuHomeBanners", cause);
                log.error("根据tenantId查询对应的管理端首页banners数据,失败");
                return Collections.emptyList();
            }
        };
    }

}
