package com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: Oauth2AuthorizationConsentQuery
 * @Description TODO OAuth2授权同意，记录用户对每个客户端的授权同意情况分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:22
 * @Version 1.0
 **/
@Schema(description ="OAuth2授权同意，记录用户对每个客户端的授权同意情况查询对象")
@Getter
@Setter
public class Oauth2AuthorizationConsentQuery extends BasePageQuery {

    @Schema(description = "注册客户端ID，指向oauth2_registered_client表")
    private String registeredClientId;
    @Schema(description = "用户主体名称，用户的唯一标识")
    private String principalName;
    @Schema(description = "用户对该客户端已同意的权限列表，JSON格式存储")
    private String authorities;
}
