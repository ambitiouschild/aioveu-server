package com.aioveu.data.sync.dp.resp;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/31 10:50
 */
@Data
public class DpOrder {

    private BigDecimal couponValue;

    private String shopName;

    private String shopIdStr;

    /**
     * 接单时候的备注
     */
    private String shopRemark;

    /**
     * 顾客备注 默认 无
     */
    private String userRemark;

    /**
     * 176****0028
     */
    private String encryptedPhone;

    private BigDecimal actualAmount;

    private BigDecimal totalAmount;

    /**
     * confirmDate
     */
    private String confirmDate;

    /**
     * 2025-03-31 11:33:00
     */
    private String paySuccessTime;

    /**
     * 2025-03-31 11:32:58
     */
    private String orderDate;

    /**
     * 2025-04-03\n壁球1号场|08:00-09:00\n
     * 2025-04-04\n壁球2号场|08:00-09:00\n壁球1号场|08:00-09:00\n壁球2号场|09:00-10:00\n
     */
    private String bookStartTime;

    /**
     * 数量 1
     */
    private Integer quantity;

    /**
     * 2 未消费 6 退款成功 5 退款中
     */
    private Integer displayStatus;

    /**
     * 未消费 退款成功 退款中
     */
    private String displayStatusText;

    /**
     * 08:00-09:00
     */
    private String skuTitle;

    /**
     * 壁球
     */
    private String productTitle;

    /**
     * 渠道名称 美团 点评
     */
    private String channelName;

    /**
     * 渠道 2 美团 1点评
     */
    private Integer channel;

    /**
     * 订单id
     */
    private String unifiedOrderId;

    /**
     * 04月03日（周四）08:00~23:45  15.8小时
     */
    private String bookTimeDesc;

    /**
     * 确认时间
     */
    private Long confirmDeadline;

    /**
     * 商家拒单  计划有变，无时间消费
     */
    private String refundReason;

    private String refundTime;

    /**
     * 商家人工审核,备注：商家同意,处理人:13100663772(217336493)
     */
    private String refundAuditor;

    public static void main(String[] args) throws Exception {
        // 2025-03-01 00:00:00 2025-03-31 23:59:59
        String book = "壁球1号场|08:00-09:00";
        String[] fieldSplit = book.split("\\|");
        String fieldName = fieldSplit[0];
        String timeStr = fieldSplit[1];
        System.out.println(fieldName);
        System.out.println(timeStr);
        String[] timeSplit = timeStr.split("-");
        System.out.println(timeSplit[0]);
        System.out.println(timeSplit[1]);
    }

}
