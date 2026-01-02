package com.aioveu.common.security.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.aioveu.common.constant.JwtClaimConstants;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

/**
 * @Description: TODO 资源服务器配置  - Spring Security OAuth2资源服务器核心配置类
 *                  核心功能：配置业务微服务（非认证服务）的安全策略，保护REST API接口
 *                   * 主要职责：JWT令牌验证、接口权限控制、白名单配置、异常处理
 *                   *
 *                   * 适用场景：所有需要JWT令牌保护的业务微服务（如用户服务、订单服务等）
 *
 * TODO Spring Security 安全体系
 *     ├── WebSecurityCustomizer (完全忽略的路径 - 最高性能)
 *     ├── SecurityFilterChain (主要安全配置)
 *     │   ├── 白名单路径 (permitAll() - 经过过滤器但无需认证)
 *     │   ├── 需认证路径 (authenticated() - 需要JWT认证)
 *     │   └── OAuth2资源服务器配置 (JWT令牌处理)
 *     └── 全局异常处理 (认证入口点和拒绝处理器)
 *
 * TODO JWT验证架构解析
 *           1.认证服务将JWK（包含公钥）存储在Redis中
 *           2.认证服务通过/oauth2/jwks端点暴露公钥
 *           3.其他微服务通过配置的jwk-set-uri获取公钥来验证JWT
 *          // Spring Boot自动根据jwk-set-uri创建JwtDecoder
 *          // 不需要手动配置JwtDecoder Bean
 *                 ${gateway.endpoint}/aioveu-auth/oauth2/jwks
 *                  ↓ 解析为
 *                  http://api-gateway:8080/aioveu-auth/oauth2/jwks
 *                  ↓ 网关路由到
 *                  http://auth-service:9000/oauth2/jwks
 *                  ↓ 认证服务从Redis读取
 *                  Redis键: security:jwk:set
 *
 *   TODO               你的架构设计是完全正确的：
 *                   ✅ 集中存储：认证服务将JWKSet存储在Redis中
 *                   ✅ 标准端点：通过/oauth2/jwks端点暴露公钥
 *                   ✅ 自动发现：业务服务通过jwk-set-uri自动获取公钥
 *                   ✅ 标准验证：使用Spring Security标准JWT验证机制
 *                   Redis的持久化优势（密钥不丢失）
 *                   标准OAuth2端点（兼容性好）
 *                   Spring Security自动配置（开发简便）
 *                   微服务架构（解耦性好）
 *                   是一个既符合标准又实用的JWT验证架构！
 *
 * @Author: 雒世松
 * @Date: 2025/6/5 16:10
 * @param
 * @return:
 **/


// 从application.yml中读取security前缀的配置
@ConfigurationProperties(prefix = "security")
// 翻译：类被标记为@ConstructorBinding，但又被定义为Spring组件
@Configuration   // 标记为配置类
@EnableWebSecurity   // 启用Spring Security Web安全支持
@EnableMethodSecurity    // 启用方法级安全注解（如@PreAuthorize）
@RequiredArgsConstructor   // Lombok注解，自动注入final字段
@Slf4j
public class ResourceServerConfig {


    // 自定义访问拒绝处理器（403 Forbidden情况）
    private final AccessDeniedHandler accessDeniedHandler;

    // 自定义认证入口点（401 Unauthorized情况）
    private final AuthenticationEntryPoint authenticationEntryPoint;



    /**
     * 白名单路径列表 - 从配置文件动态注入
     * 配置示例：
     * security:
     *   whitelist-paths:
     *     - "/api/v1/public/**"
     *     - "/health"
     *     - "/actuator/info"
     */
    @Setter
    private List<String> whitelistPaths;


    /**
     * TODO 安全过滤器链配置 - 资源服务器的核心安全配置
     *
     *  TODO    配置内容：
     *      1. 请求授权规则（哪些路径需要认证，哪些可以匿名访问）
     *      2. CSRF防护配置
     *      3. OAuth2资源服务器JWT认证配置
     *      4. 自定义异常处理器
     *
     * @param http HttpSecurity对象，用于构建安全配置
     * @param introspector HandlerMappingIntrospector，用于MVC路径匹配
     * @return SecurityFilterChain 安全过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {


        // 创建MVC请求匹配器构建器，支持Spring MVC的路径模式匹配
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        // 记录白名单路径，便于调试和监控
        log.info("记录白名单路径，便于调试和监控");
        log.info("whitelist path:{}", JSONUtil.toJsonStr(whitelistPaths));

        // 配置HTTP请求授权规则
        http.authorizeHttpRequests((requests) ->
                        {
                            // 配置白名单路径 - 这些路径不需要认证即可访问
                            if (CollectionUtil.isNotEmpty(whitelistPaths)) {
                                for (String whitelistPath : whitelistPaths) {

                                    // 使用MVC模式匹配器配置白名单路径为允许所有人访问
                                    requests.requestMatchers(mvcMatcherBuilder.pattern(whitelistPath)).permitAll();
                                }
                            }
                            // 除白名单外的所有其他请求都需要认证
                            requests.anyRequest().authenticated();
                        }
                )
                // 禁用CSRF防护 - 对于REST API通常不需要CSRF保护
                .csrf(AbstractHttpConfigurer::disable)
        ;
        // 配置OAuth2资源服务器（JWT令牌认证）
        // 你的代码中没有显式配置JwtDecoder
        // Spring Boot会尝试自动配置
        http.oauth2ResourceServer(resourceServerConfigurer ->
                        resourceServerConfigurer
                                // 配置JWT认证，使用自定义的JWT转换器
                                .jwt(jwtConfigurer -> jwtAuthenticationConverter())  // 只配置了转换器
                                // 设置自定义认证入口点（处理401未认证）
                                .authenticationEntryPoint(authenticationEntryPoint)
                                // 设置自定义访问拒绝处理器（处理403权限不足）
                                .accessDeniedHandler(accessDeniedHandler)
                // 没有配置issuer-uri或jwk-set-uri
        );
        return http.build();
    }

    /**
     * TODO Web安全定制器 - 配置完全绕过Spring Security过滤器链的路径
     *      与白名单的区别：
     *      - 白名单路径：经过安全过滤器但允许匿名访问
     *      - 忽略的路径：完全绕过安全过滤器，性能更好
     *
     * 适用场景：静态资源、API文档等完全公开的内容
     *
     * @return WebSecurityCustomizer Web安全定制器
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        // Swagger/OpenAPI相关静态资源 - 完全绕过安全过滤
        return (web) -> web.ignoring().requestMatchers(
                AntPathRequestMatcher.antMatcher("/webjars/**"),   // WebJars静态资源
                AntPathRequestMatcher.antMatcher("/doc.html"),     // Knife4j文档页面
                AntPathRequestMatcher.antMatcher("/swagger-resources/**"),   // Swagger资源
                AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),      // OpenAPI v3规范
                AntPathRequestMatcher.antMatcher("/swagger-ui/**")     // Swagger UI界面
        );
    }

    /**
     * MVC请求匹配器构建器 - 用于创建基于Spring MVC的路径匹配器
     *
     * 优势：支持Spring MVC的完整路径匹配模式，包括路径变量等
     * 示例：/api/v1/users/{id} 可以正确匹配 /api/v1/users/123
     *
     * @param introspector HandlerMappingIntrospector Spring MVC的处理器映射内省器
     * @return MvcRequestMatcher.Builder MVC请求匹配器构建器
     */
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    /**
     * TODO   自定义JWT认证转换器 - 核心组件：从JWT令牌中提取用户权限信息
     *          功能说明：
     *          1. 从JWT的声明(claims)中提取权限信息
     *          2. 将权限字符串转换为Spring Security的GrantedAuthority对象
     *          3. 设置权限的前缀和声明字段名
     *          注意：此转换器被JwtAuthenticationProvider使用
     *           // 1. 请求进入Spring Security过滤器链
     *           // 2. 调用JwtDecoder解码令牌
     *           // 3. NimbusJwtDecoder从jwks端点获取公钥
     *           // 这里会发起HTTP请求到：${gateway.endpoint}/aioveu-auth/oauth2/jwks
     *
     *  TODO    公钥获取发生在 JWT验证的第一步，在jwtAuthenticationConverter之前
     *           // Spring Security过滤器链的执行顺序
     *           1. 请求到达 → 2. JwtDecoder验证签名（获取公钥） → 3. jwtAuthenticationConverter提取权限
     *           虽然你的ResourceServerConfig中没有显式代码，但公钥获取发生在
     *             // Spring Security自动配置的过滤器链中
     *                  JwtAuthenticationProvider.authenticate() {
     *                             // 这里调用JwtDecoder，触发公钥获取
     *                       Jwt jwt = this.jwtDecoder.decode(authentication.getCredentials());
     *                       // 只有验证成功后，才执行你的jwtAuthenticationConverter
     *                       AbstractAuthenticationToken authResult = this.jwtAuthenticationConverter.convert(jwt);
     *                     }
     *       获取公钥的具体时机：
     *              触发条件：第一个需要JWT验证的请求到达时
     *              具体位置：在JwtDecoder.decode()方法内部
     *              执行顺序：在jwtAuthenticationConverter.convert()之前
     *              缓存机制：首次获取后缓存，定期刷新
     *              简单来说：公钥获取发生在JWT签名验证阶段，而这个阶段在你的自定义转换器执行之前就完成了。
     *
     * @return Converter<Jwt, AbstractAuthenticationToken> JWT认证转换器
     *
     * @see JwtAuthenticationProvider#setJwtAuthenticationConverter(Converter)
     */
    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {

        // 创建JWT权限转换器 - 负责从JWT声明中提取权限信息
//        jwtAuthenticationConverter是在JWT验证之后使用的，用于提取权限。
        // JWT的签名验证（使用公钥）已经在此之前完成
        log.info("==== 触发获取公钥:第一个需要JWT验证的请求到达时 ====");
        log.info("====JWT的签名验证（使用公钥）已经在此之前完成====");
        log.info("jwtAuthenticationConverter是在JWT验证之后使用的，用于提取权限。");
        log.info("创建JWT权限转换器 - 负责从JWT声明中提取权限信息");
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // 设置权限前缀为空字符串（默认是"SCOPE_"）
        // 例如：JWT中的"admin"权限转换为"admin"而不是"SCOPE_admin"
        log.info("设置权限前缀为空字符串（默认是\"SCOPE_\"）");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(Strings.EMPTY);

        // 设置JWT中存储权限信息的声明字段名（对应JwtTokenCustomizerConfig中设置的声明）
        log.info("设置JWT中存储权限信息的声明字段名（对应JwtTokenCustomizerConfig中设置的声明）");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(JwtClaimConstants.AUTHORITIES);

        // 创建JWT认证转换器 - 主要的认证转换组件
        log.info("创建JWT认证转换器 - 主要的认证转换组件");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        // 设置权限转换器，将JWT中的权限信息转换为Spring Security的权限对象
        log.info("设置权限转换器，将JWT中的权限信息转换为Spring Security的权限对象");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

/*   TODO 我们注意到在ResourceServerConfig中，我们配置了jwtAuthenticationConverter，但是并没有显式配置JwtDecoder。
          根据之前的讨论，我们是在application.yml中配置了jwk-set-uri，然后Spring Boot会自动配置JwtDecoder。
          但是，在这个配置类中，我们并没有看到直接使用jwk-set-uri来创建JwtDecoder的代码。
          实际上，Spring Boot的自动配置类OAuth2ResourceServerJwtConfiguration会在classpath下存在JwtDecoder类时自动配置一个JwtDecoder Bean。
          它会根据我们在application.yml中提供的jwk-set-uri属性创建一个NimbusJwtDecoder。
          因此，在ResourceServerConfig中，我们不需要显式提供JwtDecoder的@Bean，除非我们需要自定义。
          所以，公钥的获取是通过在application.yml中配置的jwk-set-uri，由Spring Boot自动创建JwtDecoder，该JwtDecoder会从指定的jwk-set-uri获取公钥。
          那么，在ResourceServerConfig中，我们只需要配置如何从JWT中提取权限（即jwtAuthenticationConverter）即可。
          总结：公钥的获取是Spring Boot自动完成的，我们只需要提供jwk-set-uri配置。

   TODO       在你的代码中，公钥获取发生在：
                    1.隐式位置：Spring Boot自动创建的JwtDecoder中
                    2.具体调用：NimbusJwtDecoder内部通过HTTP请求获取JWK Set
                    3.端点地址：${gateway.endpoint}/aioveu-auth/oauth2/jwks
                    4.触发时机：第一次JWT验证请求时，后续会缓存
                    这种设计的好处是完全符合OAuth2标准，公钥获取对业务代码透明，只需要简单的配置即可工作。
*/


    /**
     * 配置密码编码器
     * 使用 BCrypt 强哈希函数加密密码
     *
     * @原理说明:
     *   - BCrypt 会自动生成随机的盐值(salt)，相同的密码每次加密结果不同
     *   - 内置强度因子(默认10)，可抵抗暴力破解
     *   - 符合密码学安全标准，广泛用于密码存储
     *
     * @安全特性:
     *   - 防彩虹表攻击（随机盐值）
     *   - 自适应计算成本（可调整强度）
     *   - 密码哈希不可逆
     *
     * @使用示例:
     *   - 加密: passwordEncoder.encode("rawPassword")
     *   - 验证: passwordEncoder.matches("rawPassword", "encodedPassword")
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
