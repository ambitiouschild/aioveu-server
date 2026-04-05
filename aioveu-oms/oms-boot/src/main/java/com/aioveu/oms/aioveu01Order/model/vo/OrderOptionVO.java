package com.aioveu.oms.aioveu01Order.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: OrderOptionVO
 * @Description TODO 表单选择器搜索项
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/5 18:07
 * @Version 1.0
 **/

@Schema(description ="表单选择器搜索项")
@Data
public class OrderOptionVO {


    /**
     * 订单ID
     */
    private Long id;
    /**
     * 订单号
     */
    private String orderSn;
}
