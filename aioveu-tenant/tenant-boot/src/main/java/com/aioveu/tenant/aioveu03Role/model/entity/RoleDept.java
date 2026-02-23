package com.aioveu.tenant.aioveu03Role.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: RoleDept
 * @Description TODO 角色部门关联实体
 *                   * <p>
 *                   * 用于存储角色自定义数据权限时，可访问的部门ID列表
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:21
 * @Version 1.0
 **/
@TableName("sys_role_dept")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDept {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;
}
