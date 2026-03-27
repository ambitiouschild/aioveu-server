package com.aioveu.registry.aioveu05RegistryCertification.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: RegistryCertificationQuery
 * @Description TODO 认证记录分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:14
 * @Version 1.0
 **/
@Schema(description ="认证记录查询对象")
@Getter
@Setter
public class RegistryCertificationQuery extends BasePageQuery {

    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "小程序账号ID")
    private Long appAccountId;
    @Schema(description = "认证状态：0-未开始，1-申请中，2-审核中，3-审核通过，4-审核失败，5-已过期")
    private Integer certificationStatus;
    @Schema(description = "审核时间")
    private LocalDateTime auditTime;
}
