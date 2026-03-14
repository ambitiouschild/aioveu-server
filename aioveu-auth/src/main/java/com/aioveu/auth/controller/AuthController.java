package com.aioveu.auth.controller;

import com.aioveu.auth.model.CaptchaResult;
import com.aioveu.auth.model.SysUserDetails;
import com.aioveu.auth.service.AuthService;
import com.aioveu.auth.util.SecurityUtils;
import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.common.result.ResultCode;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.TenantVO;
import com.aioveu.tenant.dto.UserAuthInfoWithTenantId;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Description: TODO 认证控制器 获取验证码、退出登录等接口
 * 注：登录接口不在此控制器，在过滤器OAuth2TokenEndpointFilter拦截端点(/oauth2/token)处理
 * @Author: 雒世松
 * @Date: 2025/6/5 17:43
 * @param
 * @return:
 **/
@Slf4j
@RestController  // 标识该类为RESTful Web服务控制器，所有方法返回数据直接写入HTTP响应体
@Tag(name = "01.认证中心")
@RequestMapping("/api/v1/auth")   // 定义控制器的基础请求映射路径，所有接口都以/api/v1/auth开头
@RequiredArgsConstructor   // Lombok注解，为所有final字段生成构造函数，实现依赖注入
public class AuthController {


    DefaultOAuth2TokenContext.Builder tokenContextBuilder;

    OAuth2TokenGenerator<?> tokenGenerator;            // 令牌生成器

    // 注入认证服务层实例，用于处理业务逻辑
    private final AuthService authService;

    private final TenantFeignClient tenantFeignClient;
    /**
     * 生成图形验证码接口
     * 用于用户登录或注册时的安全验证，防止机器人恶意请求
     *
     * @return Result<CaptchaResult> 统一响应结果，包含验证码数据
     *         - 验证码图片（Base64编码或图片流）
     *         - 验证码唯一标识（用于后续验证）
     *         - 过期时间等信息
     *
     * 接口路径：GET /api/v1/auth/captcha
     * 适用场景：用户登录前获取验证码图片
     */
    @Operation(summary = "获取验证码")  // Swagger注解，在API文档中显示接口摘要描述
    @GetMapping("/captcha")  // 处理HTTP GET请求，完整路径为/api/v1/auth/captcha
    @Log(value = "获取验证码", module = LogModuleEnum.AUTH)
    public Result<CaptchaResult> getCaptcha() {

        // 调用服务层生成验证码，包含图片和验证码信息
        CaptchaResult captchaResult = authService.getCaptcha();

        // 将结果包装成统一响应格式返回
        return Result.success(captchaResult);
    }


    /**
     * 发送手机短信验证码接口
     * 用于手机号登录或注册时发送验证码短信
     *
     * @param mobile 用户手机号码，作为请求参数传递
     *               - 格式要求：11位手机号
     *               - 必填：是
     *               - 示例：13800138000
     *
     * @return Result 统一响应结果，只包含成功或失败状态
     *         - 成功：返回操作成功状态
     *         - 失败：返回具体错误信息
     *
     * 接口路径：POST /api/v1/auth/sms_code
     * 适用场景：用户选择手机号登录时获取短信验证码
     *
     * 安全考虑：通常需要限制发送频率，防止短信轰炸攻击
     */
    @Operation(summary = "发送手机短信验证码")  // Swagger注解，描述接口功能
    @PostMapping("/sms_code")   // 处理HTTP POST请求，完整路径为/api/v1/auth/sms_code
    @Log(value = "发送手机短信验证码", module = LogModuleEnum.AUTH)
    public Result sendLoginSmsCode(
            @Parameter(description = "手机号")   // Swagger参数描述，在API文档中显示参数说明
            @RequestParam String mobile         // 从请求参数中获取手机号，参数名为mobile
    ) {

        // 调用服务层发送短信验证码，返回发送结果（成功/失败）
        boolean result = authService.sendLoginSmsCode(mobile);

        // 根据布尔结果返回对应的响应（成功或失败）
        return Result.judge(result);
    }

    @Operation(summary = "切换租户")
    @PostMapping("/switch-tenant")
    public Result<Map<String, Object>> switchTenant(@RequestParam Long tenantId) {

        try {
                // 1. 权限校验

        //        if (!tenantService.hasTenantSwitchPermission()) {
        //            return Result.failed("无权限");
        //        }

                if (!SecurityUtils.canSwitchTenant()) {
                    return Result.failed("无租户切换权限");
                }

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !(authentication.getPrincipal() instanceof SysUserDetails details)) {
                    return Result.failed(ResultCode.ACCESS_TOKEN_INVALID);
                }

                // 3. 校验用户是否能访问该租户
                boolean canAccess = tenantFeignClient.canAccessTenant(details.getUserId(), tenantId);
                if (!canAccess) {
                    return Result.failed("无权限访问该租户");
                }

                // 获取用户在新租户下的权限信息（可选，如果需要更新权限）
        //        UserAuthInfoWithTenantId userAuthInfoWithTenantId = tenantFeignClient.getUserAuthInfoWithTenantId
        //                (details.getUsername(), tenantId);


                // ✅ 3. 最简单的方法：不生成新Token，返回原Token + 新租户ID
                // 前端存储两个东西：Token + 当前租户ID
                String currentToken = SecurityUtils.getToken();

                if (currentToken == null) {
                    return Result.failed("无法获取当前Token");
                }
                // 6. 返回结果
                Map<String, Object> result = new HashMap<>();
                result.put("access_token", currentToken);      // 原Token不变
                result.put("token_type", "Bearer");
                result.put("tenant_id", tenantId);             // 新租户ID
                result.put("user_id", details.getUserId());
                result.put("username", details.getUsername());
                result.put("success", true);
                result.put("timestamp", System.currentTimeMillis());

                log.info("用户 {} 切换到租户 {} 成功", details.getUsername(), tenantId);

                return Result.success(result);

        } catch (Exception e) {
            log.error("切换租户失败", e);
            return Result.failed("切换失败: " + e.getMessage());
        }

    }

    /**
     * 获取当前用户的租户列表
     * <p>
     * 根据当前登录用户查询其所属的所有租户
     * </p>
     *
     * @return 租户列表
     */
    @Operation(summary = "新增:根据用户名获取可登录的租户列表")
    @GetMapping("/tenants/{username}")
    @Log(value = "新增：根据用户名获取可登录的租户列表）", module = LogModuleEnum.USER)
    public Result<List<TenantVO>> getAccessibleTenantsByUsername(
            @Parameter(description = "用户名") @PathVariable String username
    ) {
        log.info("调用tenantFeignClient微服务一次查询获取用户名在所有租户中的可访问租户");
        List<TenantVO> tenantList= tenantFeignClient.getAccessibleTenantsByUsername(username);
        log.info("一次查询获取用户名在所有租户中的可访问租户tenantList:{}",tenantList);
        return Result.success(tenantList);
    }


    @Operation(summary = "退出登录")
    @DeleteMapping("/logout")
    @Log(value = "退出登录", module = LogModuleEnum.LOGIN)
    public Result<?> logout() {
        authService.logout();
        log.info("【退出登录】退出登录成功");
        return Result.success();
    }




}
