package com.aioveu.registry.aioveu02RegistryAppAccount.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RegistryAppAccount
 * @Description TODO 小程序账号实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:01
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("registry_app_account")
public class RegistryAppAccount extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 小程序AppID
     */
    private Long appId;
    /**
     * 所属租户ID
     */
    private Long tenantId;
    /**
     * 小程序AppSecret
     */
    private String appSecret;
    /**
     * 原始ID
     */
    private String originalId;
    /**
     * 小程序名称
     */
    private String accountName;
    /**
     * 账号类型：0-未注册，1-普通小程序，2-游戏小程序
     */
    private Integer accountType;
    /**
     * 注册邮箱
     */
    private String email;
    /**
     * 登录密码（加密存储）
     */
    private String password;
    /**
     * 邮箱验证码
     */
    private String emailVerifyCode;
    /**
     * 邮箱是否已验证：0-未验证，1-已验证
     */
    private Integer emailVerified;
    /**
     * 注册状态：0-未开始，1-邮箱注册中，2-信息登记中，3-主体认证中，4-注册完成
     */
    private Integer registerStatus;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
