package com.aioveu.tenant.aioveu06Dict.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: DictItem
 * @Description TODO 字典项实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:13
 * @Version 1.0
 **/
@Schema(description = "字典项实体对象")
@TableName("sys_dict_item")
@Data
public class DictItem extends BaseEntity {

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典项名称
     */
    private String label;

    /**
     * 字典项值
     */
    private String value;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态（1-正常，0-禁用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 标签类型
     */
    private String tagType;
}
