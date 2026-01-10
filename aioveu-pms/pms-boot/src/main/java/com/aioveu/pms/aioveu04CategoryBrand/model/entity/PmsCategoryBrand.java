package com.aioveu.pms.aioveu04CategoryBrand.model.entity;

import com.aioveu.common.base.BaseEntity;
import lombok.Data;

/**
 * @Description: TODO 分类品牌
 * @Author: 雒世松
 * @Date: 2025/6/5 18:30
 * @param
 * @return:
 **/

@Data
public class PmsCategoryBrand extends BaseEntity {
    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 品牌ID
     */
    private Long brandId;

}
