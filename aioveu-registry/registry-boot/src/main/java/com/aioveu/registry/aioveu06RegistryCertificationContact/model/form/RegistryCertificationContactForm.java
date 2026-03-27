package com.aioveu.registry.aioveu06RegistryCertificationContact.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: RegistryCertificationContactForm
 * @Description TODO 认证联系人表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:24
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "认证联系人表单对象")
public class RegistryCertificationContactForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "联系人ID")
    private Long id;

    @Schema(description = "所属租户ID")
    @NotNull(message = "所属租户ID不能为空")
    private Long tenantId;

    @Schema(description = "认证记录ID")
    @NotNull(message = "认证记录ID不能为空")
    private Long certificationId;

    @Schema(description = "联系人姓名")
    @NotBlank(message = "联系人姓名不能为空")
    @Size(max=50, message="联系人姓名长度不能超过50个字符")
    private String contactName;

    @Schema(description = "联系人身份证号")
    @Size(max=18, message="联系人身份证号长度不能超过18个字符")
    private String contactIdCard;

    @Schema(description = "联系人手机号")
    @NotBlank(message = "联系人手机号不能为空")
    @Size(max=20, message="联系人手机号长度不能超过20个字符")
    private String contactPhone;

    @Schema(description = "联系人短信验证码")
    @Size(max=10, message="联系人短信验证码长度不能超过10个字符")
    private String contactPhoneVerifyCode;

    @Schema(description = "联系人座机（含分机）")
    @Size(max=50, message="联系人座机（含分机）长度不能超过50个字符")
    private String contactLandline;

    @Schema(description = "联系人微信OpenID")
    @Size(max=100, message="联系人微信OpenID长度不能超过100个字符")
    private String contactWechatOpenid;

    @Schema(description = "联系人微信扫码是否验证")
    private Integer contactWechatScanned;
}
