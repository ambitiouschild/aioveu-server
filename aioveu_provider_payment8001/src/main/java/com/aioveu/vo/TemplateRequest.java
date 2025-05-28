package com.aioveu.vo;

import com.aioveu.enums.CouponCategory;
import com.aioveu.enums.DistributeTarget;
import com.aioveu.enums.ProductLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <h1>优惠券模板创建请求对象</h1>
 * @author: 雒世松
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRequest {

    private Long id;

    /** 优惠券名称 */
    @NotEmpty(message = "名称不能为空")
    private String name;

    /** 优惠券 logo */
    private String logo;

    /** 优惠券描述 */
    private String desc;

    /**
     * 激活价格
     */
    private BigDecimal activePrice;

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
    @NotNull(message = "公司id不能为空")
    private Long companyId;

    /**
     * 店铺id
     */
    @NotNull(message = "店铺id不能为空")
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

    /**
     * <h2>校验对象的合法性</h2>
     * */
    public boolean validate() {

        boolean stringValid = StringUtils.isNotEmpty(name)
                && StringUtils.isNotEmpty(logo)
                && StringUtils.isNotEmpty(desc);
        boolean enumValid = null != CouponCategory.of(category)
                && null != ProductLine.of(productLine)
                && null != DistributeTarget.of(target);
        boolean numValid = userId != null;

        return stringValid && enumValid && numValid && rule.validate();
    }
}
