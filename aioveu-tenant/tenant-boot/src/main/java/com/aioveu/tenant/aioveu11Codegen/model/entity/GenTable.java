package com.aioveu.tenant.aioveu11Codegen.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName: GenTable
 * @Description TODO 代码生成表配置
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:52
 * @Version 1.0
 **/
@TableName(value = "gen_table")
@Data
public class GenTable extends BaseEntity {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 模块名
     */
    private String moduleName;

    /**
     * 实体类名
     */
    private String entityName;

    /**
     * 业务名
     */
    private String businessName;

    /**
     * 父菜单ID
     */
    private Long parentMenuId;

    /**
     * 作者
     */
    private String author;

    /**
     * 页面类型 classic|curd
     */
    private String pageType;

    /**
     * 要移除的表前缀，如: sys_
     */
    private String removeTablePrefix;
}
