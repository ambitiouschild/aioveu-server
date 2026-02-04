package com.aioveu.pay.aioveu02PayRefundRecord.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PayRefundRecord
 * @Description TODO 退款记录实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 18:46
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("pay_refund_record")
public class PayRefundRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 退款单号，格式：RFyyyyMMddHHmmss+6位随机
     */
    private String refundNo;
    /**
     * 原支付单号
     */
    private String paymentNo;
    /**
     * 业务订单号
     */
    private String orderNo;
    /**
     * 业务类型：REFUND-退款 ORDER-订单退款
     */
    private String bizType;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 退款状态：0-待退款 1-退款中 2-退款成功 3-退款失败 4-已关闭
     */
    private Integer refundStatus;
    /**
     * 退款渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额
     */
    private String refundChannel;
    /**
     * 退款原因
     */
    private String refundReason;
    /**
     * 申请时间
     */
    private LocalDateTime applyTime;
    /**
     * 退款完成时间
     */
    private LocalDateTime refundTime;
    /**
     * 第三方退款单号
     */
    private String thirdRefundNo;
    /**
     * 第三方退款流水号
     */
    private String thirdTransactionNo;
    /**
     * 币种
     */
    private String currency;
    /**
     * 错误代码
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 通知状态：0-未通知 1-通知中 2-通知成功 3-通知失败
     */
    private Integer notifyStatus;
    /**
     * 通知次数
     */
    private Integer notifyCount;
    /**
     * 最后通知时间
     */
    private LocalDateTime lastNotifyTime;
    /**
     * 下次通知时间
     */
    private LocalDateTime nextNotifyTime;
    /**
     * 回调数据
     */
    private String callbackData;
    /**
     * 回调时间
     */
    private LocalDateTime callbackTime;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新人
     */
    private String updateBy;
}
