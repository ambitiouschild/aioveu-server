package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: xiaoyao
 */
@TableName("sport_store_product_category")
@Data
public class StoreProductCategory extends IdEntity{

    /**
     * 分类店铺
     */
    @NotNull(message = "商店Id不能为空")
    private Long storeId;

    /**
     * 分类Id
     */
    @NotNull(message = "分类Id不能为空")
    private Long categoryId;

}
