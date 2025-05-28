package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_share_config")
@Data
public class ShareConfig extends IdEntity {

    private Long storeId;

    private Long categoryId;

    private String productId;

    private Long companyId;

    private Double rewardRatio;

    private BigDecimal rewardAmount;

    /**
     * 是否需要有订单
     */
    private Boolean rewardCondition;

    /**
     * 分享类型 0 商品分享 1 海报分享
     */
    private Integer shareType;

}
