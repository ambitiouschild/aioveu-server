package com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: Oauth2AuthorizationConsentVo
 * @Description TODO OAuth2授权同意，记录用户对每个客户端的授权同意情况视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:27
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "OAuth2授权同意，记录用户对每个客户端的授权同意情况视图对象")
public class Oauth2AuthorizationConsentVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "注册客户端ID，指向oauth2_registered_client表")
    private String registeredClientId;
    @Schema(description = "用户主体名称，用户的唯一标识")
    private String principalName;
    @Schema(description = "用户对该客户端已同意的权限列表，JSON格式存储")
    private String authorities;
}
