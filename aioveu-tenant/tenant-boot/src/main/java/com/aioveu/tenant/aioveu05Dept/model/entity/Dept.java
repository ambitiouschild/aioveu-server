package com.aioveu.tenant.aioveu05Dept.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: Dept
 * @Description TODO 部门实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:11
 * @Version 1.0
 **/
@Schema(description = "部门实体对象")
@TableName("sys_dept")
@Getter
@Setter
public class Dept extends BaseEntityWithTenantId {

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门编码
     */
    private String code;

    /**
     * 父节点id
     */
    private Long parentId;

    /**
     * 父节点id路径
     */
    private String treePath;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 状态(1-正常 0-禁用)
     */
    private Integer status;

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
