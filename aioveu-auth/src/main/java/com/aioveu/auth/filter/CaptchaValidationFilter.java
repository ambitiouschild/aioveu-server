package com.aioveu.auth.filter;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.constant.RedisConstants;
import com.aioveu.common.constant.SecurityConstants;
import com.aioveu.common.result.ResultCode;
import com.aioveu.common.web.util.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: CaptchaValidationFilter
 * @Description: TODO 图形验证码校验过滤器
 *                      功能说明：
 *                      1. 拦截登录请求，验证图形验证码
 *                      2. 防止暴力破解，提升系统安全性
 *                      3. 基于Spring Security过滤器链实现
 *                      过滤器位置：
 *                      通常配置在UsernamePasswordAuthenticationFilter之前
 *                      确保在用户认证前完成验证码校验
 *                      工作流程：
 *                      1. 匹配登录请求路径
 *                      2. 提取请求中的验证码和验证码Key
 *                      3. 从Redis获取存储的验证码
 *                      4. 比对验证码（忽略大小写）
 *                      5. 验证通过则放行，失败则拦截
 *                      技术要点：
 *                      - 使用OncePerRequestFilter确保每次请求只执行一次
 *                      - 集成Redis存储验证码，支持分布式部署
 *                      - 通过AntPathRequestMatcher精准匹配登录端点
 *                      - 采用Hutool验证码工具统一校验逻辑
 * @Author 雒世松
 * @Date 2025/12/20 17:43
 * @Version 1.0
 **/

@Slf4j
public class CaptchaValidationFilter extends OncePerRequestFilter {


    /**
     *     TODO         登录路径请求匹配器
     *              功能：用于匹配登录接口请求
     *              说明：
     *              1. AntPathRequestMatcher是Spring Security提供的路径匹配器
     *              2. 支持ANT风格路径匹配（如：/login, /api/** 等）
     *              3. 第二个参数指定HTTP方法，这里只匹配POST请求
     *              4. 通过matches()方法判断当前请求是否匹配
     *              配置示例：
     *              - 匹配路径：SecurityConstants.LOGIN_PATH（如：/oauth2/token）
     *              - 匹配方法：HttpMethod.POST（仅处理POST请求）
     */
    private static final AntPathRequestMatcher LOGIN_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher(SecurityConstants.LOGIN_PATH, HttpMethod.POST.name());


    // 需要验证码的授权类型
    private static final Set<String> CAPTCHA_REQUIRED_GRANT_TYPES = new HashSet<>(
            Arrays.asList("password")  // 只保护密码模式
    );

    /**
     *    TODO          验证码参数名常量
     *          使用场景：前端传递验证码时使用的参数名
     *          建议：
     *          1. 前端提交表单时使用这些参数名
     *          2. 保持前后端参数名一致
     *          3. 可在配置文件中配置，增强灵活性
     */
    public static final String CAPTCHA_CODE_PARAM_NAME = "captchaCode";  // 用户输入的验证码
    public static final String CAPTCHA_KEY_PARAM_NAME = "captchaKey";    // 验证码唯一标识Key


    /**
     *    TODO      Redis操作模板
     *          作用：用于操作Redis，存储和获取验证码  ✅ 正确：使用 StringRedisTemplate
     *          序列化说明：
     *          1. 通常配置为StringRedisTemplate（字符串序列化）
     *          2. 验证码以字符串形式存储
     *          3. 设置合理的过期时间（如5分钟）
     */
    private final StringRedisTemplate redisTemplate;


    /**
     *   TODO               验证码生成器
     *              作用：用于验证验证码的正确性
     *              实现方式：
     *              1. Hutool提供了多种验证码生成器
     *              2. RandomGenerator：随机字符生成器
     *              3. MathGenerator：数学计算生成器
     *              4. 可自定义实现CodeGenerator接口
     *
     * 典型用法：
     * codeGenerator.verify(原始验证码, 用户输入验证码)
     */
    private final CodeGenerator codeGenerator;


    /**
     *    TODO          构造方法 - 依赖注入
     *              @param redisTemplate Redis操作模板，用于存取验证码  使用 StringRedisTemplate，这是Spring提供的专门处理字符串的Redis模板。
     *              @param codeGenerator 验证码生成器，用于验证码校验
     *              设计模式：构造函数注入
     *              优点：
     *              1. 依赖关系明确
     *              2. 不可变对象，线程安全
     *              3. 易于单元测试
     */
    public CaptchaValidationFilter(StringRedisTemplate redisTemplate, CodeGenerator codeGenerator) {
        this.redisTemplate = redisTemplate;
        this.codeGenerator = codeGenerator;
    }


    /**
     *     TODO             过滤器核心方法 - 实现验证码校验逻辑
     *                          方法执行流程：
     *                          1. 检查是否为登录请求
     *                          2. 提取验证码参数
     *                          3. 兼容性处理
     *                          4. 验证码非空校验
     *                          5. 从Redis获取存储的验证码
     *                          6. 验证码比对
     *                          7. 验证结果处理
     *                          @param request  HTTP请求对象，包含请求参数、头信息等
     *                          @param response HTTP响应对象，用于返回错误信息
     *                          @param chain   过滤器链，用于继续执行后续过滤器
     *                          @throws ServletException Servlet相关异常
     *                          @throws IOException      IO相关异常
     *                          注意事项：
     *                          1. 此方法在每个请求中只执行一次（OncePerRequestFilter保证）
     *                          2. 验证成功后需手动删除Redis中的验证码，防止重用
     *                          3. 验证失败时需及时返回，不继续执行过滤器链
     */
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {





        // 检验登录接口的验证码
        log.info("步骤1：检查请求是否为需要验证码的登录接口");
        log.info("使用AntPathRequestMatcher匹配请求路径和方法");
        if (LOGIN_PATH_REQUEST_MATCHER.matches(request)) {
            // 请求中的验证码


            log.info("获取授权类型");
            String grantType = request.getParameter("grant_type");

            log.info("获取授权类型grant_type：{}", grantType);

            log.info("检查是否需要验证码");
            if (grantType == null || !CAPTCHA_REQUIRED_GRANT_TYPES.contains(grantType)) {
                // 非密码模式，直接放行
                log.info("授权类型 {} 不需要验证码，直接放行", grantType);
                chain.doFilter(request, response);
                return;
            }

            log.info("开始验证码验证流程，授权类型: {}", grantType);

            log.info("步骤2：从请求参数中获取用户输入的验证码");
            String captchaCode = request.getParameter(CAPTCHA_CODE_PARAM_NAME);
            log.info("用户输入的验证码captchaCode:{}", captchaCode);

            // TODO 兼容没有验证码的版本(线上请移除这个判断)
            // 说明：此兼容逻辑仅用于开发和测试环境
            // 生产环境必须强制验证码校验，否则存在安全风险
            // 建议：通过配置文件控制是否开启验证码校验
            // 生产环境必须删除这段代码
//            if (StrUtil.isBlank(captchaCode)) {
//                log.warn("验证码为空，跳过验证（兼容模式）");
//                chain.doFilter(request, response);
//                return;
//            }

            log.info("步骤3：从请求参数中获取验证码Key");
            log.info("验证码Key是验证码的唯一标识，用于从Redis获取对应验证码");
            // 缓存中的验证码
            String verifyCodeKey = request.getParameter(CAPTCHA_KEY_PARAM_NAME);

            log.info("请求参数中获取验证码verifyCodeKey:{}", verifyCodeKey);

            log.info("用户输入的验证码: {}, 验证码Key: {}", captchaCode, verifyCodeKey);


            // 步骤5：验证码非空校验
            if (StrUtil.isBlank(captchaCode)) {
                log.warn("验证码为空");
                ResponseUtils.writeErrMsg(response, ResultCode.USER_VERIFICATION_CODE_REQUIRED);
                return;
            }

            if (StrUtil.isBlank(verifyCodeKey)) {
                log.warn("验证码Key为空");
                ResponseUtils.writeErrMsg(response, ResultCode.USER_VERIFICATION_KEY_REQUIRED);
                return;
            }




            log.info("步骤4：构建Redis Key并从Redis获取存储的验证码");
            log.info("Redis Key格式示例：captcha:image:{verifyCodeKey}");
            log.info("注意：RedisConstants.Captcha.IMAGE_CODE 是验证码存储前缀常量");

            String redisKey = StrUtil.format(RedisConstants.Captcha.IMAGE_CODE, verifyCodeKey);
            log.info("构建的Redis Key: {}", redisKey);
            log.info("RedisConstants.Captcha.IMAGE_CODE 实际值: {}", RedisConstants.Captcha.IMAGE_CODE);



            String cacheVerifyCode = (String) redisTemplate.opsForValue().get(
                    StrUtil.format(RedisConstants.Captcha.IMAGE_CODE, verifyCodeKey)
            );
            log.info("Redis获取存储的验证码 ：{}", cacheVerifyCode);

            log.info("步骤5：验证码存在性检查");
            if (cacheVerifyCode == null) {

                log.info("验证码不存在或已过期");
                log.info("可能原因：1.验证码Key错误 2.验证码已过期 3.验证码已被使用");

                log.info("验证码已过期或不存在，Key: {}", verifyCodeKey);

                log.info("返回验证码过期错误");
                ResponseUtils.writeErrMsg(response, ResultCode.USER_VERIFICATION_CODE_EXPIRED);

                return;  // 重要：验证失败，直接返回，不继续执行过滤器链
            } else {

                log.info("步骤6：验证码比对");
                log.info("使用codeGenerator.verify()方法进行比对");
                log.info("此方法通常支持忽略大小写、去除空格等处理");
                // 验证码比对
                if (codeGenerator.verify(cacheVerifyCode, captchaCode)) {
                    log.info("步骤7：验证码验证成功");
                    log.info("注意：此处应该删除Redis中的验证码，防止重放攻击");
                    log.info("示例：redisTemplate.delete(key);");

                    redisTemplate.delete(verifyCodeKey);

                    log.info("继续执行过滤器链（后续可能是UsernamePasswordAuthenticationFilter）");
                    chain.doFilter(request, response);
                } else {

                    log.info("步骤8：验证码验证失败");
                    log.info("可能原因：1.用户输入错误 2.验证码被恶意尝试");
                    log.info("验证码验证失败，用户输入: {}, 期望值: {}", captchaCode, cacheVerifyCode);

                    log.info("可以记录验证失败次数，实现防暴力破解");
                    log.info("返回验证码错误响应");
                    ResponseUtils.writeErrMsg(response, ResultCode.USER_VERIFICATION_CODE_ERROR);

                    // 注意：验证失败时不删除验证码，允许用户重新尝试
                    // 但应该记录失败次数，超过阈值可锁定

                }
            }
        } else {
            // 非登录接口放行
            log.info("非登录接口，直接放行");
            log.info("说明：此过滤器只处理登录请求，其他请求不受影响");
            chain.doFilter(request, response);
        }
    }


    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        log.info("图形验证码过滤器初始化完成，拦截路径: {}", SecurityConstants.LOGIN_PATH);
    }

    /**
     * 过滤器销毁方法（可选重写）
     *
     * 可以在此方法中进行资源清理：
     * 1. 关闭连接
     * 2. 清理缓存
     * 3. 打印关闭日志
     */
    @Override
    public void destroy() {
        super.destroy();
        log.info("图形验证码过滤器已销毁");
    }
}
