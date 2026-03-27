package com.aioveu.registry.aioveu09RegistryOperationLog.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RegistryOperationLog
 * @Description TODO 操作日志实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:24
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("registry_operation_log")
public class RegistryOperationLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 操作人ID
     */
    private Long operatorId;
    /**
     * 操作人类型：1-系统，2-租户，3-管理员
     */
    private Integer operatorType;
    /**
     * 操作类型：REGISTER, CERTIFY, FILE, UPDATE, DELETE等
     */
    private String operationType;
    /**
     * 操作目标：TENANT, APP, CERTIFICATION, FILING等
     */
    private String operationTarget;
    /**
     * 目标记录ID
     */
    private Long targetId;
    /**
     * 操作内容（JSON格式）
     */
    private String operationContent;
    /**
     * 操作结果：1-成功，0-失败
     */
    private Integer operationResult;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 操作IP
     */
    private String ipAddress;
    /**
     * 用户代理
     */
    private String userAgent;
}
