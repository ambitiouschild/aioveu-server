package com.aioveu.pay.aioveu02PayConfig.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PayConfigQuery
 * @Description TODO 支付配置主表分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:04
 * @Version 1.0
 **/
@Schema(description ="支付配置主表查询对象")
@Getter
@Setter
public class PayConfigQuery extends BasePageQuery {

    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "配置编码（唯一标识）")
    private String configCode;
    @Schema(description = "配置名称")
    private String configName;
    @Schema(description = "支付平台类型: WECHAT-微信支付, ALIPAY-支付宝, DUMMY-模拟支付")
    private String platformType;
    @Schema(description = "支付类型: APP-APP支付, JSAPI-公众号支付, NATIVE-扫码支付, MWEB-H5支付, MINIPROGRAM-小程序支付, FACE-刷脸支付")
    private String payType;
    @Schema(description = "是否启用: 0-禁用, 1-启用")
    private Integer enabled;
    @Schema(description = "是否默认配置: 0-否, 1-是")
    private Integer isDefault;
}
