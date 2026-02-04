package com.aioveu.pay.aioveu01PayOrder.model.form;

import java.io.Serial;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import jakarta.validation.constraints.*;


/**
 * @ClassName: PayOrderForm
 * @Description TODO 支付订单表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 17:26
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "支付订单表单对象")
public class PayOrderForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "支付单号，唯一，格式：PAYyyyyMMddHHmmss+6位随机")
    @NotBlank(message = "支付单号，唯一，格式：PAYyyyyMMddHHmmss+6位随机不能为空")
    @Size(max=32, message="支付单号，唯一，格式：PAYyyyyMMddHHmmss+6位随机长度不能超过32个字符")
    private String paymentNo;

    @Schema(description = "业务订单号（如退款单号、订单号）")
    @NotBlank(message = "业务订单号（如退款单号、订单号）不能为空")
    @Size(max=32, message="业务订单号（如退款单号、订单号）长度不能超过32个字符")
    private String orderNo;

    @Schema(description = "业务类型：REFUND-退款 ORDER-订单 RECHARGE-充值")
    @NotBlank(message = "业务类型：REFUND-退款 ORDER-订单 RECHARGE-充值不能为空")
    @Size(max=20, message="业务类型：REFUND-退款 ORDER-订单 RECHARGE-充值长度不能超过20个字符")
    private String bizType;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "支付/退款金额")
    @NotNull(message = "支付/退款金额不能为空")
    private BigDecimal paymentAmount;

    @Schema(description = "支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败 4-已关闭 5-已退款")
    @NotNull(message = "支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败 4-已关闭 5-已退款不能为空")
    private Integer paymentStatus;

    @Schema(description = "支付渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额")
    @NotBlank(message = "支付渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额不能为空")
    @Size(max=20, message="支付渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额长度不能超过20个字符")
    private String paymentChannel;

    @Schema(description = "支付方式：APP-APP支付 H5-H5支付 JSAPI-小程序/公众号 NATIVE-扫码支付")
    @Size(max=20, message="支付方式：APP-APP支付 H5-H5支付 JSAPI-小程序/公众号 NATIVE-扫码支付长度不能超过20个字符")
    private String paymentMethod;

    @Schema(description = "支付时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    @Schema(description = "支付过期时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentExpireTime;

    @Schema(description = "第三方支付单号")
    @Size(max=128, message="第三方支付单号长度不能超过128个字符")
    private String thirdPaymentNo;

    @Schema(description = "第三方交易流水号")
    @Size(max=128, message="第三方交易流水号长度不能超过128个字符")
    private String thirdTransactionNo;

    @Schema(description = "附加数据，JSON格式")
    private String attachData;

    @Schema(description = "异步通知地址")
    @Size(max=500, message="异步通知地址长度不能超过500个字符")
    private String notifyUrl;

    @Schema(description = "同步返回地址")
    @Size(max=500, message="同步返回地址长度不能超过500个字符")
    private String returnUrl;

    @Schema(description = "客户端IP")
    @Size(max=50, message="客户端IP长度不能超过50个字符")
    private String clientIp;

    @Schema(description = "设备信息")
    @Size(max=200, message="设备信息长度不能超过200个字符")
    private String deviceInfo;

    @Schema(description = "订单标题")
    @Size(max=200, message="订单标题长度不能超过200个字符")
    private String subject;

    @Schema(description = "订单描述")
    @Size(max=500, message="订单描述长度不能超过500个字符")
    private String body;

    @Schema(description = "币种，默认人民币")
    @Size(max=3, message="币种，默认人民币长度不能超过3个字符")
    private String currency;

    @Schema(description = "错误代码")
    @Size(max=50, message="错误代码长度不能超过50个字符")
    private String errorCode;

    @Schema(description = "错误信息")
    @Size(max=500, message="错误信息长度不能超过500个字符")
    private String errorMessage;

    @Schema(description = "通知状态：0-未通知 1-通知中 2-通知成功 3-通知失败")
    private Integer notifyStatus;

    @Schema(description = "通知次数")
    private Integer notifyCount;

    @Schema(description = "最后通知时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastNotifyTime;

    @Schema(description = "下次通知时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextNotifyTime;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    @NotNull(message = "逻辑删除：0-未删除 1-已删除不能为空")
    private Integer isDeleted;

    @Schema(description = "创建人")
    @Size(max=64, message="创建人长度不能超过64个字符")
    private String createBy;

    @Schema(description = "更新人")
    @Size(max=64, message="更新人长度不能超过64个字符")
    private String updateBy;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "版本号（用于乐观锁）")
    private Integer version;
}
