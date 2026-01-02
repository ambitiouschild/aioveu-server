package com.aioveu.system.aioveu08Config.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: Config
 * @Description TODO  系统配置对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:34
 * @Version 1.0
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "系统配置")
@TableName("sys_config")
public class Config extends BaseEntity {

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置键
     */
    private String configKey;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 描述、备注
     */
    private String remark;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 逻辑删除标识(0-未删除 1-已删除)
     */
    private Integer isDeleted;
}
