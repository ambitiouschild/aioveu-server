package com.aioveu.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @Description: TODO 用户登录视图对象
 * @Author: 雒世松
 * @Date: 2025/6/5 17:25
 * @param
 * @return:
 **/

@Schema(description ="当前登录用户视图对象")
@Data
public class UserInfoVO {

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="用户昵称")
    private String nickname;

    @Schema(description="头像地址")
    private String avatar;

    @Schema(description="用户角色编码集合")
    private Set<String> roles;

    @Schema(description="用户权限标识集合")
    private Set<String> perms;

}
