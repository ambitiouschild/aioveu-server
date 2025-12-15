package com.aioveu.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @ClassName: CurrentUserDTO
 * @Description TODO  当前登录用户对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/15 16:13
 * @Version 1.0
 **/

@Schema(description ="当前登录用户对象")
@Data
public class CurrentUserDTO {

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="用户名")
    private String username;

    @Schema(description="用户昵称")
    private String nickname;

    @Schema(description="头像地址")
    private String avatar;

    @Schema(description="用户角色编码集合")
    private Set<String> roles;

    @Schema(description="用户权限标识集合")
    private Set<String> perms;

}
