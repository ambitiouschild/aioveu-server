package com.aioveu.system.aioveu11Codegen.model.bo;

import lombok.Data;

/**
 * @ClassName: TableMetaData
 * @Description TODO  数据表元数据
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:36
 * @Version 1.0
 **/

@Data
public class TableMetaData {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表描述
     */
    private String tableComment;

    /**
     * 排序规则
     */
    private String tableCollation;

    /**
     * 存储引擎
     */
    private String engine;

    /**
     * 字符集
     */
    private String charset;

    /**
     * 创建时间
     */
    private String createTime;
}
