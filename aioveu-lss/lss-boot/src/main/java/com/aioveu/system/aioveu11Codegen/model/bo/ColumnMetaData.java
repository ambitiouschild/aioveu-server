package com.aioveu.system.aioveu11Codegen.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: ColumnMetaData
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:36
 * @Version 1.0
 **/

@Schema(description = "数据表字段VO")
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
