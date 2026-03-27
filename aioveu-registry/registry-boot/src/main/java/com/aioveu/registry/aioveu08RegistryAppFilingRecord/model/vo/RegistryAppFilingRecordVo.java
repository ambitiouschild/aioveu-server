package com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryAppFilingRecordVo
 * @Description TODO 小程序备案记录视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:10
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "小程序备案记录视图对象")
public class RegistryAppFilingRecordVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "备案ID")
    private Long id;
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
    @Schema(description = "备案域名")
    private String filingDomain;
    @Schema(description = "备案IP")
    private String filingIp;
    @Schema(description = "备案证书路径")
    private String filingCertificatePath;
    @Schema(description = "备案申请时间")
    private LocalDateTime applyTime;
    @Schema(description = "备案审核时间")
    private LocalDateTime auditTime;
    @Schema(description = "备案驳回原因")
    private String rejectionReason;
    @Schema(description = "备案到期时间")
    private LocalDateTime expireTime;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
