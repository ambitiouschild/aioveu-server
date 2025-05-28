package com.aioveu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.aioveu.enums.CouponCategory;
import com.aioveu.enums.DistributeTarget;
import com.aioveu.enums.ProductLine;
import com.aioveu.coupon.serialization.CouponTemplateSerialize;
import com.aioveu.vo.TemplateRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h1>优惠券模板实体类定义: 基础属性 + 规则属性</h1>
 * @author: 雒世松
 */
@TableName(autoResultMap = true, value = "sport_coupon_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = CouponTemplateSerialize.class)
public class CouponTemplate extends IdNameEntity {

    /** 是否是可用状态 */
    private Boolean available;

    /** 是否过期 */
    private Boolean expired;

    /** 优惠券名称 */
    private String name;

    /** 优惠券 logo */
    private String logo;

    /** 优惠券描述 */
    private String intro;

    /** 优惠券分类 */
    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    private CouponCategory category;

    /** 产品线 */
    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    private ProductLine productLine;

    /** 产品id
     * 当 分类==优惠券，即 productLine=2 时，产品id才可配置值
     * productLine=2，产品id为空时，表示不限制，所有产品下单都可使用该卡券
     * productLine=2，产品id有值时，表示限定下单某个产品才可使用该卡券*/
    private String productId;

    /**
     *  总数 -1 表示不限制数量
     *  */
    private Integer couponCount;

    /**
     * 激活价格
     */
    private BigDecimal activePrice;

    private Long companyId;

    private Long storeId;

    /** 创建用户 */
    private String userId;

    /** 优惠券模板的编码 */
    private String templateKey;

    /** 目标用户 */
    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    private DistributeTarget target;

    /** 优惠券规则 */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private TemplateRule rule;

    /** 是否显示在店铺中 **/
    private Integer showStore;

    /**
     * 会员价可用
     * 为true，正常使用
     * 为false，选择会员卡，使用会员价的时候，不可使用该卡券叠加优惠
     */
    private Integer vipPriceCanUse;

    /**
     * <h2>自定义构造函数</h2>
     * */
    public CouponTemplate(String name, String logo, String intro, String category,
                          Integer productLine, Integer couponCount, String userId,
                          Integer target, TemplateRule rule, Integer showStore) {

        this.available = false;
        this.expired = false;
        this.name = name;
        this.logo = logo;
        this.intro = intro;
        this.category = CouponCategory.of(category);
        this.productLine = ProductLine.of(productLine);
        this.couponCount = couponCount;
        this.userId = userId;
        // 优惠券模板唯一编码 = 4(产品线和类型) + 8(日期: 20190101) + id(扩充为4位)
        this.templateKey = productLine.toString() + category +
                new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.target = DistributeTarget.of(target);
        this.rule = rule;
        this.showStore = showStore;
    }
}
