package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description 用户角色表
 * @author: 雒世松
 * @date: 2025/2/3 0003 14:37
 */
@Data
@TableName("sport_role_user")
public class RoleUser extends IdEntity {

    @NotBlank(message = "userId can not be null!")
    private String userId;

    @NotBlank(message = "roleCode can not be null!")
    private String roleCode;

    private Long storeId;

    private Long companyId;


}
