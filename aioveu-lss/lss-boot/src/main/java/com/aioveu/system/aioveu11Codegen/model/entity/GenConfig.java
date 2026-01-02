package com.aioveu.system.aioveu11Codegen.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: GenConfig
 * @Description TODO 代码生成基础配置
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:37
 * @Version 1.0
 **/

@TableName(value = "gen_config")
@Getter
@Setter
public class GenConfig extends BaseEntity {

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
}
