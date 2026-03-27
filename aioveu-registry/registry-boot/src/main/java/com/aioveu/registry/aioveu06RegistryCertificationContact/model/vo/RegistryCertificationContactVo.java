package com.aioveu.registry.aioveu06RegistryCertificationContact.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryCertificationContactVo
 * @Description TODO 认证联系人视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:26
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "认证联系人视图对象")
public class RegistryCertificationContactVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "联系人ID")
    private Long id;
    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "认证记录ID")
    private Long certificationId;
    @Schema(description = "联系人姓名")
    private String contactName;
    @Schema(description = "联系人身份证号")
    private String contactIdCard;
    @Schema(description = "联系人手机号")
    private String contactPhone;
    @Schema(description = "联系人短信验证码")
    private String contactPhoneVerifyCode;
    @Schema(description = "联系人座机（含分机）")
    private String contactLandline;
    @Schema(description = "联系人微信OpenID")
    private String contactWechatOpenid;
    @Schema(description = "联系人微信扫码是否验证")
    private Integer contactWechatScanned;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


}
