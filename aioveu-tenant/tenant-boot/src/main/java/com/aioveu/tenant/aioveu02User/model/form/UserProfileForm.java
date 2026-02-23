package com.aioveu.tenant.aioveu02User.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: UserProfileForm
 * @Description TODO 个人中心用户信息
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:40
 * @Version 1.0
 **/
@Schema(description = "个人中心用户信息")
@Data
public class UserProfileForm {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "性别")
    private Integer gender;
}
