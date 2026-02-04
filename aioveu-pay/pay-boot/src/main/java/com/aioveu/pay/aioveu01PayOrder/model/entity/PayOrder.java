package com.aioveu.pay.aioveu01PayOrder.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;  // 必须是java.math.BigDecimal
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: PayOrder
 * @Description TODO  支付订单实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 17:25
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("pay_order")
public class PayOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 支付单号，唯一，格式：PAYyyyyMMddHHmmss+6位随机
     */
    private String paymentNo;
    /**
     * 业务订单号（如退款单号、订单号）
     */
    private String orderNo;
    /**
     * 业务类型：REFUND-退款 ORDER-订单 RECHARGE-充值
     */
    private String bizType;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 支付/退款金额  // 必须是java.math.BigDecimal
     */
    private BigDecimal paymentAmount;
    /**
     * 支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败 4-已关闭 5-已退款
     */
    private Integer paymentStatus;
    /**
     * 支付渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额
     */
    private String paymentChannel;
    /**
     * 支付方式：APP-APP支付 H5-H5支付 JSAPI-小程序/公众号 NATIVE-扫码支付
     */
    private String paymentMethod;
    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;
    /**
     * 支付过期时间
     */
    private LocalDateTime paymentExpireTime;
    /**
     * 第三方支付单号
     */
    private String thirdPaymentNo;
    /**
     * 第三方交易流水号
     */
    private String thirdTransactionNo;
    /**
     * 附加数据，JSON格式
     */
    private String attachData;
    /**
     * 异步通知地址
     */
    private String notifyUrl;
    /**
     * 同步返回地址
     */
    private String returnUrl;
    /**
     * 客户端IP
     */
    private String clientIp;
    /**
     * 设备信息
     */
    private String deviceInfo;
    /**
     * 订单标题
     */
    private String subject;
    /**
     * 订单描述
     */
    private String body;
    /**
     * 币种，默认人民币
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
    /**
     * 版本号（用于乐观锁）
     */
    private Integer version;
}
