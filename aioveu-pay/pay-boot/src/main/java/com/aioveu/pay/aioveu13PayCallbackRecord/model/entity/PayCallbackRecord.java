package com.aioveu.pay.aioveu13PayCallbackRecord.model.entity;


import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PayCallbackRecord
 * @Description TODO 支付回调记录实体对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/29 18:07
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("pay_callback_record")
public class PayCallbackRecord extends BaseEntityWithTenantId {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID，0表示平台
     */
    private Long tenantId;
    /**
     * 内部支付单号
     */
    private String paymentNo;
    /**
     * 业务订单号
     */
    private String orderNo;
    /**
     * 支付渠道交易号（微信transaction_id / 支付宝trade_no）
     */
    private String transactionId;
    /**
     * 支付渠道：WECHAT / ALIPAY / UNION
     */
    private String channel;
    /**
     * 回调状态：0-接收 1-处理成功 2-处理失败
     */
    private Integer notifyStatus;
    /**
     * 回调次数（防风暴）
     */
    private Integer notifyCount;
    /**
     * 最后一次回调时间
     */
    private LocalDateTime lastNotifyTime;
    /**
     * 实际支付金额
     */
    private BigDecimal paidAmount;
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 原始回调报文（XML / JSON）
     */
    private String rawData;
    /**
     * 处理失败原因
     */
    private String errorMsg;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
