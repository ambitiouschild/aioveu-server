package com.aioveu.oms.aioveu03OrderDelivery.utils;


import cn.hutool.core.util.StrUtil;
import com.aioveu.common.enums.oms.LogisticsCompanyCodeEnum;
import com.aioveu.oms.aioveu03OrderDelivery.model.entity.OmsOrderDelivery;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: TODO 订单工具
 * @Author: 雒世松
 * @Date: 2026-07-27 10:38
 * @param
 * @return:
 **/

@Slf4j
public class DeliveryUtils {

    /**
     * 从 delivery 里统一解析出快递公司编码（String）
     */
    public static LogisticsCompanyCodeEnum resolveExpressCode(OmsOrderDelivery delivery) {

        // 优先取枚举的 value
        if (delivery.getDeliveryCompanyCode() != null) {
            return delivery.getDeliveryCompanyCode(); // 改成你枚举实际的方法名
        }

        // 兜底：从公司名称模糊匹配
        if (StrUtil.isNotBlank(delivery.getDeliveryCompany())) {
            return mapCompanyNameToCode(delivery.getDeliveryCompany());
        }

        return null;
    }

    /**
     * 公司名称 → 微信编码 简单映射
     */
    public static LogisticsCompanyCodeEnum mapCompanyNameToCode(String companyName) {
        if (companyName == null) return null;

        // 精确匹配
        switch (companyName.trim()) {
            case "顺丰速运":
            case "顺丰":
                return LogisticsCompanyCodeEnum.SF;
            case "中通快递":
            case "中通":
                return LogisticsCompanyCodeEnum.ZTO;
            case "圆通速递":
            case "圆通":
                return LogisticsCompanyCodeEnum.YTO;
            case "韵达快递":
            case "韵达":
                return LogisticsCompanyCodeEnum.YD;
            case "申通快递":
            case "申通":
                return LogisticsCompanyCodeEnum.STO;
            case "京东物流":
            case "京东":
                return LogisticsCompanyCodeEnum.JD;
            case "EMS":
                return LogisticsCompanyCodeEnum.EMS;
            case "极兔速递":
            case "极兔":
                return LogisticsCompanyCodeEnum.JTSD;
            case "德邦快递":
            case "德邦":
                return LogisticsCompanyCodeEnum.DBLK;
            default:
                log.warn("【发货】未匹配的快递公司名称: {}", companyName);
                return LogisticsCompanyCodeEnum.UNKNOWN;
        }
    }
}
