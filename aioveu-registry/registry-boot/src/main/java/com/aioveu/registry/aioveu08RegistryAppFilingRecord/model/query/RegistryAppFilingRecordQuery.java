package com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: RegistryAppFilingRecordQuery
 * @Description TODO 小程序备案记录分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:10
 * @Version 1.0
 **/
@Schema(description ="小程序备案记录查询对象")
@Getter
@Setter
public class RegistryAppFilingRecordQuery extends BasePageQuery {

    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "小程序账号ID")
    private Long appAccountId;
    @Schema(description = "备案类型：1-首次备案，2-变更备案，3-注销备案")
    private Integer filingType;
    @Schema(description = "备案状态：0-未备案，1-备案中，2-备案通过，3-备案驳回，4-已注销")
    private Integer filingStatus;
    @Schema(description = "备案编号")
    private String filingNo;
    @Schema(description = "备案主体")
    private String filingSubject;
    @Schema(description = "备案小程序名称")
    private String filingAppName;
    @Schema(description = "备案申请时间")
    private LocalDateTime applyTime;
}
