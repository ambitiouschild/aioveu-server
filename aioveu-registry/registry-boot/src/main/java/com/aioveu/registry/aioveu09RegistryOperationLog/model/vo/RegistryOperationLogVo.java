package com.aioveu.registry.aioveu09RegistryOperationLog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryOperationLogVo
 * @Description TODO 操作日志视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:27
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "操作日志视图对象")
public class RegistryOperationLogVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "日志ID")
    private Long id;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "操作人ID")
    private Long operatorId;
    @Schema(description = "操作人类型：1-系统，2-租户，3-管理员")
    private Integer operatorType;
    @Schema(description = "操作类型：REGISTER, CERTIFY, FILE, UPDATE, DELETE等")
    private String operationType;
    @Schema(description = "操作目标：TENANT, APP, CERTIFICATION, FILING等")
    private String operationTarget;
    @Schema(description = "目标记录ID")
    private Long targetId;
    @Schema(description = "操作内容（JSON格式）")
    private String operationContent;
    @Schema(description = "操作结果：1-成功，0-失败")
    private Integer operationResult;
    @Schema(description = "错误信息")
    private String errorMessage;
    @Schema(description = "操作IP")
    private String ipAddress;
    @Schema(description = "用户代理")
    private String userAgent;
    @Schema(description = "操作时间")
    private LocalDateTime createTime;
}
