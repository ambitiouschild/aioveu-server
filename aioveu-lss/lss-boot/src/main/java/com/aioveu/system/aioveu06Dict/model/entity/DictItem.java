package com.aioveu.system.aioveu06Dict.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: DictItem
 * @Description TODO  字典项实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:36
 * @Version 1.0
 **/

@EqualsAndHashCode(callSuper = false)
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
