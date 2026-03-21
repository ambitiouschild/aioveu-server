package com.aioveu.tenant.aioveu14OauthClientWxApp.controller;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.form.OauthClientWxAppForm;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.query.OauthClientWxAppQuery;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.vo.OauthClientWxAppVo;
import com.aioveu.tenant.aioveu14OauthClientWxApp.service.OauthClientWxAppService;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.vo.TenantWxAppInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: OauthClientWxAppController
 * @Description TODO OAuth2客户端与微信小程序映射前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 16:54
 * @Version 1.0
 **/
@Tag(name = "OAuth2客户端与微信小程序映射接口")
@RestController
@RequestMapping("/api/v1/oauth-client-wx-app")
@RequiredArgsConstructor
public class OauthClientWxAppController {

    private final OauthClientWxAppService oauthClientWxAppService;

    @Operation(summary = "OAuth2客户端与微信小程序映射分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallTenantOauthClientWxApp:oauth-client-wx-app:list')")
    public PageResult<OauthClientWxAppVo> getOauthClientWxAppPage(OauthClientWxAppQuery queryParams ) {
        IPage<OauthClientWxAppVo> result = oauthClientWxAppService.getOauthClientWxAppPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增OAuth2客户端与微信小程序映射")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallTenantOauthClientWxApp:oauth-client-wx-app:create')")
    public Result<Void> saveOauthClientWxApp(@RequestBody @Valid OauthClientWxAppForm formData ) {
        boolean result = oauthClientWxAppService.saveOauthClientWxApp(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取OAuth2客户端与微信小程序映射表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallTenantOauthClientWxApp:oauth-client-wx-app:update')")
    public Result<OauthClientWxAppForm> getOauthClientWxAppForm(
            @Parameter(description = "OAuth2客户端与微信小程序映射ID") @PathVariable Long id
    ) {
        OauthClientWxAppForm formData = oauthClientWxAppService.getOauthClientWxAppFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改OAuth2客户端与微信小程序映射")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallTenantOauthClientWxApp:oauth-client-wx-app:update')")
    public Result<Void> updateOauthClientWxApp(
            @Parameter(description = "OAuth2客户端与微信小程序映射ID") @PathVariable Long id,
            @RequestBody @Validated OauthClientWxAppForm formData
    ) {
        boolean result = oauthClientWxAppService.updateOauthClientWxApp(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除OAuth2客户端与微信小程序映射")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallTenantOauthClientWxApp:oauth-client-wx-app:delete')")
    public Result<Void> deleteOauthClientWxApps(
            @Parameter(description = "OAuth2客户端与微信小程序映射ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = oauthClientWxAppService.deleteOauthClientWxApps(ids);
        return Result.judge(result);
    }


    /*
    * 建议：保持使用@RequestParam
    * 接口语义：getTenantWxAppInfoByClientId是一个查询操作，不是资源获取操作
    * RESTful设计：
    * 如果是获取资源：GET /api/v1/oauth-client-wx-app/{id}
    * 如果是查询：GET /api/v1/oauth-client-wx-app?clientId={id}
    * 参数扩展性：未来可能需要其他查询条件
    * */
    @Operation(summary = "通过 clientId 获取租户和小程序信息")
    @GetMapping("/getTenantWxAppInfoByClientId")  // ✅ 应该改为GET
    @Log(value = "通过 clientId 获取租户和小程序信息）", module = LogModuleEnum.TENANT)
    public TenantWxAppInfo getTenantWxAppInfoByClientId(
            @Parameter(description = "clientId") @RequestParam("clientId") String clientId
    ) {
        TenantWxAppInfo tenantWxAppInfo  = oauthClientWxAppService.getTenantWxAppInfoByClientId(clientId);
        return tenantWxAppInfo;
    }
}
