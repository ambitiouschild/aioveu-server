package com.aioveu.tenant.aioveu03Role.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName: Role
 * @Description TODO 角色实体
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:21
 * @Version 1.0
 **/
@TableName("sys_role")
@Data
public class Role extends BaseEntityWithTenantId {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 角色状态(1-正常 0-停用)
     */
    private Integer status;

    /**
     * 数据权限
     */
    private Integer dataScope;

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
}
