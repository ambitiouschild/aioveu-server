package com.aioveu.pms.aioveu02Category.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.aioveu.common.base.BaseEntity;
import lombok.Data;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO  商品分类实体对象
 * @Date  2026/1/11 17:27
 * @Param
 * @return
 **/

@Data
@TableName("pms_category")
public class PmsCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * 商品分类名称
     */
    private String name;

    /**
     * 父级ID
     */
    private Long parentId;


    /**
     * 层级
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer level;

    /**
     * 图标地址
     */
    private String iconUrl;

    /**
     * 排序
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer sort;

    /**
     * 显示状态:( 0:隐藏 1:显示)
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer visible;
}
