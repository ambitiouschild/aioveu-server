package com.aioveu.pay.aioveu01PayOrder.model.vo;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.math.BigDecimal;
/**
 * @ClassName: PayOrderVO
 * @Description TODO 支付订单视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 17:28
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "支付订单视图对象")
public class PayOrderVO implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "支付单号，唯一，格式：PAYyyyyMMddHHmmss+6位随机")
    private String paymentNo;
    @Schema(description = "业务订单号（如退款单号、订单号）")
    private String orderNo;
    @Schema(description = "业务类型：REFUND-退款 ORDER-订单 RECHARGE-充值")
    private String bizType;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "支付/退款金额")
    private BigDecimal paymentAmount;
    @Schema(description = "支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败 4-已关闭 5-已退款")
    private Integer paymentStatus;
    @Schema(description = "支付渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额")
    private String paymentChannel;
    @Schema(description = "支付方式：APP-APP支付 H5-H5支付 JSAPI-小程序/公众号 NATIVE-扫码支付")
    private String paymentMethod;
    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;
    @Schema(description = "支付过期时间")
    private LocalDateTime paymentExpireTime;
    @Schema(description = "第三方支付单号")
    private String thirdPaymentNo;
    @Schema(description = "第三方交易流水号")
    private String thirdTransactionNo;
    @Schema(description = "附加数据，JSON格式")
    private String attachData;
    @Schema(description = "异步通知地址")
    private String notifyUrl;
    @Schema(description = "同步返回地址")
    private String returnUrl;
    @Schema(description = "客户端IP")
    private String clientIp;
    @Schema(description = "设备信息")
    private String deviceInfo;
    @Schema(description = "订单标题")
    private String subject;
    @Schema(description = "订单描述")
    private String body;
    @Schema(description = "币种，默认人民币")
    private String currency;
    @Schema(description = "错误代码")
    private String errorCode;
    @Schema(description = "错误信息")
    private String errorMessage;
    @Schema(description = "通知状态：0-未通知 1-通知中 2-通知成功 3-通知失败")
    private Integer notifyStatus;
    @Schema(description = "通知次数")
    private Integer notifyCount;
    @Schema(description = "最后通知时间")
    private LocalDateTime lastNotifyTime;
    @Schema(description = "下次通知时间")
    private LocalDateTime nextNotifyTime;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建人")
    private String createBy;
    @Schema(description = "更新人")
    private String updateBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "版本号（用于乐观锁）")
    private Integer version;
}
