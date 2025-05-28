package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/6/7 17:16
 */
@TableName("sport_poster")
@Data
public class Poster extends IdNameEntity{

    /**
     * 海报 跳转的 商品分类
     */
    private Long categoryId;

    private Long storeId;

    private String url;

    /**
     * 海报 跳转 商品id 用于去详情页
     */
    private String productId;

    private Long rewardCategoryId;

    private String rewardProductId;

    private Long rewardCompanyId;
}
