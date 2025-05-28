package com.aioveu.form;

import com.aioveu.vo.GoodsInfo;
import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/6/21 0021 21:24
 */
@Data
public class SettlementForm {

    private String userId;

    /**
     * 商品信息
     */
    private List<GoodsInfo> goodsInfos;

    /**
     * 优惠券列表
     */
    private List<Long> couponIds;
}
