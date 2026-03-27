package com.aioveu.registry.aioveu02RegistryAppAccount.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryAppAccountVo
 * @Description TODO 小程序账号视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:04
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "小程序账号视图对象")
public class RegistryAppAccountVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "小程序AppID")
    private Long appId;
    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "小程序AppSecret")
    private String appSecret;
    @Schema(description = "原始ID")
    private String originalId;
    @Schema(description = "小程序名称")
    private String accountName;
    @Schema(description = "账号类型：0-未注册，1-普通小程序，2-游戏小程序")
    private Integer accountType;
    @Schema(description = "注册邮箱")
    private String email;
    @Schema(description = "登录密码（加密存储）")
    private String password;
    @Schema(description = "邮箱验证码")
    private String emailVerifyCode;
    @Schema(description = "邮箱是否已验证：0-未验证，1-已验证")
    private Integer emailVerified;
    @Schema(description = "注册状态：0-未开始，1-邮箱注册中，2-信息登记中，3-主体认证中，4-注册完成")
    private Integer registerStatus;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


}
