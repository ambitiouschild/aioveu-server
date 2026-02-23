package com.aioveu.tenant.aioveu02User.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName: User
 * @Description TODO 用户实体
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:26
 * @Version 1.0
 **/

@TableName("sys_user")
@Data
public class User extends BaseEntityWithTenantId {

    /**
     * 用户名
     */
    private String username;


    /**
     * 昵称
     */
    private String nickname;


    /**
     * 性别((1-男 2-女 0-保密)
     */
    private Integer gender;

    /**
     * 密码
     */
    private String password;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 联系方式
     */
    private String mobile;

    /**
     * 状态((1-正常 0-禁用)
     */
    private Integer status;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 创建人 ID
     */
    private Long createBy;

    /**
     * 更新人 ID
     */
    private Long updateBy;

    /**
     * 是否删除(0-否 1-是)
     */
    private Integer isDeleted;

    /**
     * 微信 OpenID
     */
    private String openid;
}
