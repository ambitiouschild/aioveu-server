package com.aioveu.pay.aioveu04PayConfigDummy.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PayConfigDummyQuery
 * @Description TODO 模拟支付配置分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:29
 * @Version 1.0
 **/
@Schema(description ="模拟支付配置查询对象")
@Getter
@Setter
public class PayConfigDummyQuery extends BasePageQuery {

    @Schema(description = "支付配置主表ID")
    private Long configId;
    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "支付成功率(0-100)")
    private Integer successRate;
}
