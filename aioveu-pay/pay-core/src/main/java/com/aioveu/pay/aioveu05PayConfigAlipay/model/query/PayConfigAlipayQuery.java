package com.aioveu.pay.aioveu05PayConfigAlipay.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PayConfigAlipayQuery
 * @Description TODO 支付宝支付配置分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 17:09
 * @Version 1.0
 **/
@Schema(description ="支付宝支付配置查询对象")
@Getter
@Setter
public class PayConfigAlipayQuery extends BasePageQuery {

    @Schema(description = "支付配置主表ID")
    private Long configId;
    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "阿里应用ID")
    private String appId;
    @Schema(description = "支付宝公钥")
    private String alipayPublicKey;
    @Schema(description = "创建人ID")
    private Long createBy;
}
