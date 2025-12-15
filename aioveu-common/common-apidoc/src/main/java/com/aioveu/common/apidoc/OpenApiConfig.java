package com.aioveu.common.apidoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
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
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiDocInfoProperties.class)
public class OpenApiConfig {

    /**
     * OAuth2 认证 endpoint
     */
    @Value("${spring.security.oauth2.authorizationserver.token-uri}")
    private String tokenUrl;

    /**
     * API 文档信息属性
     */
    private final ApiDocInfoProperties apiDocInfoProperties;


    /**
     * OpenAPI 配置（元信息、安全协议）
     *
     * 看到你的配置了。问题在于 Knife4j 的 OAuth2 配置不会自动填充 Token 到接口。这是 Knife4j 的一个设计缺陷。我来给你修改配置
     * 你的配置使用的是 OAuth2 类型，Knife4j 会：
     * 显示复杂的 OAuth2 配置表单
     * 但不会自动将获取的 Token 填充到接口请求头
     * 需要手动复制粘贴
     *
     * 将 OAuth2 类型改为 HTTP Bearer 类型，这样 Knife4j 会：
     * 显示简单的输入框
     * 自动填充到所有接口请求头
     *
     */
    @Bean
    public OpenAPI apiInfo() {

        log.info("🔧 初始化OpenAPI配置，tokenUrl: {}", tokenUrl);

        OpenAPI openAPI = new OpenAPI()
                .components(new Components()
//                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                        .addSecuritySchemes("bearerAuth",  // ✅ 改个名字，避免冲突 // ✅ 使用简单名称
                                new SecurityScheme()
//                                        // OAuth2 授权模式
//                                        .type(SecurityScheme.Type.OAUTH2)
//                                        .name(HttpHeaders.AUTHORIZATION)
//                                        .flows(new OAuthFlows()
//                                                .password(
//                                                        new OAuthFlow()
//                                                                .tokenUrl(tokenUrl)
//                                                                .refreshUrl(tokenUrl)
//                                                )
//                                        )
//                                        // 安全模式使用Bearer令牌（即JWT）
//                                        .in(SecurityScheme.In.HEADER)
//                                        .scheme("Bearer")
//                                        .bearerFormat("JWT")

                                        // ❌ 不要用 OAUTH2，改为 HTTP
                                        .type(SecurityScheme.Type.HTTP)  // 关键修改
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Bearer Token认证，从认证服务获取Token后粘贴到这里")
                                //现在的 HTTP 模式
//                                Knife4j 显示简单的输入框
//                                只需要粘贴 Token
//                                认证成功后，Token 自动添加到所有请求头
                        )
                )
                // 接口全局添加 Authorization 参数
//                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                // 接口文档信息(不重要)
                .info(new Info()
                        .title(apiDocInfoProperties.getTitle())
                        .version(apiDocInfoProperties.getVersion())
                        .description(apiDocInfoProperties.getDescription())
                        .contact(new Contact()
                                .name(apiDocInfoProperties.getContact().getName())
                                .url(apiDocInfoProperties.getContact().getUrl())
                                .email(apiDocInfoProperties.getContact().getEmail())
                        )
                        .license(new License().name(apiDocInfoProperties.getLicense().getName())
                                .url(apiDocInfoProperties.getLicense().getUrl())
                        ));

        log.info("✅ OpenAPI配置完成");

        return openAPI;

    }

//    你的配置中至少有两个地方定义了名为 "default" 的 OpenAPI 分组：
//    1.在 OpenApiConfig.java中的 publicApi()方法
//    2.在 application.yml中的 springdoc.group-configs


/*    1. 理解修改的作用
        修改前（OAuth2 模式）：
            Knife4j 显示复杂的 OAuth2 配置表单
            需要填写：用户名、密码、Client ID、Client Secret
            但认证成功后，Token 不会自动添加到请求头

        修改后（HTTP 模式）：
            Knife4j 显示简单的 Token 输入框
            只需要粘贴：Bearer 你的Token
            认证成功后，Token 会自动添加到所有请求头

    关键点：
            ✅ Token 获取方式完全不变
            ✅ 还是调用 /oauth2/token接口
            ✅ 只是 Knife4j 的界面变简单了*/

}
