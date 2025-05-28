package com.aioveu.coupon.executor.impl;

import com.aioveu.coupon.executor.AbstractExecutor;
import com.aioveu.coupon.executor.RuleExecutor;
import com.aioveu.enums.RuleFlag;
import com.aioveu.vo.CouponTemplateSDK;
import com.aioveu.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.print.Doc;

/**
 * @description <h1>折扣优惠券结算规则执行器</h1>
 * @author: 雒世松
 * @date: 2025/2/2 0002 17:34
 */
@Slf4j
@Component
public class ZheKouExecutor extends AbstractExecutor implements RuleExecutor {

    /**
     * <h2>规则类型标记</h2>
     *
     * @return {@link RuleFlag}
     */
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.ZHEKOU;
    }

    /**
     * <h2>优惠券规则的计算</h2>
     *
     * @param settlement {@link SettlementInfo} 包含了选择的优惠券
     * @return {@link SettlementInfo} 修正过的结算信息
     */
    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {
        double goodsSum = retain2Decimals(
                goodsCostSum(settlement.getGoodsInfos())
        );
        SettlementInfo probability = processGoodsTypeNotSatisfy(
                settlement, goodsSum
        );
        if (null != probability) {
            log.debug("ZheKou Template Is Not Match To GoodsType!");
            return probability;
        }
        // 折扣优惠券可以直接使用, 没有门槛
        CouponTemplateSDK templateSDK = settlement.getCouponAndTemplateInfos()
                .get(0).getTemplate();

        double zheKou = templateSDK.getRule().getDiscount().getQuota() * 1.0 / 100;

        // 计算使用优惠券之后的价格 - 结算
        settlement.setCost(retain2Decimals(
                (goodsSum * zheKou) > minCost() ? (goodsSum * zheKou) : minCost()
        ));
        Double maxDisAmt = templateSDK.getRule().getDiscount().getMaxDisAmt();
        if (maxDisAmt != null && maxDisAmt.compareTo(0.0) > 0) {
            Double couponCost = goodsSum - settlement.getCost();
            if (couponCost.compareTo(maxDisAmt) > 0) {
                settlement.setCost(goodsSum - maxDisAmt);
            }
        }
        log.debug("Use ZheKou Coupon Make Goods Cost From {} To {}",
                goodsSum, settlement.getCost());
        return settlement;
    }
}
