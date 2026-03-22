package com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: Oauth2RegisteredClientQuery
 * @Description TODO OAuth2注册客户端，存储所有已注册的客户端应用信息分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 15:01
 * @Version 1.0
 **/
@Schema(description ="OAuth2注册客户端，存储所有已注册的客户端应用信息查询对象")
@Getter
@Setter
public class Oauth2RegisteredClientQuery extends BasePageQuery {

    @Schema(description = "客户端记录的唯一标识符，通常是UUID")
    private String id;
    @Schema(description = "客户端标识符，OAuth2请求中使用的client_id")
    private String clientId;
    @Schema(description = "客户端ID的创建时间")
    private LocalDateTime clientIdIssuedAt;
    @Schema(description = "客户端显示名称，用于用户界面显示")
    private String clientName;
    @Schema(description = "支持的授权类型，JSON格式数组。")
    private String authorizationGrantTypes;
    @Schema(description = "客户端可请求的范围列表，JSON格式数组。")
    private String scopes;
}
