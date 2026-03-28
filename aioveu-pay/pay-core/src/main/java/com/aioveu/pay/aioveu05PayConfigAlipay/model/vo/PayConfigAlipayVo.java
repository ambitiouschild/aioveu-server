package com.aioveu.pay.aioveu05PayConfigAlipay.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: PayConfigAlipayVo
 * @Description TODO 支付宝支付配置视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 17:09
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "支付宝支付配置视图对象")
public class PayConfigAlipayVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "支付配置主表ID")
    private Long configId;
    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "阿里应用ID")
    private String appId;
    @Schema(description = "应用私钥")
    private String merchantPrivateKey;
    @Schema(description = "支付宝公钥")
    private String alipayPublicKey;
    @Schema(description = "支付宝根证书")
    private String alipayRootCert;
    @Schema(description = "应用公钥证书")
    private String appCertPublicKey;
    @Schema(description = "异步通知地址")
    private String notifyUrl;
    @Schema(description = "同步通知地址")
    private String returnUrl;
    @Schema(description = "AES加密密钥")
    private String encryptKey;
    @Schema(description = "签名类型: RSA/RSA2")
    private String signType;
    @Schema(description = "字符编码")
    private String charset;
    @Schema(description = "数据格式")
    private String format;
    @Schema(description = "网关地址")
    private String gatewayUrl;
    @Schema(description = "沙箱网关地址")
    private String sandboxGatewayUrl;
    @Schema(description = "是否沙箱环境: 0-否, 1-是")
    private Integer sandbox;
    @Schema(description = "加密方式: AES")
    private String encryptType;
    @Schema(description = "连接超时时间(秒)")
    private Integer connectTimeout;
    @Schema(description = "读取超时时间(秒)")
    private Integer readTimeout;
    @Schema(description = "代理主机")
    private String proxyHost;
    @Schema(description = "代理端口")
    private Integer proxyPort;
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
