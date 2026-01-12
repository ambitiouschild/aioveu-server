package com.aioveu.ums.aioveu01Member.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO 会员视图层对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:59
 * @param
 * @return:
 **/

@Schema(description = "会员视图层对象")
@Data
public class MemberVO {

    @Schema(description="会员ID")
    private Long id;

    @Schema(description="会员昵称")
    private String nickName;

    @Schema(description="会员头像地址")
    private String avatarUrl;

    @Schema(description="会员手机号")
    private String mobile;

    @Schema(description="会员余额(单位:分)")
    private Long balance;

}
