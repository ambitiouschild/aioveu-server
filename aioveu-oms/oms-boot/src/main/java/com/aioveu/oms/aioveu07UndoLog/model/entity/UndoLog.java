package com.aioveu.oms.aioveu07UndoLog.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: UndoLog
 * @Description TODO AT transaction mode undo table实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:32
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("undo_log")
public class UndoLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 分支事务的唯一标识。branch transaction id
     */
    private Long branchId;
    /**
     * 全局事务的唯一标识。global transaction id
     */
    private String xid;
    /**
     * 记录上下文信息 undo_log context,such as serialization
     */
    private String context;
    /**
     * 核心字段。存储序列化后的回滚数据，包含前后镜像（beforeImage和afterImage）
     */
    @TableField(value = "rollback_info")
    private byte[] rollbackInfo;  // 方式1：使用byte数组（最常用）
    /**
     * 日志状态。0：正常状态；1：全局事务已完成，用于防悬挂（防止第二阶段回滚请求在一阶段日志产生前到达）0:normal status,1:defense status
     */
    private Integer logStatus;
    /**
     * 记录的创建时间create datetime
     */
    private LocalDateTime logCreated;
    /**
     * 记录的最后修改时间。modify datetime
     */
    private LocalDateTime logModified;
}
