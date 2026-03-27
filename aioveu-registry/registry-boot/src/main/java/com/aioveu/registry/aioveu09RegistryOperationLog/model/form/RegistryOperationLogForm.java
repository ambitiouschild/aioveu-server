package com.aioveu.registry.aioveu09RegistryOperationLog.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: RegistryOperationLogForm
 * @Description TODO 操作日志表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:25
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "操作日志表单对象")
public class RegistryOperationLogForm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "日志ID")
    private Long id;

    @Schema(description = "租户ID")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @Schema(description = "操作人ID")
    @NotNull(message = "操作人ID不能为空")
    private Long operatorId;

    @Schema(description = "操作人类型：1-系统，2-租户，3-管理员")
    @NotNull(message = "操作人类型：1-系统，2-租户，3-管理员不能为空")
    private Integer operatorType;

    @Schema(description = "操作类型：REGISTER, CERTIFY, FILE, UPDATE, DELETE等")
    @NotBlank(message = "操作类型：REGISTER, CERTIFY, FILE, UPDATE, DELETE等不能为空")
    @Size(max=50, message="操作类型：REGISTER, CERTIFY, FILE, UPDATE, DELETE等长度不能超过50个字符")
    private String operationType;

    @Schema(description = "操作目标：TENANT, APP, CERTIFICATION, FILING等")
    @NotBlank(message = "操作目标：TENANT, APP, CERTIFICATION, FILING等不能为空")
    @Size(max=50, message="操作目标：TENANT, APP, CERTIFICATION, FILING等长度不能超过50个字符")
    private String operationTarget;

    @Schema(description = "目标记录ID")
    private Long targetId;

    @Schema(description = "操作内容（JSON格式）")
    private String operationContent;

    @Schema(description = "操作结果：1-成功，0-失败")
    private Integer operationResult;

    @Schema(description = "错误信息")
    @Size(max=65535, message="错误信息长度不能超过65535个字符")
    private String errorMessage;

    @Schema(description = "操作IP")
    @Size(max=50, message="操作IP长度不能超过50个字符")
    private String ipAddress;

    @Schema(description = "用户代理")
    @Size(max=500, message="用户代理长度不能超过500个字符")
    private String userAgent;
}
