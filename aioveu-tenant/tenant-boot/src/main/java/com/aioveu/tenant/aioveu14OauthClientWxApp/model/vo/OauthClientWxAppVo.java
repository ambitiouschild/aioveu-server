package com.aioveu.tenant.aioveu14OauthClientWxApp.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: OauthClientWxAppVo
 * @Description TODO OAuth2客户端与微信小程序映射视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 16:49
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "OAuth2客户端与微信小程序映射视图对象")
public class OauthClientWxAppVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "OAuth2 client客户端 ID")
    private String clientId;
    @Schema(description = "微信小程序appid")
    private String wxAppid;

    /**
     * 微信小程序appname
     */
    private String wxAppName;

    /**
     * 微信小程序注册邮箱
     */
    private String registeredEmail;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
