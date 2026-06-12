package com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.controller.admin;


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
 * @Description TODO OAuth2 客户端业务状态（auth 服务本地校验用）前端控制层
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/11 17:38
 * @Version 1.0
 **/
@Tag(name = "OAuth2 客户端业务状态（auth 服务本地校验用）接口")
@RestController
@RequestMapping("/api/v1/oauth2-registered-client-biz")
@RequiredArgsConstructor
public class Oauth2RegisteredClientBizController {

    private final Oauth2RegisteredClientBizService oauth2RegisteredClientBizService;

    @Operation(summary = "OAuth2 客户端业务状态（auth 服务本地校验用）分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClientBiz:oauth2-registered-client-biz:list')")
    public PageResult<Oauth2RegisteredClientBizVo> getOauth2RegisteredClientBizPage(Oauth2RegisteredClientBizQuery queryParams ) {
        IPage<Oauth2RegisteredClientBizVo> result = oauth2RegisteredClientBizService.getOauth2RegisteredClientBizPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增OAuth2 客户端业务状态（auth 服务本地校验用）")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClientBiz:oauth2-registered-client-biz:create')")
    public Result<Void> saveOauth2RegisteredClientBiz(@RequestBody @Valid Oauth2RegisteredClientBizForm formData ) {
        boolean result = oauth2RegisteredClientBizService.saveOauth2RegisteredClientBiz(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取OAuth2 客户端业务状态（auth 服务本地校验用）表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClientBiz:oauth2-registered-client-biz:update')")
    public Result<Oauth2RegisteredClientBizForm> getOauth2RegisteredClientBizForm(
            @Parameter(description = "OAuth2 客户端业务状态（auth 服务本地校验用）ID") @PathVariable Long id
    ) {
        Oauth2RegisteredClientBizForm formData = oauth2RegisteredClientBizService.getOauth2RegisteredClientBizFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改OAuth2 客户端业务状态（auth 服务本地校验用）")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClientBiz:oauth2-registered-client-biz:update')")
    public Result<Void> updateOauth2RegisteredClientBiz(
            @Parameter(description = "OAuth2 客户端业务状态（auth 服务本地校验用）ID") @PathVariable Long id,
            @RequestBody @Validated Oauth2RegisteredClientBizForm formData
    ) {
        boolean result = oauth2RegisteredClientBizService.updateOauth2RegisteredClientBiz(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除OAuth2 客户端业务状态（auth 服务本地校验用）")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallAuthOauth2RegisteredClientBiz:oauth2-registered-client-biz:delete')")
    public Result<Void> deleteOauth2RegisteredClientBizs(
            @Parameter(description = "OAuth2 客户端业务状态（auth 服务本地校验用）ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = oauth2RegisteredClientBizService.deleteOauth2RegisteredClientBizs(ids);
        return Result.judge(result);
    }
}
