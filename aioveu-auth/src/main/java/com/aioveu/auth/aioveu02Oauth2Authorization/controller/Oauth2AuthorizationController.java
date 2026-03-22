package com.aioveu.auth.aioveu02Oauth2Authorization.controller;

import com.aioveu.auth.aioveu02Oauth2Authorization.model.form.Oauth2AuthorizationForm;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.query.Oauth2AuthorizationQuery;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.vo.Oauth2AuthorizationVo;
import com.aioveu.auth.aioveu02Oauth2Authorization.service.Oauth2AuthorizationService;
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
 * @ClassName: Oauth2AuthorizationController
 * @Description TODO OAuth2授权信息，存储所有的授权记录、令牌和状态信息前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 15:40
 * @Version 1.0
 **/
@Tag(name = "OAuth2授权信息，存储所有的授权记录、令牌和状态信息接口")
@RestController
@RequestMapping("/api/v1/oauth2-authorization")
@RequiredArgsConstructor
public class Oauth2AuthorizationController {

    private final Oauth2AuthorizationService oauth2AuthorizationService;

    @Operation(summary = "OAuth2授权信息，存储所有的授权记录、令牌和状态信息分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2Authorization:oauth2-authorization:list')")
    public PageResult<Oauth2AuthorizationVo> getOauth2AuthorizationPage(Oauth2AuthorizationQuery queryParams ) {
        IPage<Oauth2AuthorizationVo> result = oauth2AuthorizationService.getOauth2AuthorizationPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增OAuth2授权信息，存储所有的授权记录、令牌和状态信息")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2Authorization:oauth2-authorization:create')")
    public Result<Void> saveOauth2Authorization(@RequestBody @Valid Oauth2AuthorizationForm formData ) {
        boolean result = oauth2AuthorizationService.saveOauth2Authorization(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取OAuth2授权信息，存储所有的授权记录、令牌和状态信息表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2Authorization:oauth2-authorization:update')")
    public Result<Oauth2AuthorizationForm> getOauth2AuthorizationForm(
            @Parameter(description = "OAuth2授权信息，存储所有的授权记录、令牌和状态信息ID") @PathVariable Long id
    ) {
        Oauth2AuthorizationForm formData = oauth2AuthorizationService.getOauth2AuthorizationFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改OAuth2授权信息，存储所有的授权记录、令牌和状态信息")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2Authorization:oauth2-authorization:update')")
    public Result<Void> updateOauth2Authorization(
            @Parameter(description = "OAuth2授权信息，存储所有的授权记录、令牌和状态信息ID") @PathVariable Long id,
            @RequestBody @Validated Oauth2AuthorizationForm formData
    ) {
        boolean result = oauth2AuthorizationService.updateOauth2Authorization(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除OAuth2授权信息，存储所有的授权记录、令牌和状态信息")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2Authorization:oauth2-authorization:delete')")
    public Result<Void> deleteOauth2Authorizations(
            @Parameter(description = "OAuth2授权信息，存储所有的授权记录、令牌和状态信息ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = oauth2AuthorizationService.deleteOauth2Authorizations(ids);
        return Result.judge(result);
    }
}
