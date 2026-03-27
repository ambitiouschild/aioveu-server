package com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryAdministratorInfoForm
 * @Description TODO 管理员信息表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:44
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "管理员信息表单对象")
public class RegistryAdministratorInfoForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "管理员ID")
    private Long id;

    @Schema(description = "所属租户ID")
    @NotNull(message = "所属租户ID不能为空")
    private Long tenantId;

    @Schema(description = "管理员真实姓名")
    @NotBlank(message = "管理员真实姓名不能为空")
    @Size(max=50, message="管理员真实姓名长度不能超过50个字符")
    private String realName;

    @Schema(description = "身份证号码")
    @NotBlank(message = "身份证号码不能为空")
    @Size(max=18, message="身份证号码长度不能超过18个字符")
    private String idCardNo;

    @Schema(description = "身份证正面照片")
    @Size(max=500, message="身份证正面照片长度不能超过500个字符")
    private String idCardFrontPath;

    @Schema(description = "身份证反面照片")
    @Size(max=500, message="身份证反面照片长度不能超过500个字符")
    private String idCardBackPath;

    @Schema(description = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    @Size(max=20, message="手机号码长度不能超过20个字符")
    private String phone;

    @Schema(description = "短信验证码")
    @Size(max=10, message="短信验证码长度不能超过10个字符")
    private String phoneVerifyCode;

    @Schema(description = "手机是否已验证：0-未验证，1-已验证")
    @NotNull(message = "手机是否已验证：0-未验证，1-已验证不能为空")
    private Integer phoneVerified;

    @Schema(description = "管理员微信OpenID")
    @Size(max=100, message="管理员微信OpenID长度不能超过100个字符")
    private String wechatOpenid;

    @Schema(description = "管理员微信UnionID")
    @Size(max=100, message="管理员微信UnionID长度不能超过100个字符")
    private String wechatUnionid;

    @Schema(description = "微信扫码是否已验证")
    @NotNull(message = "微信扫码是否已验证不能为空")
    private Integer wechatQrScanned;

    @Schema(description = "微信扫码验证时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime wechatScannedTime;
}
