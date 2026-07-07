package com.aioveu.ums.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;

/**
 * @Description: TODO 会员传输层对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:53
 * @param
 * @return:
 **/

@Data
public class MemberRegisterDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会员ID")
    private Long id;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "状态(0=禁用,1=正常)")
    private Integer status;


    @Schema(description = "微信OpenID")
    private String openId;

    /**
     * 租户ID
     */
    private Long tenantId;

}
