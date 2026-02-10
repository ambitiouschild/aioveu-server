package com.aioveu.pay.aioveu07PayNotify.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: PayNotify
 * @Description TODO 支付通知实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 15:54
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("pay_notify")
public class PayNotify extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 通知编号
     */
    private String notifyNo;
    /**
     * 支付单号
     */
    private String paymentNo;
    /**
     * 退款单号
     */
    private String refundNo;
    /**
     * 通知类型：PAYMENT-支付 REFUND-退款
     */
    private String notifyType;
    /**
     * 通知地址
     */
    private String notifyUrl;
    /**
     * 请求数据
     */
    private String requestData;
    /**
     * 响应数据
     */
    private String responseData;
    /**
     * 通知状态：0-待通知 1-通知中 2-通知成功 3-通知失败
     */
    private Integer notifyStatus;
    /**
     * 通知次数
     */
    private Integer notifyCount;
    /**
     * 最大通知次数
     */
    private Integer maxNotifyCount;
    /**
     * 下次通知时间
     */
    private LocalDateTime nextNotifyTime;
    /**
     * 最后通知时间
     */
    private LocalDateTime lastNotifyTime;
    /**
     * 成功时间
     */
    private LocalDateTime successTime;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
