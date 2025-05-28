package com.aioveu.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.aioveu.vo.PriceRule;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_vip_card")
@Data
public class VipCard extends IdNameEntity {

    private BigDecimal price;

    private Double discount;

    private Long companyId;

    private Long storeId;

    private String categoryCode;

    private String productCategoryCode;

    private Date fixedTime;

    private Integer receiveDay;

    private BigDecimal originalPrice;

    private BigDecimal sellingPrice;

    private BigDecimal storedPrice;

    private BigDecimal giftPrice;

    private String description;

    private String priceRule;

    @TableField(exist = false)
    private List<PriceRule> priceRules;

    public List<PriceRule> getPriceRules() {
        if (priceRules == null) {
            priceRules = new ArrayList<>();
            if (!StringUtils.isEmpty(priceRule)) {
                priceRules = JSONArray.parseArray(priceRule, PriceRule.class);
            }
        }
        return priceRules;
    }

}
