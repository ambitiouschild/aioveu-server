package com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: Oauth2AuthorizationConsent
 * @Description TODO OAuth2授权同意，记录用户对每个客户端的授权同意情况实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:21
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("oauth2_authorization_consent")
public class Oauth2AuthorizationConsent extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 注册客户端ID，指向oauth2_registered_client表
     */
    private String registeredClientId;
    /**
     * 用户主体名称，用户的唯一标识
     */
    private String principalName;
    /**
     * 用户对该客户端已同意的权限列表，JSON格式存储
     */
    private String authorities;
}
