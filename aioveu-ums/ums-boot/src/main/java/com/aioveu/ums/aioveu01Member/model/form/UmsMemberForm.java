package com.aioveu.ums.aioveu01Member.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @ClassName: UmsMemberForm
 * @Description TODO  会员表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 14:42
 * @Version 1.0
 **/


@Getter
@Setter
@Schema(description = "会员表单对象")
public class UmsMemberForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会员ID")
    private Long id;

    @Schema(description = "昵称")
    @NotBlank(message = "昵称不能为空")
    @Size(max=50, message="昵称长度不能超过50个字符")
    private String nickName;

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    @Size(max=20, message="手机号长度不能超过20个字符")
    private String mobile;

    @Schema(description = "头像URL")
    @Size(max=255, message="头像URL长度不能超过255个字符")
    private String avatarUrl;

    @Schema(description = "性别(0=未知,1=男,2=女)")
    private Integer gender;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "会员积分")
    private Integer point;

    @Schema(description = "账户余额(单位:分)")
    private Long balance;

    @Schema(description = "状态(0=禁用,1=正常)")
    private Integer status;

    @Schema(description = "删除标志(0=未删除,1=已删除)")
    private Integer deleted;

    @Schema(description = "微信OpenID")
    @Size(max=28, message="微信OpenID长度不能超过28个字符")
    private String openid;

    @Schema(description = "微信会话密钥")
    @Size(max=32, message="微信会话密钥长度不能超过32个字符")
    private String sessionKey;

    @Schema(description = "国家")
    @Size(max=32, message="国家长度不能超过32个字符")
    private String country;

    @Schema(description = "省份")
    @Size(max=32, message="省份长度不能超过32个字符")
    private String province;

    @Schema(description = "城市")
    @Size(max=32, message="城市长度不能超过32个字符")
    private String city;

    @Schema(description = "语言")
    @Size(max=10, message="语言长度不能超过10个字符")
    private String language;
}
