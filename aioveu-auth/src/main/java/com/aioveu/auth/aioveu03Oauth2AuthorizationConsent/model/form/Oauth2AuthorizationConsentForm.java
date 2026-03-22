package com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: Oauth2AuthorizationConsentForm
 * @Description TODO OAuth2授权同意，记录用户对每个客户端的授权同意情况表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:21
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "OAuth2授权同意，记录用户对每个客户端的授权同意情况表单对象")
public class Oauth2AuthorizationConsentForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "注册客户端ID，指向oauth2_registered_client表")
    @Size(max=100, message="注册客户端ID，指向oauth2_registered_client表长度不能超过100个字符")
    private String registeredClientId;

    @Schema(description = "用户主体名称，用户的唯一标识")
    @NotBlank(message = "用户主体名称，用户的唯一标识不能为空")
    @Size(max=200, message="用户主体名称，用户的唯一标识长度不能超过200个字符")
    private String principalName;

    @Schema(description = "用户对该客户端已同意的权限列表，JSON格式存储")
    @NotBlank(message = "用户对该客户端已同意的权限列表，JSON格式存储不能为空")
    @Size(max=1000, message="用户对该客户端已同意的权限列表，JSON格式存储长度不能超过1000个字符")
    private String authorities;
}
