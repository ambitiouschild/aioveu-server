package com.aioveu.pms.aioveu03CategoryAttribute.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.aioveu.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Description: TODO 商品分类属性/规格
 * @Author: 雒世松
 * @Date: 2025/6/5 18:30
 * @param
 * @return:
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmsCategoryAttribute extends BaseEntity {

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
