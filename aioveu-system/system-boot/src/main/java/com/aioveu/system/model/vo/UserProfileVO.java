package com.aioveu.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @Description: TODO 个人中心用户视图对象
 * @Author: 雒世松
 * @Date: 2025/6/5 17:26
 * @param
 * @return:
 **/

@Schema(description ="个人中心用户视图对象")
@Data
public class UserProfileVO {

    @Schema(description="用户ID")
    private Long id;

    @Schema(description="登录账号")
    private String username;

    @Schema(description="用户昵称")
    private String nickname;

    @Schema(description="手机号码")
    private String mobile;

    @Schema(description="头像地址")
    private String avatar;

    @Schema(description="用户角色名称集合")
    private Set<String> roleNames;

    @Schema(description="部门名称")
    private String deptName;

    @Schema(description="邮箱")
    private String email;

    @Schema(description="性别")
    private String genderLabel;

}
