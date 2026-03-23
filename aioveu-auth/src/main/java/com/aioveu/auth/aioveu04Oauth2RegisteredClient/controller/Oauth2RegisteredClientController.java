package com.aioveu.auth.aioveu04Oauth2RegisteredClient.controller;

import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.form.Oauth2RegisteredClientForm;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.query.Oauth2RegisteredClientQuery;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.vo.Oauth2RegisteredClientVo;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.service.Oauth2RegisteredClientService;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: Oauth2RegisteredClientController
 * @Description TODO OAuth2注册客户端，存储所有已注册的客户端应用信息前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 15:22
 * @Version 1.0
 **/

@Slf4j
@Tag(name = "OAuth2注册客户端，存储所有已注册的客户端应用信息接口", description = "OAuth2客户端注册和管理接口")
@RestController
@RequestMapping("/api/v1/oauth2-registered-client")
@RequiredArgsConstructor
public class Oauth2RegisteredClientController {

    private final Oauth2RegisteredClientService oauth2RegisteredClientService;

    @Operation(summary = "OAuth2注册客户端，存储所有已注册的客户端应用信息分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClient:oauth2-registered-client:list')")
    public PageResult<Oauth2RegisteredClientVo> getOauth2RegisteredClientPage(Oauth2RegisteredClientQuery queryParams ) {
        IPage<Oauth2RegisteredClientVo> result = oauth2RegisteredClientService.getOauth2RegisteredClientPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增OAuth2注册客户端，存储所有已注册的客户端应用信息")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClient:oauth2-registered-client:create')")
    public Result<Void> saveOauth2RegisteredClient(@RequestBody @Valid Oauth2RegisteredClientForm formData ) {

        log.info("注册新客户端: clientId={}", formData.getClientId());
        boolean result = oauth2RegisteredClientService.saveOauth2RegisteredClient(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取OAuth2注册客户端，存储所有已注册的客户端应用信息表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClient:oauth2-registered-client:update')")
    public Result<Oauth2RegisteredClientForm> getOauth2RegisteredClientForm(
            @Parameter(description = "OAuth2注册客户端，存储所有已注册的客户端应用信息ID") @PathVariable Long id
    ) {
        Oauth2RegisteredClientForm formData = oauth2RegisteredClientService.getOauth2RegisteredClientFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改OAuth2注册客户端，存储所有已注册的客户端应用信息")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClient:oauth2-registered-client:update')")
    public Result<Void> updateOauth2RegisteredClient(
            @Parameter(description = "OAuth2注册客户端，存储所有已注册的客户端应用信息ID") @PathVariable String clientId,
            @RequestBody @Validated Oauth2RegisteredClientForm formData
    ) {
        boolean result = oauth2RegisteredClientService.updateOauth2RegisteredClient(clientId, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除OAuth2注册客户端，存储所有已注册的客户端应用信息")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClient:oauth2-registered-client:delete')")
    public Result<Void> deleteOauth2RegisteredClients(
            @Parameter(description = "OAuth2注册客户端，存储所有已注册的客户端应用信息ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = oauth2RegisteredClientService.deleteOauth2RegisteredClients(ids);
        return Result.judge(result);
    }

    /**
     * 根据ID获取客户端
     */
    @Operation(summary = "根据ID获取客户端")
    @GetMapping("/{clientId}")
    public Result<Oauth2RegisteredClientVo> getClient(
            @PathVariable @Parameter(description = "客户端ID") String clientId) {

        Oauth2RegisteredClientVo client = oauth2RegisteredClientService.getClient(clientId);
        return Result.success(client);
    }

    /**
     * 重置客户端密钥
     */
    @PostMapping("/{clientId}/reset-secret")
    @Operation(summary = "重置客户端密钥")
    public Result<Oauth2RegisteredClientVo> resetClientSecret(
            @PathVariable @Parameter(description = "客户端ID") String clientId) {

        log.info("重置客户端密钥: clientId={}", clientId);
        Oauth2RegisteredClientVo response = oauth2RegisteredClientService.resetClientSecret(clientId);
        return Result.success(response);
    }

    /**
     * 启用/禁用客户端
     */
    @PostMapping("/{clientId}/toggle-status")
    @Operation(summary = "启用/禁用客户端")
    public Result<Void> toggleClientStatus(
            @PathVariable @Parameter(description = "客户端ID") String clientId,
            @RequestParam @Parameter(description = "是否启用") Boolean enabled) {

        log.info("修改客户端状态: clientId={}, enabled={}", clientId, enabled);
        oauth2RegisteredClientService.toggleClientStatus(clientId, enabled);
        return Result.success();
    }
}
