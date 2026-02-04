package com.aioveu.pay.aioveu03PayChannelConfig.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PayChannelConfigQuery
 * @Description TODO 支付渠道配置分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 19:54
 * @Version 1.0
 **/

@Schema(description ="支付渠道配置查询对象")
@Getter
@Setter
public class PayChannelConfigQuery extends BasePageQuery {

    @Schema(description = "渠道编码：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联")
    private String channelCode;
    @Schema(description = "渠道名称")
    private String channelName;
    @Schema(description = "渠道类型：ONLINE-线上 OFFLINE-线下")
    private String channelType;
    @Schema(description = "配置类型：CERT-证书 KEY-密钥 URL-地址")
    private String configType;
    @Schema(description = "是否启用：0-禁用 1-启用")
    private Integer isEnabled;
    @Schema(description = "是否默认：0-否 1-是")
    private Integer isDefault;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
}
