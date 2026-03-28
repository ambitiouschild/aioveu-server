package com.aioveu.pay.aioveu03PayConfigWechat.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PayConfigWechat
 * @Description TODO 微信支付配置实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:13
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("pay_config_wechat")
public class PayConfigWechat extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 支付配置主表ID
     */
    private Long configId;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 微信应用ID(公众号/小程序/APP等)
     */
    private String appId;
    /**
     * 微信商户号(10位数字)
     */
    private String mchId;
    /**
     * 商户API密钥V2
     */
    private String mchKey;
    /**
     * APIv3密钥(32位字符)
     */
    private String apiV3Key;
    /**
     * 商户证书序列号
     */
    private String mchSerialNo;
    /**
     * 商户私钥内容(PKCS#8格式)
     */
    private String privateKey;
    /**
     * 商户私钥文件路径
     */
    private String privateKeyPath;
    /**
     * 微信支付公钥ID
     */
    private String wechatpayPublicKeyId;
    /**
     * 微信支付公钥内容
     */
    private String wechatpayPublicKey;
    /**
     * 微信支付公钥文件路径
     */
    private String wechatpayPublicKeyPath;
    /**
     * 平台证书序列号
     */
    private String platformCertSerialNo;
    /**
     * 平台证书文件路径
     */
    private String platformCertPath;
    /**
     * 网关地址
     */
    private String apiDomain;
    /**
     * 连接超时时间(秒)
     */
    private Integer connectTimeout;
    /**
     * 读取超时时间(秒)
     */
    private Integer readTimeout;
    /**
     * 支付异步通知地址
     */
    private String notifyUrl;
    /**
     * 退款异步通知地址
     */
    private String refundNotifyUrl;
    /**
     * 是否沙箱环境: 0-否, 1-是
     */
    private Integer sandbox;
    /**
     * 签名类型: RSA, HMAC-SHA256
     */
    private String signType;
    /**
     * 证书存储方式: FILE-文件, STRING-字符串, CLOUD-云存储
     */
    private String certStoreType;
    /**
     * 是否自动下载平台证书: 0-否, 1-是
     */
    private Integer autoDownloadCert;
    /**
     * 子商户应用ID
     */
    private String subAppId;
    /**
     * 子商户号
     */
    private String subMchId;
    /**
     * 是否支持分账: 0-否, 1-是
     */
    private Integer profitSharing;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
    /**
     * 创建人ID
     */
    private Long createBy;
    /**
     * 更新人ID
     */
    private Long updateBy;
}
