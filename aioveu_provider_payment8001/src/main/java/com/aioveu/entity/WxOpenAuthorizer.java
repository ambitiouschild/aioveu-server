package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description 微信开放平台授权表
 * @Author luyao
 * @Date: 2025-02-09 10:04:24
 */

@TableName("sport_wx_open_authorizer")
@Data
public class WxOpenAuthorizer extends StringIdEntity {

    /**
     * 第三方平台appid
     */
    private String componentAppId;

    /**
     * 授权appid
     */
    private String authorizerAppId;
    /**
     * 主体信息
     */
    private String principalName;
    /**
     * 小程序名称
     */
    private String nickName;
    /**
     * 小程序图标
     */
    private String headImg;
    /**
     * 账号介绍
     */
    private String signature;

    /**
     * 令牌有效期
     */
    private int expiresIn;

    /**
     * 刷新令牌
     */
    private String authorizerRefreshToken;

    /**
     * 刷新令牌时间，单位秒
     */
    private Date authorizerRefreshDate;

    /**
     * 授权时间
     */
    private Date authorizerDate;
    /**
     * 授权给开发者的权限集列表
     */
    private String funcInfo;

    /**
     * 域名是否配置
     */
    private Boolean domainConfig;

    /**
     * webview是否配置
     */
    private Boolean webviewConfig;

    /**
     * 是否进行隐私配置
     */
    private Boolean privacySetting;

    /**
     * 授权人
     */
    private String authorizerUserId;

    //0 删除 1 授权 2 解除授权
}
