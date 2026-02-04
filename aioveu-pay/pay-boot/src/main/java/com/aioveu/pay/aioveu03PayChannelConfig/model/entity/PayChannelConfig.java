package com.aioveu.pay.aioveu03PayChannelConfig.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PayChannelConfig
 * @Description TODO 支付渠道配置实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 19:52
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("pay_channel_config")
public class PayChannelConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 渠道编码：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联
     */
    private String channelCode;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 渠道类型：ONLINE-线上 OFFLINE-线下
     */
    private String channelType;
    /**
     * 配置键
     */
    private String configKey;
    /**
     * 配置值
     */
    private String configValue;
    /**
     * 配置类型：CERT-证书 KEY-密钥 URL-地址
     */
    private String configType;
    /**
     * 配置描述
     */
    private String configDesc;
    /**
     * 是否启用：0-禁用 1-启用
     */
    private Integer isEnabled;
    /**
     * 是否默认：0-否 1-是
     */
    private Integer isDefault;
    /**
     * 优先级，数值越大优先级越高
     */
    private Integer priority;
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
}
