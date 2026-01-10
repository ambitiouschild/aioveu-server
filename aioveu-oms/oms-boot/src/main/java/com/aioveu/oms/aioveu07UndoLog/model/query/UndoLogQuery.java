package com.aioveu.oms.aioveu07UndoLog.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: UndoLogQuery
 * @Description TODO  AT transaction mode undo table分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:37
 * @Version 1.0
 **/

@Schema(description ="AT transaction mode undo table查询对象")
@Getter
@Setter
public class UndoLogQuery extends BasePageQuery {

    @Schema(description = "分支事务的唯一标识。branch transaction id")
    private Long branchId;
    @Schema(description = "全局事务的唯一标识。global transaction id")
    private String xid;
    @Schema(description = "记录上下文信息 undo_log context,such as serialization")
    private String context;
    @Schema(description = "核心字段。存储序列化后的回滚数据，包含前后镜像（beforeImage和afterImage）。rollback info")
    private byte[] rollbackInfo;
    @Schema(description = "日志状态。0：正常状态；1：全局事务已完成，用于防悬挂（防止第二阶段回滚请求在一阶段日志产生前到达）0:normal status,1:defense status")
    private Integer logStatus;
}
