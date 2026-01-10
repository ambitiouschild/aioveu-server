package com.aioveu.oms.aioveu07UndoLog.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: UndoLogForm
 * @Description TODO  AT transaction mode undo table表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:36
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "AT transaction mode undo table表单对象")
public class UndoLogForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "分支事务的唯一标识。branch transaction id")
    @NotNull(message = "分支事务的唯一标识。branch transaction id不能为空")
    private Long branchId;

    @Schema(description = "全局事务的唯一标识。global transaction id")
    @NotBlank(message = "全局事务的唯一标识。global transaction id不能为空")
    @Size(max=100, message="全局事务的唯一标识。global transaction id长度不能超过100个字符")
    private String xid;

    @Schema(description = "记录上下文信息 undo_log context,such as serialization")
    @NotBlank(message = "记录上下文信息 undo_log context,such as serialization不能为空")
    @Size(max=128, message="记录上下文信息 undo_log context,such as serialization长度不能超过128个字符")
    private String context;

    @Schema(description = "核心字段。存储序列化后的回滚数据，包含前后镜像（beforeImage和afterImage）。rollback info")
    @NotNull(message = "核心字段。存储序列化后的回滚数据，包含前后镜像（beforeImage和afterImage）。rollback info不能为空")
    @Size(max=-1, message="核心字段。存储序列化后的回滚数据，包含前后镜像（beforeImage和afterImage）。rollback info长度不能超过-1个字符")
    private byte[] rollbackInfo;

    @Schema(description = "日志状态。0：正常状态；1：全局事务已完成，用于防悬挂（防止第二阶段回滚请求在一阶段日志产生前到达）0:normal status,1:defense status")
    @NotNull(message = "日志状态。0：正常状态；1：全局事务已完成，用于防悬挂（防止第二阶段回滚请求在一阶段日志产生前到达）0:normal status,1:defense status不能为空")
    private Integer logStatus;
}
