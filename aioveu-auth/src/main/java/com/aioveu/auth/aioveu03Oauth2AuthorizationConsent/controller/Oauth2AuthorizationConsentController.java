package com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.controller;

import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.form.Oauth2AuthorizationConsentForm;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.query.Oauth2AuthorizationConsentQuery;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.vo.Oauth2AuthorizationConsentVo;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.service.Oauth2AuthorizationConsentService;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
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
 * @ClassName: Oauth2AuthorizationConsentController
 * @Description TODO OAuth2授权同意，记录用户对每个客户端的授权同意情况前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:33
 * @Version 1.0
 **/
@Tag(name = "OAuth2授权同意，记录用户对每个客户端的授权同意情况接口")
@RestController
@RequestMapping("/api/v1/oauth2-authorization-consent")
@RequiredArgsConstructor
public class Oauth2AuthorizationConsentController {

    private final Oauth2AuthorizationConsentService oauth2AuthorizationConsentService;

    @Operation(summary = "OAuth2授权同意，记录用户对每个客户端的授权同意情况分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2AuthorizationConsent:oauth2-authorization-consent:list')")
    public PageResult<Oauth2AuthorizationConsentVo> getOauth2AuthorizationConsentPage(Oauth2AuthorizationConsentQuery queryParams ) {
        IPage<Oauth2AuthorizationConsentVo> result = oauth2AuthorizationConsentService.getOauth2AuthorizationConsentPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增OAuth2授权同意，记录用户对每个客户端的授权同意情况")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2AuthorizationConsent:oauth2-authorization-consent:create')")
    public Result<Void> saveOauth2AuthorizationConsent(@RequestBody @Valid Oauth2AuthorizationConsentForm formData ) {
        boolean result = oauth2AuthorizationConsentService.saveOauth2AuthorizationConsent(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取OAuth2授权同意，记录用户对每个客户端的授权同意情况表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2AuthorizationConsent:oauth2-authorization-consent:update')")
    public Result<Oauth2AuthorizationConsentForm> getOauth2AuthorizationConsentForm(
            @Parameter(description = "OAuth2授权同意，记录用户对每个客户端的授权同意情况ID") @PathVariable Long id
    ) {
        Oauth2AuthorizationConsentForm formData = oauth2AuthorizationConsentService.getOauth2AuthorizationConsentFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改OAuth2授权同意，记录用户对每个客户端的授权同意情况")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2AuthorizationConsent:oauth2-authorization-consent:update')")
    public Result<Void> updateOauth2AuthorizationConsent(
            @Parameter(description = "OAuth2授权同意，记录用户对每个客户端的授权同意情况ID") @PathVariable Long id,
            @RequestBody @Validated Oauth2AuthorizationConsentForm formData
    ) {
        boolean result = oauth2AuthorizationConsentService.updateOauth2AuthorizationConsent(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除OAuth2授权同意，记录用户对每个客户端的授权同意情况")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2AuthorizationConsent:oauth2-authorization-consent:delete')")
    public Result<Void> deleteOauth2AuthorizationConsents(
            @Parameter(description = "OAuth2授权同意，记录用户对每个客户端的授权同意情况ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = oauth2AuthorizationConsentService.deleteOauth2AuthorizationConsents(ids);
        return Result.judge(result);
    }
}
