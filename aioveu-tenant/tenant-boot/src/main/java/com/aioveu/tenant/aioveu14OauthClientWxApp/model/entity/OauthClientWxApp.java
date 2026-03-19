package com.aioveu.tenant.aioveu14OauthClientWxApp.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: OauthClientWxApp
 * @Description TODO OAuth2客户端与微信小程序映射实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 16:46
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("sys_oauth_client_wx_app")
public class OauthClientWxApp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * OAuth2 client客户端 ID
     */
    private String clientId;
    /**
     * 微信小程序appid
     */
    private String wxAppid;

    /**
     * 微信小程序appname
     */
    private String wxAppName;

    /**
     * 微信小程序注册邮箱
     */
    private String registeredEmail;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
