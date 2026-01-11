package com.aioveu.pms.aioveu04CategoryBrand.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description: TODO 商品分类与品牌关联表实体对象
 * @Author: 雒世松
 * @Date: 2026-01-11 20:01
 * @param
 * @return:
 **/

@Data
@TableName("pms_category_brand")
public class PmsCategoryBrand extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品分类ID
     */
    private Long categoryId;

    /**
     * 商品品牌ID
     */
    private Long brandId;

}
