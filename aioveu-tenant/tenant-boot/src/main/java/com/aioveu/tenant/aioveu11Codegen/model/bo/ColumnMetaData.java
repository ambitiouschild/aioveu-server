package com.aioveu.tenant.aioveu11Codegen.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: ColumnMetaData
 * @Description TODO 数据表字段Vo
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 22:00
 * @Version 1.0
 **/
@Schema(description = "数据表字段Vo")
@Data
public class ColumnMetaData {

    /**
     * 字段名称
     */
    private String columnName;

    /**
     * 字段类型
     */
    private String dataType;

    /**
     * 字段描述
     */
    private String columnComment;

    /**
     * 字段长度
     */
    private Long characterMaximumLength;

    /**
     * 是否主键(1-是 0-否)
     */
    private Integer isPrimaryKey;

    /**
     * 是否可为空(1-是 0-否)
     */
    private String isNullable;

    /**
     * 字符集
     */
    private String characterSetName;

    /**
     * 排序规则
     */
    private String collationName;
}
