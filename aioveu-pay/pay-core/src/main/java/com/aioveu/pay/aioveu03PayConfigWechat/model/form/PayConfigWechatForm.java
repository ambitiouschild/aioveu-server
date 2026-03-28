package com.aioveu.pay.aioveu03PayConfigWechat.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: PayConfigWechatForm
 * @Description TODO 微信支付配置表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:13
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "微信支付配置表单对象")
public class PayConfigWechatForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "支付配置主表ID")
    @NotNull(message = "支付配置主表ID不能为空")
    private Long configId;

    @Schema(description = "租户ID")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @Schema(description = "微信应用ID(公众号/小程序/APP等)")
    @NotBlank(message = "微信应用ID(公众号/小程序/APP等)不能为空")
    @Size(max=100, message="微信应用ID(公众号/小程序/APP等)长度不能超过100个字符")
    private String appId;

    @Schema(description = "微信商户号(10位数字)")
    @NotBlank(message = "微信商户号(10位数字)不能为空")
    @Size(max=100, message="微信商户号(10位数字)长度不能超过100个字符")
    private String mchId;

    @Schema(description = "商户API密钥V2")
    @Size(max=128, message="商户API密钥V2长度不能超过128个字符")
    private String mchKey;

    @Schema(description = "APIv3密钥(32位字符)")
    @NotBlank(message = "APIv3密钥(32位字符)不能为空")
    @Size(max=128, message="APIv3密钥(32位字符)长度不能超过128个字符")
    private String apiV3Key;

    @Schema(description = "商户证书序列号")
    @Size(max=200, message="商户证书序列号长度不能超过200个字符")
    private String mchSerialNo;

    @Schema(description = "商户私钥内容(PKCS#8格式)")
    @Size(max=65535, message="商户私钥内容(PKCS#8格式)长度不能超过65535个字符")
    private String privateKey;

    @Schema(description = "商户私钥文件路径")
    @Size(max=500, message="商户私钥文件路径长度不能超过500个字符")
    private String privateKeyPath;

    @Schema(description = "微信支付公钥ID")
    @NotBlank(message = "微信支付公钥ID不能为空")
    @Size(max=200, message="微信支付公钥ID长度不能超过200个字符")
    private String wechatpayPublicKeyId;

    @Schema(description = "微信支付公钥内容")
    @NotBlank(message = "微信支付公钥内容不能为空")
    @Size(max=65535, message="微信支付公钥内容长度不能超过65535个字符")
    private String wechatpayPublicKey;

    @Schema(description = "微信支付公钥文件路径")
    @Size(max=500, message="微信支付公钥文件路径长度不能超过500个字符")
    private String wechatpayPublicKeyPath;

    @Schema(description = "平台证书序列号")
    @Size(max=200, message="平台证书序列号长度不能超过200个字符")
    private String platformCertSerialNo;

    @Schema(description = "平台证书文件路径")
    @Size(max=500, message="平台证书文件路径长度不能超过500个字符")
    private String platformCertPath;

    @Schema(description = "网关地址")
    @Size(max=200, message="网关地址长度不能超过200个字符")
    private String apiDomain;

    @Schema(description = "连接超时时间(秒)")
    private Integer connectTimeout;

    @Schema(description = "读取超时时间(秒)")
    private Integer readTimeout;

    @Schema(description = "支付异步通知地址")
    @Size(max=500, message="支付异步通知地址长度不能超过500个字符")
    private String notifyUrl;

    @Schema(description = "退款异步通知地址")
    @Size(max=500, message="退款异步通知地址长度不能超过500个字符")
    private String refundNotifyUrl;

    @Schema(description = "是否沙箱环境: 0-否, 1-是")
    private Integer sandbox;

    @Schema(description = "签名类型: RSA, HMAC-SHA256")
    @Size(max=20, message="签名类型: RSA, HMAC-SHA256长度不能超过20个字符")
    private String signType;

    @Schema(description = "证书存储方式: FILE-文件, STRING-字符串, CLOUD-云存储")
    @Size(max=20, message="证书存储方式: FILE-文件, STRING-字符串, CLOUD-云存储长度不能超过20个字符")
    private String certStoreType;

    @Schema(description = "是否自动下载平台证书: 0-否, 1-是")
    private Integer autoDownloadCert;

    @Schema(description = "子商户应用ID")
    @Size(max=100, message="子商户应用ID长度不能超过100个字符")
    private String subAppId;

    @Schema(description = "子商户号")
    @Size(max=50, message="子商户号长度不能超过50个字符")
    private String subMchId;

    @Schema(description = "是否支持分账: 0-否, 1-是")
    private Integer profitSharing;

    @Schema(description = "创建人ID")
    private Long createBy;

    @Schema(description = "更新人ID")
    private Long updateBy;
}
