package com.aioveu.pay.aioveu03PayConfigWechat.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: PayConfigWechatVo
 * @Description TODO 微信支付配置视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:14
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "微信支付配置视图对象")
public class PayConfigWechatVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "支付配置主表ID")
    private Long configId;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "微信应用ID(公众号/小程序/APP等)")
    private String appId;
    @Schema(description = "微信商户号(10位数字)")
    private String mchId;
    @Schema(description = "商户API密钥V2")
    private String mchKey;
    @Schema(description = "APIv3密钥(32位字符)")
    private String apiV3Key;
    @Schema(description = "商户证书序列号")
    private String mchSerialNo;
    @Schema(description = "商户私钥内容(PKCS#8格式)")
    private String privateKey;
    @Schema(description = "商户私钥文件路径")
    private String privateKeyPath;
    @Schema(description = "微信支付公钥ID")
    private String wechatpayPublicKeyId;
    @Schema(description = "微信支付公钥内容")
    private String wechatpayPublicKey;
    @Schema(description = "微信支付公钥文件路径")
    private String wechatpayPublicKeyPath;
    @Schema(description = "平台证书序列号")
    private String platformCertSerialNo;
    @Schema(description = "平台证书文件路径")
    private String platformCertPath;
    @Schema(description = "网关地址")
    private String apiDomain;
    @Schema(description = "连接超时时间(秒)")
    private Integer connectTimeout;
    @Schema(description = "读取超时时间(秒)")
    private Integer readTimeout;
    @Schema(description = "支付异步通知地址")
    private String notifyUrl;
    @Schema(description = "退款异步通知地址")
    private String refundNotifyUrl;
    @Schema(description = "是否沙箱环境: 0-否, 1-是")
    private Integer sandbox;
    @Schema(description = "签名类型: RSA, HMAC-SHA256")
    private String signType;
    @Schema(description = "证书存储方式: FILE-文件, STRING-字符串, CLOUD-云存储")
    private String certStoreType;
    @Schema(description = "是否自动下载平台证书: 0-否, 1-是")
    private Integer autoDownloadCert;
    @Schema(description = "子商户应用ID")
    private String subAppId;
    @Schema(description = "子商户号")
    private String subMchId;
    @Schema(description = "是否支持分账: 0-否, 1-是")
    private Integer profitSharing;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "创建人ID")
    private Long createBy;
    @Schema(description = "更新人ID")
    private Long updateBy;
}
