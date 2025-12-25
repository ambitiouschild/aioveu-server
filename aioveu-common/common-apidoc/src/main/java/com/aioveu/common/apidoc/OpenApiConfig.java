package com.aioveu.common.apidoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import lombok.RequiredArgsConstructor;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/6/5 14:51
 * @Param:
 * @Return:
 * @Description: TODO OpenAPI 配置类 基于 OpenAPI 3.0 规范 + SpringDoc 实现 + knife4j 增强
 *
 *   TODO
 **         功能说明:
 *          *   - 基于 OpenAPI 3.0 规范 + SpringDoc 实现 API 文档生成
 *          *   - 集成 knife4j 进行文档增强和界面美化
 *          *   - 配置 OAuth2 安全认证，支持 JWT Bearer Token
 *          *   - 自动为所有接口添加认证要求
 *          *
 *          * 技术特性:
 *          *   - ✅ 自动生成交互式 API 文档
 *          *   - ✅ 支持在线调试和接口测试
 *          *   - ✅ OAuth2 密码模式认证集成
 *          *   - ✅ JWT Bearer Token 自动传递
 *          *   - ✅ 可配置的 API 元信息
 *          *
 *          * 使用方式:
 *          *   - 访问: http://localhost:端口/doc.html
 *          *   - 点击"授权"按钮输入 token 进行认证
 *          *   - 认证后所有请求自动携带 Authorization 头
 *          *
 *          * 依赖组件:
 *          *   - springdoc-openapi: OpenAPI 3.0 规范实现
 *          *   - knife4j: Swagger 增强 UI
 *          *   - Spring Security OAuth2: 认证支持

 **/

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiDocInfoProperties.class)
public class OpenApiConfig {

    /**
     * OAuth2 认证 endpoint
     */
    /**
     * OAuth2 令牌端点 URL
     * 从配置文件中读取认证服务器的 token 获取地址
     * 示例: http://localhost:9000/oauth2/token
     */
    @Value("${spring.security.oauth2.authorizationserver.token-uri}")
    private String tokenUrl;

    /**
     * API 文档信息属性
     */
    /**
     * API 文档信息配置属性
     * 通过 @EnableConfigurationProperties 注入，支持配置文件外部化配置
     */
    private final ApiDocInfoProperties apiDocInfoProperties;


    /**
     * OpenAPI 配置（元信息、安全协议）
     */
    /**
     * 配置 OpenAPI 文档元信息和安全协议
     *
     * 核心功能:
     * 1. 定义安全方案 - OAuth2 密码模式
     * 2. 全局安全要求 - 所有接口需要认证
     * 3. API 元信息 - 标题、版本、描述等
     *
     * @return OpenAPI 配置实例
     *
     * 安全流程说明:
     * 1. 用户在 Swagger UI 点击"授权"按钮
     * 2. 输入用户名密码（OAuth2 密码模式）
     * 3. 系统向 tokenUrl 发起认证请求
     * 4. 获取 JWT token 并自动存储
     * 5. 后续所有请求自动添加 Authorization: Bearer <token>
     */
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                // ==================== 安全组件配置 ====================
                .components(new Components()
                        // 添加安全方案定义 - OAuth2 密码授权模式
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                                new SecurityScheme()
                                        // 认证类型: OAuth2
                                        .type(SecurityScheme.Type.OAUTH2)
                                        // 安全方案名称，对应 SecurityRequirement
                                        .name(HttpHeaders.AUTHORIZATION)
                                        // OAuth2 流程配置 - 密码模式
                                        .flows(new OAuthFlows()
                                                .password(
                                                        new OAuthFlow()
                                                                // Token 获取端点
                                                                .tokenUrl(tokenUrl)
                                                                // Token 刷新端点（同获取端点）
                                                                .refreshUrl(tokenUrl)
                                                )
                                        )
                                        // 安全模式使用Bearer令牌（即JWT） // Token 位置: 请求头
                                        .in(SecurityScheme.In.HEADER)
                                        // HTTP 认证方案: Bearer
                                        .scheme("Bearer")
                                        // Token 格式: JWT
                                        .bearerFormat("JWT")
                        )
                )
                // ==================== 全局安全要求 ====================
                // 为所有接口添加认证要求，除非接口单独配置 @SecurityRequirement
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                // 接口文档信息(不重要)
                // ==================== API 元信息 ====================
                .info(new Info()
                        // API 标题
                        .title(apiDocInfoProperties.getTitle())
                        // API 版本
                        .version(apiDocInfoProperties.getVersion())
                        // API 详细描述，支持 Markdown
                        .description(apiDocInfoProperties.getDescription())
                        // 联系人信息
                        .contact(new Contact()
                                .name(apiDocInfoProperties.getContact().getName())
                                .url(apiDocInfoProperties.getContact().getUrl())
                                .email(apiDocInfoProperties.getContact().getEmail())
                        )
                        // 许可证信息
                        .license(new License().name(apiDocInfoProperties.getLicense().getName())
                                .url(apiDocInfoProperties.getLicense().getUrl())
                                // 可扩展: 服务条款、外部文档等
                                // .termsOfService("http://example.com/terms/")
                                // .extensions(...)
                        ));
    }
}
