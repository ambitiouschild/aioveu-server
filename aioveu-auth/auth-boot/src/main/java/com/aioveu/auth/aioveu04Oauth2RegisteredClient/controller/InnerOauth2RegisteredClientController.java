package com.aioveu.auth.aioveu04Oauth2RegisteredClient.controller;


import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.form.Oauth2RegisteredClientForm;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.service.Oauth2RegisteredClientService;
import com.aioveu.auth.model.TenantClientInitDTO;
import com.aioveu.common.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @ClassName: InnerOauth2RegisteredClientController
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/5 14:14
 * @Version 1.0
 **/
@Slf4j
@Tag(name = "一个内部接口（不暴露给前端）", description = "OAuth2客户端注册和管理接口")
@RestController
@RequestMapping("/inner")
@RequiredArgsConstructor
public class InnerOauth2RegisteredClientController {

    private final Oauth2RegisteredClientService oauth2RegisteredClientService;

    @PostMapping("/clients/init-by-tenant")
    public Result<Void> initClientByTenant(@RequestBody TenantClientInitDTO dto) {

        boolean result =   oauth2RegisteredClientService.initClientByTenant(dto);
        return Result.judge(result);
    }


}
