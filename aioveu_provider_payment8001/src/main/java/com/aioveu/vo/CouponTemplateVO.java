package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/21 0021 21:41
 */
@Data
public class CouponTemplateVO {

    private Long id;

    /** 优惠券名称 */
    private String name;

    /** 优惠券 logo */
    private String logo;

    /** 优惠券描述 */
    private String desc;

    /** 优惠券分类 */
    private String category;

    /** 产品线 */
    private Integer productLine;
    /** 产品id
     * 当 分类==优惠券，即 productLine=2 时，产品id才可配置值
     * productLine=2，产品id为空时，表示不限制，所有产品下单都可使用该卡券
     * productLine=2，产品id有值时，表示限定下单某个产品才可使用该卡券*/
    private String productId;
    /** 总数 */
    private Integer count;

    /** 创建用户 */
    private String userId;

    /** 目标用户 */
    private Integer target;

    /**
     * 公司id
     */
    private Long companyId;

    private String companyName;

    /**
     * 店铺id
     */
    private Long storeId;

    /** 优惠券规则 */
    private TemplateRule rule;


    private Integer showStore;

    /**
     * 会员价可用
     * 为true，正常使用
     * 为false，选择会员卡，使用会员价的时候，不可使用该卡券叠加优惠
     */
    private Integer vipPriceCanUse;

}
