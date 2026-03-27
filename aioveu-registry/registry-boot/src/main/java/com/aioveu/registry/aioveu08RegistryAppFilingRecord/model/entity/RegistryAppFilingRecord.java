package com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: RegistryAppFilingRecord
 * @Description TODO 小程序备案记录实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:08
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("registry_app_filing_record")
public class RegistryAppFilingRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 所属租户ID
     */
    private Long tenantId;
    /**
     * 小程序账号ID
     */
    private Long appAccountId;
    /**
     * 备案类型：1-首次备案，2-变更备案，3-注销备案
     */
    private Integer filingType;
    /**
     * 备案状态：0-未备案，1-备案中，2-备案通过，3-备案驳回，4-已注销
     */
    private Integer filingStatus;
    /**
     * 备案编号
     */
    private String filingNo;
    /**
     * 备案主体
     */
    private String filingSubject;
    /**
     * 备案小程序名称
     */
    private String filingAppName;
    /**
     * 备案域名
     */
    private String filingDomain;
    /**
     * 备案IP
     */
    private String filingIp;
    /**
     * 备案证书路径
     */
    private String filingCertificatePath;
    /**
     * 备案申请时间
     */
    private LocalDateTime applyTime;
    /**
     * 备案审核时间
     */
    private LocalDateTime auditTime;
    /**
     * 备案驳回原因
     */
    private String rejectionReason;
    /**
     * 备案到期时间
     */
    private LocalDateTime expireTime;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
