package com.aioveu.tenant.aioveu14OauthClientWxApp.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: OauthClientWxAppQuery
 * @Description TODO OAuth2客户端与微信小程序映射分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 16:48
 * @Version 1.0
 **/
@Schema(description ="OAuth2客户端与微信小程序映射查询对象")
@Getter
@Setter
public class OauthClientWxAppQuery extends BasePageQuery {

    @Schema(description = "OAuth2 client客户端 ID")
    private String clientId;
    @Schema(description = "微信小程序appid")
    private String wxAppid;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;

    /**
     * 微信小程序appname
     */
    private String wxAppname;

    /**
     * 微信小程序注册邮箱
     */
    private String registeredEmail;
}
