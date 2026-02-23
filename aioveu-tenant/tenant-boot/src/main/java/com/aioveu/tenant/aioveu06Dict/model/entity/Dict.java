package com.aioveu.tenant.aioveu06Dict.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: Dict
 * @Description TODO 字典实体
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:12
 * @Version 1.0
 **/
@Schema(description = "字典实体")
@TableName("sys_dict")
@Data
public class Dict extends BaseEntity {


    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典名称
     */
    private String name;


    /**
     * 状态（1：启用, 0：停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 逻辑删除标识(0-未删除 1-已删除)
     */
    private Integer isDeleted;
}
