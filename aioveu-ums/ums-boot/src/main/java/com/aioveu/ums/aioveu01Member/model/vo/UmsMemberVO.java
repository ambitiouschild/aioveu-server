package com.aioveu.ums.aioveu01Member.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description: TODO 会员视图层对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:59
 * @param
 * @return:
 **/

@Schema(description = "会员视图层对象")
@Data
public class UmsMemberVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会员ID")
    private Long id;
    @Schema(description = "昵称")
    private String nickName;
    @Schema(description = "手机号")
    private String mobile;
    @Schema(description = "头像URL")
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
    private String openid;
    @Schema(description = "微信会话密钥")
    private String sessionKey;
    @Schema(description = "国家")
    private String country;
    @Schema(description = "省份")
    private String province;
    @Schema(description = "城市")
    private String city;
    @Schema(description = "语言")
    private String language;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
