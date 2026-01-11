package com.aioveu.pms.aioveu03CategoryAttribute.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品分类类型（规格，属性）实体对象
 * @Date  2026/1/11 19:39
 * @Param
 * @return
 **/


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_category_attribute")
public class PmsCategoryAttribute extends BaseEntity {


    private static final long serialVersionUID = 1L;

    @TableId(type=IdType.AUTO)
    private Long id;

    /**
     * 商品分类ID
     */
    private Long categoryId;

    /**
     * 属性/规格名称
     */
    private String name;

    /**
     * 类型(1:规格;2:属性;)
     */
    private Integer type;
}
