package com.aioveu.pay.aioveu03PayConfigWechat.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PayConfigWechatQuery
 * @Description TODO 微信支付配置分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:14
 * @Version 1.0
 **/
@Schema(description ="微信支付配置查询对象")
@Getter
@Setter
public class PayConfigWechatQuery extends BasePageQuery {

    @Schema(description = "支付配置主表ID")
    private Long configId;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "微信应用ID(公众号/小程序/APP等)")
    private String appId;
    @Schema(description = "微信商户号(10位数字)")
    private String mchId;
    @Schema(description = "商户证书序列号")
    private String mchSerialNo;
    @Schema(description = "微信支付公钥ID")
    private String wechatpayPublicKeyId;
    @Schema(description = "创建人ID")
    private Long createBy;
}
