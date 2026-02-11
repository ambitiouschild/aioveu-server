package com.aioveu.pay.aioveuModule.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: PaymentParamsVO
 * @Description TODO 支付参数VO - 用于前端调起支付
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:47
 * @Version 1.0
 **/

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "支付参数响应对象")
public class PaymentParamsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 基础信息 ====================
    @Schema(description = "支付单号(系统内部)", example = "P202502110001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String paymentNo;

    @Schema(description = "商户订单号", example = "202502110001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;

    @Schema(description = "支付渠道: wechat-微信 alipay-支付宝 balance-余额",
            example = "wechat", requiredMode = Schema.RequiredMode.REQUIRED)
    private String channel;

    @Schema(description = "支付金额(元)", example = "100.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    @Schema(description = "商品标题", example = "iPhone 15 Pro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String subject;

    @Schema(description = "商品描述", example = "苹果手机 iPhone 15 Pro 256GB")
    private String body;

    @Schema(description = "是否成功(true-成功 false-失败)", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean success;

    @Schema(description = "返回消息", example = "支付参数生成成功")
    private String message;

    @Schema(description = "错误码", example = "SUCCESS")
    private String errorCode;

    @Schema(description = "错误信息", example = "成功")
    private String errorMessage;

    @Schema(description = "创建时间戳(毫秒)", example = "1739257200000")
    private Long createTime;

    @Schema(description = "过期时间(分钟)", example = "30")
    private Integer expireMinutes;

    // ==================== 支付参数字段 ====================
    @Schema(description = "支付参数(JSON字符串或特定格式字符串)",
            example = "{\"appId\":\"wx123456\",\"timeStamp\":\"1739257200\"}")
    private String paymentParams;

    @Schema(description = "第三方支付单号", example = "4200002009202402111234567890")
    private String thirdPaymentNo;

    @Schema(description = "商户交易号", example = "M202502110001")
    private String tradeNo;

    // ==================== 微信支付特定参数 ====================
    @Schema(description = "应用ID(微信)", example = "wx1234567890abcdef")
    private String appId;

    @Schema(description = "商户号(微信)", example = "1234567890")
    private String mchId;

    @Schema(description = "时间戳(微信)", example = "1739257200")
    private String timeStamp;

    @Schema(description = "随机字符串(微信)", example = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS")
    private String nonceStr;

    @Schema(description = "订单详情扩展字符串(微信)", example = "prepay_id=wx202502110001")
    private String packageValue;

    @Schema(description = "签名方式(微信)", example = "RSA")
    private String signType;

    @Schema(description = "签名(微信)", example = "C380BEC2BFD727A4B6845133519F3AD6")
    private String paySign;

    @Schema(description = "预支付交易会话ID(微信)", example = "wx202502110001")
    private String prepayId;

    // ==================== 支付宝特定参数 ====================
    @Schema(description = "支付宝应用ID", example = "2021003123456789")
    private String aliAppId;

    @Schema(description = "支付宝交易号", example = "202502110001123456789")
    private String aliTradeNo;

    @Schema(description = "商户订单号(支付宝)", example = "202502110001")
    private String outTradeNo;

    @Schema(description = "支付宝订单信息", example = "app_id=2021003123456789&biz_content={...}")
    private String orderInfo;

    // ==================== 展示方式参数 ====================
    @Schema(description = "二维码URL(仅Native支付使用)", example = "https://api.weixin.qq.com/v3/pay/qrcode")
    private String qrCodeUrl;

    @Schema(description = "H5支付URL", example = "https://pay.weixin.qq.com/h5/pay")
    private String h5Url;

    @Schema(description = "小程序支付参数", example = "{\"package\":\"prepay_id=wx202502110001\"}")
    private String miniProgramParams;

    @Schema(description = "APP支付参数", example = "alipay_sdk=alipay-sdk-java")
    private String appParams;

    @Schema(description = "网页支付URL", example = "https://mapi.alipay.com/gateway.do")
    private String webUrl;

    // ==================== 状态字段 ====================
    @Schema(description = "支付状态: INITIAL-初始化 PAYING-支付中 SUCCESS-成功 FAILED-失败",
            example = "INITIAL")
    private String payStatus;

    // ==================== 其他字段 ====================
    @Schema(description = "用户ID", example = "100001")
    private Long userId;

    @Schema(description = "商户号", example = "MERCHANT001")
    private String merchantId;

    @Schema(description = "设备信息", example = "WEB")
    private String deviceInfo;

    // ==================== 业务字段 ====================
    @Schema(description = "业务标识", example = "ORDER_PAY")
    private String bizType;

    @Schema(description = "业务ID", example = "ORDER_202502110001")
    private String bizId;

    @Schema(description = "附加数据", example = "{\"userId\":100001}")
    private String attach;

    @Schema(description = "通知地址", example = "https://api.example.com/pay/notify")
    private String notifyUrl;

    @Schema(description = "前端回跳地址", example = "https://example.com/pay/success")
    private String returnUrl;          // H5支付URL
}
