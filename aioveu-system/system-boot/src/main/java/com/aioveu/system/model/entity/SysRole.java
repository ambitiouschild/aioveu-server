package com.aioveu.system.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.aioveu.common.base.BaseEntity;
import lombok.Data;

/**
 * @Description: TODO 角色表
 * @Author: 雒世松
 * @Date: 2025/6/5 17:21
 * @param
 * @return:
 **/

@TableName(value ="sys_role")
@Data
public class SysRole extends BaseEntity {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
     * 角色状态(1-正常；0-停用)
     */
    private Integer status;

    /**
     * 逻辑删除标识(0-未删除；1-已删除)
     */
    private Integer deleted;

    /**
     * 数据权限
     */
    private Integer dataScope;
}