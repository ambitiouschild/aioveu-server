package com.aioveu.registry.aioveu02RegistryAppAccount.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: RegistryAppAccountForm
 * @Description TODO 小程序账号表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:02
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "小程序账号表单对象")
public class RegistryAppAccountForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "小程序AppID")
    @NotNull(message = "小程序AppID不能为空")
    private Long appId;

    @Schema(description = "所属租户ID")
    @NotNull(message = "所属租户ID不能为空")
    private Long tenantId;

    @Schema(description = "小程序AppSecret")
    @Size(max=100, message="小程序AppSecret长度不能超过100个字符")
    private String appSecret;

    @Schema(description = "原始ID")
    @Size(max=50, message="原始ID长度不能超过50个字符")
    private String originalId;

    @Schema(description = "小程序名称")
    @NotBlank(message = "小程序名称不能为空")
    @Size(max=200, message="小程序名称长度不能超过200个字符")
    private String accountName;

    @Schema(description = "账号类型：0-未注册，1-普通小程序，2-游戏小程序")
    private Integer accountType;

    @Schema(description = "注册邮箱")
    @NotBlank(message = "注册邮箱不能为空")
    @Size(max=100, message="注册邮箱长度不能超过100个字符")
    private String email;

    @Schema(description = "登录密码（加密存储）")
    @Size(max=100, message="登录密码（加密存储）长度不能超过100个字符")
    private String password;

    @Schema(description = "邮箱验证码")
    @Size(max=10, message="邮箱验证码长度不能超过10个字符")
    private String emailVerifyCode;

    @Schema(description = "邮箱是否已验证：0-未验证，1-已验证")
    private Integer emailVerified;

    @Schema(description = "注册状态：0-未开始，1-邮箱注册中，2-信息登记中，3-主体认证中，4-注册完成")
    @NotNull(message = "注册状态：0-未开始，1-邮箱注册中，2-信息登记中，3-主体认证中，4-注册完成不能为空")
    private Integer registerStatus;
}
