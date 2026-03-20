package com.aioveu.tenant.aioveu14OauthClientWxApp.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: OauthClientWxAppForm
 * @Description TODO OAuth2客户端与微信小程序映射表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 16:47
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "OAuth2客户端与微信小程序映射表单对象")
public class OauthClientWxAppForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "OAuth2 client客户端 ID")
    @NotBlank(message = "OAuth2 client客户端 ID不能为空")
    @Size(max=255, message="OAuth2 client客户端 ID长度不能超过255个字符")
    private String clientId;

    @Schema(description = "微信小程序appid")
    @NotBlank(message = "微信小程序appid不能为空")
    @Size(max=255, message="微信小程序appid长度不能超过255个字符")
    private String wxAppid;


    /**
     * 微信小程序appname
     */
    private String wxAppname;

    /**
     * 微信小程序注册邮箱
     */
    private String registeredEmail;


    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
}
