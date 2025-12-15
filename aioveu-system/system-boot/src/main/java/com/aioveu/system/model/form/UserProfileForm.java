package com.aioveu.system.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: UserProfileForm
 * @Description TODO  个人中心用户信息
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 16:27
 * @Version 1.0
 **/
@Schema(description = "个人中心用户信息")
@Data
public class UserProfileForm {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

}
