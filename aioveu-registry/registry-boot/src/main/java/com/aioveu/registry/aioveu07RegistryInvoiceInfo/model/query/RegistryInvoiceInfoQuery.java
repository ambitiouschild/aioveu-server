package com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RegistryInvoiceInfoQuery
 * @Description TODO 发票信息分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:39
 * @Version 1.0
 **/
@Schema(description ="发票信息查询对象")
@Getter
@Setter
public class RegistryInvoiceInfoQuery extends BasePageQuery {

    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "认证记录ID")
    private Long certificationId;
    @Schema(description = "发票类型：1-电子发票，2-纸质发票")
    private Integer invoiceType;
    @Schema(description = "发票抬头")
    private String invoiceTitle;
    @Schema(description = "纳税人识别号")
    private String taxIdentificationNo;
    @Schema(description = "开票状态：0-未开票，1-开票中，2-已开票，3-已寄送")
    private Integer invoiceStatus;
}
