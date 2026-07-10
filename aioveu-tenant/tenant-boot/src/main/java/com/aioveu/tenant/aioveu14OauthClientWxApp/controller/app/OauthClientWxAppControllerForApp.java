package com.aioveu.tenant.aioveu14OauthClientWxApp.controller.app;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.annotation.PublicApi;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.tenant.aioveu14OauthClientWxApp.service.OauthClientWxAppService;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: OauthClientWxAppController
 * @Description TODO OAuth2客户端与微信小程序映射前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 16:54
 * @Version 1.0
 **/

@Slf4j
@Tag(name = "OAuth2客户端与微信小程序映射接口app")
@RestController
@RequestMapping("/aioveu/api/v8/app/tenant/oauth-client-wx-app")
@RequiredArgsConstructor
public class OauthClientWxAppControllerForApp {

    private final OauthClientWxAppService oauthClientWxAppService;

    /*
    * 建议：保持使用@RequestParam
    * 接口语义：getTenantWxAppInfoByClientId是一个查询操作，不是资源获取操作
    * RESTful设计：
    * 如果是获取资源：GET /aioveu/api/v8/admin/tenant/oauth-client-wx-app/{id}
    * 如果是查询：GET /aioveu/api/v8/admin/tenant/oauth-client-wx-app?clientId={id}
    * 参数扩展性：未来可能需要其他查询条件
    * */
    @Operation(summary = "通过 clientId 获取租户和小程序信息，只认接口参数")
    @GetMapping("/getTenantWxAppInfoByClientId")  // ✅ 应该改为GET
    @Log(value = "通过 clientId 获取租户和小程序信息）", module = LogModuleEnum.TENANT)
    public Result<TenantWxAppInfo> getTenantWxAppInfoByClientId(
            @Parameter(description = "客户端ID", required = true)
            @RequestParam("clientId") String clientId) {

        log.info("【Tenant】收到 clientId = {}", clientId);
        TenantWxAppInfo tenantWxAppInfo  = oauthClientWxAppService.getTenantWxAppInfoByClientId(clientId);


        return Result.success(tenantWxAppInfo);
    }

    @PublicApi(description = "通过 clientId 获取tenantId")
    @Operation(summary = "通过 clientId 获取tenantId")
    @GetMapping("/getTenantIdByClientId")  // ✅ 应该改为GET
    @Log(value = "通过 clientId 获取 tenantId）", module = LogModuleEnum.TENANT)
    //凡是“只给 Gateway 用的接口”,❌ 不返回 Result,返回裸数据
    public Long getTenantIdByClientId(
            @Parameter(description = "客户端ID", required = true)
            @RequestParam("clientId") String clientId) {

        log.info("【Tenant】收到 clientId = {}", clientId);
        Long tenantId  = oauthClientWxAppService.getTenantIdByClientId(clientId);
        return tenantId;
    }
}
