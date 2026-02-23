package com.aioveu.tenant.aioveu11Codegen.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.aioveu.tenant.aioveu11Codegen.enums.FormTypeEnum;
import com.aioveu.tenant.aioveu11Codegen.enums.QueryTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @ClassName: GenTableColumn
 * @Description TODO 代码生成表字段配置实体
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:53
 * @Version 1.0
 **/
@TableName(value = "gen_table_column")
@Data
public class GenTableColumn extends BaseEntity {

    /**
     * 关联的表配置ID
     */
    private Long tableId;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * 字段长度
     */
    private Long maxLength;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段排序
     */
    private Integer fieldSort;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 字段描述
     */
    private String fieldComment;

    /**
     * 表单类型
     */
    private FormTypeEnum formType;

    /**
     * 查询方式
     */
    private QueryTypeEnum queryType;

    /**
     * 是否在列表显示
     */
    private Integer isShowInList;

    /**
     * 是否在表单显示
     */
    private Integer isShowInForm;

    /**
     * 是否在查询条件显示
     */
    private Integer isShowInQuery;

    /**
     * 是否必填
     */
    private Integer isRequired;

    /**
     * TypeScript类型
     */
    @TableField(exist = false)
    @JsonIgnore
    private String tsType;

    /**
     * 字典类型
     */
    private String dictType;
}
