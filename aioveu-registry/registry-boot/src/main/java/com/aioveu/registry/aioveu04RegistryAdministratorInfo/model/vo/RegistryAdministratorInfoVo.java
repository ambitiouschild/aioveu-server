package com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryAdministratorInfoVo
 * @Description TODO 管理员信息视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:45
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "管理员信息视图对象")
public class RegistryAdministratorInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "管理员ID")
    private Long id;
    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "管理员真实姓名")
    private String realName;
    @Schema(description = "身份证号码")
    private String idCardNo;
    @Schema(description = "身份证正面照片")
    private String idCardFrontPath;
    @Schema(description = "身份证反面照片")
    private String idCardBackPath;
    @Schema(description = "手机号码")
    private String phone;
    @Schema(description = "短信验证码")
    private String phoneVerifyCode;
    @Schema(description = "手机是否已验证：0-未验证，1-已验证")
    private Integer phoneVerified;
    @Schema(description = "管理员微信OpenID")
    private String wechatOpenid;
    @Schema(description = "管理员微信UnionID")
    private String wechatUnionid;
    @Schema(description = "微信扫码是否已验证")
    private Integer wechatQrScanned;
    @Schema(description = "微信扫码验证时间")
    private LocalDateTime wechatScannedTime;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
