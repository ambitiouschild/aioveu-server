package com.aioveu.auth.controller.app;

import com.aioveu.auth.TokenManager.service.AuthTokenManagerService;
import com.aioveu.auth.model.CaptchaResult;
import com.aioveu.auth.service.AuthService;
import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pms.api.PmsFeignClient;
import com.aioveu.pms.api.PmsFeignClientWithoutConfig;
import com.aioveu.pms.model.query.PmsSpuQuery;
import com.aioveu.pms.model.vo.CategoryVO;
import com.aioveu.pms.model.vo.SeckillingSpuVO;
import com.aioveu.pms.model.vo.SpuDetailVO;
import com.aioveu.pms.model.vo.SpuPageVO;
import com.aioveu.sms.api.app.SmsFeignClient;
import com.aioveu.sms.dto.BannerVO;
import com.aioveu.sms.dto.SmsHomeAdvertVO;
import com.aioveu.sms.dto.SmsHomeCategoryVO;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.ManagerMenuCategoryWithItemsVO;
import com.aioveu.tenant.dto.ManagerMenuHomeCategoryVo;
import com.aioveu.tenant.dto.TenantVO;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RequestMapping("/app-api/v1/auth")   // 定义控制器的基础请求映射路径，所有接口都以/api/v1/auth开头
@RequiredArgsConstructor   // Lombok注解，为所有final字段生成构造函数，实现依赖注入
public class AuthForAppController {




    private final AuthTokenManagerService authTokenManagerService;// 令牌生成器

    // 注入认证服务层实例，用于处理业务逻辑
    private final AuthService authService;

    private final TenantFeignClient tenantFeignClient;

    private final SmsFeignClient smsFeignClient;

    private final PmsFeignClient pmsFeignClient;

    private final PmsFeignClientWithoutConfig pmsFeignClientWithoutConfig;
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
    public Result<Authentication> switchTenant(@RequestParam Long tenantId) {

        log.info("【Auth】切换租户");

        return authService.switchTenant(tenantId);
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

    /**
     * 获取Banners轮播图（公共接口）
     * GET /api/public/banners?clientId=mall-app
     */
    @GetMapping("/banners")
    public Result<List<BannerVO>> getHomeBanners(
            @RequestParam String clientId) {

        log.info("【auth-app-banners】前端传递的客户端clientId:{}",clientId);

        // 1. 通过clientId获取tenantId
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("【auth-app-banners】通过clientId获取tenantWxAppInfo:{}",tenantWxAppInfo);

        Long tenantId = tenantWxAppInfo.getTenantId();

        log.info("【auth-app-banners】通过clientId获取tenantId:{}",tenantId);

        List<BannerVO> banners = smsFeignClient.getSmsHomeBannersList(tenantId);
        log.info("【auth-app-banners】根据tenantI过滤对应的banners数据:{}",banners);
        return Result.success(banners);
    }

    /**
     * 获取首页分类（公共接口）
     * GET /api/public/categories?clientId=mall-app
     */
    @GetMapping("/categories")
    public Result<List<SmsHomeCategoryVO>> getHomeCategories(
            @RequestParam String clientId) {


        log.info("【auth-app-categories】前端传递的客户端clientId:{}",clientId);
        // 1. 通过clientId获取tenantId
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("【auth-app-categories】通过clientId获取tenantWxAppInfo:{}",tenantWxAppInfo);

         Long tenantId = tenantWxAppInfo.getTenantId();

        log.info("【auth-app-categories】通过clientId获取tenantId:{}",tenantId);

        // 2. 根据tenantId查询对应的分类数据
        List<SmsHomeCategoryVO>  categories = smsFeignClient.getSmsHomeCategoryList(tenantId);

        log.info("【auth-app-categories】根据tenantI过滤对应的分类数据:{}",categories);

        return Result.success(categories);
    }

    /**
     * 获取广告轮播图（公共接口）
     * GET /api/public/banners?clientId=mall-app
     */
    @GetMapping("/adverts")
    public Result<List<SmsHomeAdvertVO>> getHomeAdverts(
            @RequestParam String clientId) {

        log.info("【auth-app-adverts】前端传递的客户端clientId:{}",clientId);

        // 1. 通过clientId获取tenantId
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("【auth-app-adverts】通过clientId获取tenantWxAppInfo:{}",tenantWxAppInfo);

        Long tenantId = tenantWxAppInfo.getTenantId();

        log.info("【auth-app-adverts】通过clientId获取tenantId:{}",tenantId);

        List<SmsHomeAdvertVO> adverts = smsFeignClient.getSmsHomeAdvertList(tenantId);
        log.info("【auth-app-adverts】根据tenantI过滤对应的广告数据:{}",adverts);
        return Result.success(adverts);
    }


    /**
     * 获取秒杀商品列表
     * GET /api/public/banners?clientId=mall-app
     */
    @GetMapping("/seckilling")
    public Result<List<SeckillingSpuVO>> getHomeSeckilling(
            @RequestParam String clientId) {

        log.info("【auth-app-Seckilling】前端传递的客户端clientId:{}",clientId);

        // 1. 通过clientId获取tenantId
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("【auth-app-Seckilling】通过clientId获取tenantWxAppInfo:{}",tenantWxAppInfo);

        Long tenantId = tenantWxAppInfo.getTenantId();

        log.info("【auth-app-Seckilling】通过clientId获取tenantId:{}",tenantId);

        List<SeckillingSpuVO> seckillings = pmsFeignClient.listSeckillingSpu(tenantId);
        log.info("【auth-app-Seckilling】根据tenantI过滤获取秒杀商品列表:{}",seckillings);
        return Result.success(seckillings);
    }

    /**
     * 获取商品分类
     * GET /api/public/banners?clientId=mall-app
     */
    @GetMapping("/goodsCategories")
    public Result<List<CategoryVO>> getGoods(
            @RequestParam String clientId) {

        log.info("【auth-app-goodsCategories】前端传递的客户端clientId:{}",clientId);

        // 1. 通过clientId获取tenantId
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("【auth-app-goodsCategories】通过clientId获取tenantWxAppInfo:{}",tenantWxAppInfo);

        Long tenantId = tenantWxAppInfo.getTenantId();

        log.info("【auth-app-goodsCategories】通过clientId获取tenantId:{}",tenantId);

        List<CategoryVO> goodsCategories = pmsFeignClient.list(null, tenantId);
        log.info("【auth-app-goodsCategories】根据tenantI过滤获取商品:{}",goodsCategories);
        return Result.success(goodsCategories);
    }

    /**
     * 获取商品列表
     * GET /api/public/banners?clientId=mall-app
     */
    @GetMapping("/spuLists")
    public PageResult<SpuPageVO> getSpuLists(
            PmsSpuQuery queryParams,
            @RequestParam String clientId) {

        log.info("【auth-app-spuLists】前端传递的客户端clientId:{}",clientId);

        // 1. 通过clientId获取tenantId
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("【auth-app-spuLists】通过clientId获取tenantWxAppInfo:{}",tenantWxAppInfo);

        Long tenantId = tenantWxAppInfo.getTenantId();

        log.info("【auth-app-spuLists】通过clientId获取tenantId:{}",tenantId);

        PageResult<SpuPageVO> spuLists = pmsFeignClientWithoutConfig.listPagedSpuForApp(queryParams, tenantId);
        log.info("【auth-app-spuLists】根据tenantI过滤获取商品:{}",spuLists);
        return spuLists;
    }

    /**
     * 获取商品详情
     * GET /api/public/banners?clientId=mall-app
     */
    @GetMapping("/spuDetail/{spuId}")
    public Result<SpuDetailVO> getSpuDetail(
            @Parameter(name ="spu ID") @PathVariable Long spuId,
            @RequestParam String clientId) {

        log.info("【auth-app-spuDetail】前端传递的客户端clientId:{}",clientId);

        // 1. 通过clientId获取tenantId
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("【auth-app-spuDetail】通过clientId获取tenantWxAppInfo:{}",tenantWxAppInfo);

        Long tenantId = tenantWxAppInfo.getTenantId();

        log.info("【auth-app-spuDetail】通过clientId获取tenantId:{}",tenantId);

        SpuDetailVO spuDetail = pmsFeignClient.getSpuDetail(spuId, tenantId);
        log.info("【auth-app-spuDetail】根据tenantI过滤获取商品详情:{}",spuDetail);
        return Result.success(spuDetail);
    }


    /**
     * 获取用户的工作台菜单（包含分类和菜单项）
     */
    @Operation(summary = "获取用户的工作台菜单（包含分类和菜单项）")
    @GetMapping("/manager-categories-with-items")
    public Result<List<ManagerMenuCategoryWithItemsVO>> getWorkbenchCategoriesWithItems(
            @RequestParam String clientId
    ) {


        log.info("【auth-manager-app-categories】前端传递的客户端clientId:{}",clientId);
        // 1. 通过clientId获取tenantId
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("【auth-manager-app-categories】通过clientId获取tenantWxAppInfo:{}",tenantWxAppInfo);

        Long tenantId = tenantWxAppInfo.getTenantId();

        log.info("【auth-manager-app-categories】通过clientId获取tenantId:{}",tenantId);

        // 2. 根据tenantId查询对应的分类数据
        List<ManagerMenuCategoryWithItemsVO>  categories = tenantFeignClient.getWorkbenchCategoriesWithItems(tenantId);

        log.info("【auth-manager-app-categories】根据tenantI过滤对应的分类数据:{}",categories);

        return Result.success(categories);

    }


    /**
     * 获取首页分类（公共接口）
     * GET /api/public/categories?clientId=mall-app
     */
    @GetMapping("/manager-home-categories")
    public Result<List<ManagerMenuHomeCategoryVo>> getManagerHomeCategories(
            @RequestParam String clientId) {


        log.info("【auth-manager-app-home-categories】前端传递的客户端clientId:{}",clientId);
        // 1. 通过clientId获取tenantId
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("【auth-manager-app-home-categories】通过clientId获取tenantWxAppInfo:{}",tenantWxAppInfo);

        Long tenantId = tenantWxAppInfo.getTenantId();

        log.info("【auth-manager-app-home-categories】通过clientId获取tenantId:{}",tenantId);

        // 2. 根据tenantId查询对应的分类数据
        List<ManagerMenuHomeCategoryVo>  categories = tenantFeignClient.getManagerMenuHomeCategoryList(tenantId);

        log.info("【auth-manager-app-home-categories】根据tenantI过滤对应的分类数据:{}",categories);

        return Result.success(categories);
    }


}
