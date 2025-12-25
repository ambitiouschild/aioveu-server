package com.aioveu.common.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @ClassName: OnlineUser
 * @Description TODO 在线用户信息对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/16 16:18
 * @Version 1.0
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUser {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 数据权限范围
     * <p>定义用户可访问的数据范围，如全部、本部门或自定义范围</p>
     */
    private Integer dataScope;

    /**
     * 角色权限集合
     */
    private Set<String> roles;
}
