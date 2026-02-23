package com.aioveu.tenant.aioveu02User.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: UserRole
 * @Description TODO 用户和角色关联表
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:27
 * @Version 1.0
 **/
@TableName("sys_user_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    public UserRole(Long userId,Long roleId){
        this.userId = userId;
        this.roleId = roleId;
    }

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 租户ID（多租户模式）
     */
    @TableField("tenant_id")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Long tenantId;
}
