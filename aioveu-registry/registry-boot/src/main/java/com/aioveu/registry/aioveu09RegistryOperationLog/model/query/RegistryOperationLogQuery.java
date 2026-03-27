package com.aioveu.registry.aioveu09RegistryOperationLog.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RegistryOperationLogQuery
 * @Description TODO 操作日志分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:26
 * @Version 1.0
 **/
@Schema(description ="操作日志查询对象")
@Getter
@Setter
public class RegistryOperationLogQuery extends BasePageQuery {

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
    @Schema(description = "用户代理")
    private String userAgent;
}
