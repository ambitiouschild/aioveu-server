package com.aioveu.auth.controller;

import com.aioveu.auth.model.CaptchaInfo;
import com.aioveu.auth.model.CaptchaResult;
import com.aioveu.auth.model.WxMiniAppCodeLoginDTO;
import com.aioveu.auth.model.WxMiniAppPhoneLoginDTO;
import com.aioveu.auth.service.AuthService;
import com.aioveu.auth.service.impl.AuthServiceImpl;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.annotation.Log;
import com.aioveu.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.aioveu.common.security.model.AuthenticationToken;

/**
 * @Description: TODO 认证控制器 获取验证码、退出登录等接口
 * 注：登录接口不在此控制器，在过滤器OAuth2TokenEndpointFilter拦截端点(/oauth2/token)处理
 * @Author: 雒世松
 * @Date: 2025/6/5 17:43
 * @param
 * @return:
 **/

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authServiceImpl;
    private final AuthService authService;

    @Operation(summary = "获取验证码old")
    @GetMapping("/captcha2")
    public Result<CaptchaResult> getCaptcha2() {
        CaptchaResult captchaResult = authServiceImpl.getCaptcha2();
        return Result.success(captchaResult);
    }

    @Operation(summary = "获取登录验证码")
    @GetMapping("/captcha")
    public Result<CaptchaInfo> getCaptcha() {
        CaptchaInfo captcha = authService.getCaptcha();
        return Result.success(captcha);
    }

    @Operation(summary = "账号密码登录")
    @PostMapping("/login")
    @Log(value = "登录", module = LogModuleEnum.LOGIN)
    public Result<AuthenticationToken> login(
            @Parameter(description = "用户名", example = "aioveu") @RequestParam String username,
            @Parameter(description = "密码", example = "aioveu") @RequestParam String password
    ) {
        AuthenticationToken authenticationToken = authServiceImpl.login(username, password);
        return Result.success(authenticationToken);
    }

    @Operation(summary = "注销登录")
    @DeleteMapping("/logout")
    @Log(value = "注销", module = LogModuleEnum.LOGIN)
    public Result<?> logout() {
        authService.logout();
        return Result.success();
    }


    @Operation(summary = "刷新访问令牌")
    @PostMapping("/refresh-token")
    public Result<?> refreshToken(
            @Parameter(description = "刷新令牌", example = "xxx.xxx.xxx") @RequestParam String refreshToken
    ) {
        AuthenticationToken authenticationToken = authService.refreshToken(refreshToken);
        return Result.success(authenticationToken);
    }


    @Operation(summary = "微信授权登录")
    @PostMapping("/login/wechat")
    @Log(value = "微信登录", module = LogModuleEnum.LOGIN)
    public Result<AuthenticationToken> loginByWechat(
            @Parameter(description = "微信授权码", example = "code") @RequestParam String code
    ) {
        AuthenticationToken loginResult = authService.loginByWechat(code);
        return Result.success(loginResult);
    }


    @Operation(summary = "发送登录短信验证码")
    @PostMapping("/login/sms/code")
    public Result<Void> sendLoginVerifyCode(
            @Parameter(description = "手机号", example = "13061656199") @RequestParam String mobile
    ) {
        authService.sendSmsLoginCode(mobile);
        return Result.success();
    }


    @Operation(summary = "短信验证码登录")
    @PostMapping("/login/sms")
    @Log(value = "短信验证码登录", module = LogModuleEnum.LOGIN)
    public Result<AuthenticationToken> loginBySms(
            @Parameter(description = "手机号", example = "13061656199") @RequestParam String mobile,
            @Parameter(description = "验证码", example = "1234") @RequestParam String code
    ) {
        AuthenticationToken loginResult = authService.loginBySms(mobile, code);
        return Result.success(loginResult);
    }


    @Operation(summary = "微信小程序Code登录")
    @PostMapping("/wx/miniapp/code-login")
    public Result<AuthenticationToken> loginByWxMiniAppCode(@RequestBody @Valid WxMiniAppCodeLoginDTO loginDTO) {
        AuthenticationToken token = authService.loginByWxMiniAppCode(loginDTO);
        return Result.success(token);
    }

    @Operation(summary = "微信小程序手机号登录")
    @PostMapping("/wx/miniapp/phone-login")
    public Result<AuthenticationToken> loginByWxMiniAppPhone(@RequestBody @Valid WxMiniAppPhoneLoginDTO loginDTO) {
        AuthenticationToken token = authService.loginByWxMiniAppPhone(loginDTO);
        return Result.success(token);
    }

    @Operation(summary = "发送手机短信验证码old")
    @PostMapping("/sms_code")
    public Result sendLoginSmsCode(
            @Parameter(description = "手机号") @RequestParam String mobile
    ) {
        boolean result = authServiceImpl.sendLoginSmsCode(mobile);
        return Result.judge(result);
    }



}
