package com.aioveu.pay.aioveu05PayConfigAlipay.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: PayConfigAlipayForm
 * @Description TODO 支付宝支付配置表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 17:08
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "支付宝支付配置表单对象")
public class PayConfigAlipayForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "支付配置主表ID")
    @NotNull(message = "支付配置主表ID不能为空")
    private Long configId;

    @Schema(description = "所属租户ID")
    @NotNull(message = "所属租户ID不能为空")
    private Long tenantId;

    @Schema(description = "阿里应用ID")
    @NotBlank(message = "阿里应用ID不能为空")
    @Size(max=100, message="阿里应用ID长度不能超过100个字符")
    private String appId;

    @Schema(description = "应用私钥")
    @NotBlank(message = "应用私钥不能为空")
    @Size(max=65535, message="应用私钥长度不能超过65535个字符")
    private String merchantPrivateKey;

    @Schema(description = "支付宝公钥")
    @NotBlank(message = "支付宝公钥不能为空")
    @Size(max=65535, message="支付宝公钥长度不能超过65535个字符")
    private String alipayPublicKey;

    @Schema(description = "支付宝根证书")
    @Size(max=65535, message="支付宝根证书长度不能超过65535个字符")
    private String alipayRootCert;

    @Schema(description = "应用公钥证书")
    @Size(max=65535, message="应用公钥证书长度不能超过65535个字符")
    private String appCertPublicKey;

    @Schema(description = "异步通知地址")
    @Size(max=500, message="异步通知地址长度不能超过500个字符")
    private String notifyUrl;

    @Schema(description = "同步通知地址")
    @Size(max=500, message="同步通知地址长度不能超过500个字符")
    private String returnUrl;

    @Schema(description = "AES加密密钥")
    @Size(max=100, message="AES加密密钥长度不能超过100个字符")
    private String encryptKey;

    @Schema(description = "签名类型: RSA/RSA2")
    @Size(max=20, message="签名类型: RSA/RSA2长度不能超过20个字符")
    private String signType;

    @Schema(description = "字符编码")
    @Size(max=20, message="字符编码长度不能超过20个字符")
    private String charset;

    @Schema(description = "数据格式")
    @Size(max=20, message="数据格式长度不能超过20个字符")
    private String format;

    @Schema(description = "网关地址")
    @Size(max=200, message="网关地址长度不能超过200个字符")
    private String gatewayUrl;

    @Schema(description = "沙箱网关地址")
    @Size(max=200, message="沙箱网关地址长度不能超过200个字符")
    private String sandboxGatewayUrl;

    @Schema(description = "是否沙箱环境: 0-否, 1-是")
    private Integer sandbox;

    @Schema(description = "加密方式: AES")
    @Size(max=20, message="加密方式: AES长度不能超过20个字符")
    private String encryptType;

    @Schema(description = "连接超时时间(秒)")
    private Integer connectTimeout;

    @Schema(description = "读取超时时间(秒)")
    private Integer readTimeout;

    @Schema(description = "代理主机")
    @Size(max=100, message="代理主机长度不能超过100个字符")
    private String proxyHost;

    @Schema(description = "代理端口")
    private Integer proxyPort;

    @Schema(description = "创建人ID")
    private Long createBy;

    @Schema(description = "更新人ID")
    private Long updateBy;
}
