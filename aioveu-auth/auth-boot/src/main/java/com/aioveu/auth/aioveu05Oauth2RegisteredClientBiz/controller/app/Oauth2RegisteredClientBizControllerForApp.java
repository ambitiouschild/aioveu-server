
package com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.controller.app;


import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.form.Oauth2RegisteredClientBizForm;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.query.Oauth2RegisteredClientBizQuery;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.vo.Oauth2RegisteredClientBizVo;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.service.Oauth2RegisteredClientBizService;
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
 * @ClassName: Oauth2RegisteredClientBizController
 * @Description TODO OAuth2 客户端业务状态（auth 服务本地校验用）前端控制层ForAPP
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/11 17:38
 * @Version 1.0
 **/
@Tag(name = "OAuth2 客户端业务状态（auth 服务本地校验用）接口ForAPP")
@RestController
@RequestMapping("/aioveu/api/v8/app/auth/oauth2-registered-client-biz")
@RequiredArgsConstructor
public class Oauth2RegisteredClientBizControllerForApp {

    private final Oauth2RegisteredClientBizService oauth2RegisteredClientBizService;


    @Operation(summary = "禁用客户端业务")
    @PostMapping("/disable/{clientId}")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClientBiz:oauth2-registered-client-biz:disable')")
    public Result<Void> disable(@PathVariable String clientId) {
        boolean result = oauth2RegisteredClientBizService.disableClient(clientId);
        return Result.judge(result);
    }

    @Operation(summary = "启用客户端业务")
    @PostMapping("/enable/{clientId}")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClientBiz:oauth2-registered-client-biz:enable')")
    public Result<Void> enable(@PathVariable String clientId) {
        boolean result = oauth2RegisteredClientBizService.enableClient(clientId);
        return Result.judge(result);
    }




}
