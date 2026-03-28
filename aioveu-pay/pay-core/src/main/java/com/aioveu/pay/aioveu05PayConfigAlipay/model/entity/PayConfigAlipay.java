package com.aioveu.pay.aioveu05PayConfigAlipay.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PayConfigAlipay
 * @Description TODO 支付宝支付配置实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 17:07
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("pay_config_alipay")
public class PayConfigAlipay extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 支付配置主表ID
     */
    private Long configId;
    /**
     * 所属租户ID
     */
    private Long tenantId;
    /**
     * 阿里应用ID
     */
    private String appId;
    /**
     * 应用私钥
     */
    private String merchantPrivateKey;
    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;
    /**
     * 支付宝根证书
     */
    private String alipayRootCert;
    /**
     * 应用公钥证书
     */
    private String appCertPublicKey;
    /**
     * 异步通知地址
     */
    private String notifyUrl;
    /**
     * 同步通知地址
     */
    private String returnUrl;
    /**
     * AES加密密钥
     */
    private String encryptKey;
    /**
     * 签名类型: RSA/RSA2
     */
    private String signType;
    /**
     * 字符编码
     */
    private String charset;
    /**
     * 数据格式
     */
    private String format;
    /**
     * 网关地址
     */
    private String gatewayUrl;
    /**
     * 沙箱网关地址
     */
    private String sandboxGatewayUrl;
    /**
     * 是否沙箱环境: 0-否, 1-是
     */
    private Integer sandbox;
    /**
     * 加密方式: AES
     */
    private String encryptType;
    /**
     * 连接超时时间(秒)
     */
    private Integer connectTimeout;
    /**
     * 读取超时时间(秒)
     */
    private Integer readTimeout;
    /**
     * 代理主机
     */
    private String proxyHost;
    /**
     * 代理端口
     */
    private Integer proxyPort;
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
