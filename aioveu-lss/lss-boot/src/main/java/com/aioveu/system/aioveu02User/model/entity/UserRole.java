package com.aioveu.system.aioveu02User.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: UserRole
 * @Description TODO   用户和角色关联表
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:19
 * @Version 1.0
 **/

@TableName("sys_user_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
