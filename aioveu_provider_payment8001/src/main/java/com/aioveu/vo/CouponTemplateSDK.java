package com.aioveu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <h1>微服务之间用的优惠券模板信息定义</h1>
 * @author: 雒世松
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplateSDK {

    /** 优惠券模板主键 */
    private Long id;

    /** 优惠券模板名称 */
    private String name;

    /** 优惠券 logo */
    private String logo;

    /** 优惠券描述 */
    private String desc;

    /** 优惠券分类 */
    private String category;

    /** 产品线 */
    private Integer productLine;

    /** 优惠券模板的编码 */
    private String key;

    /** 目标用户 */
    private Integer target;

    /**
     * 激活价格
     */
    private BigDecimal activePrice;

    /** 优惠券规则 */
    private TemplateRule rule;

    private Long companyId;

    private Long storeId;

    private String productId;
}
