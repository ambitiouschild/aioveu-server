package com.aioveu.oms.aioveu12OrderExportTask.model.entity;


import com.aioveu.common.base.BaseEntityWithTenantId;
import com.aioveu.oms.aioveu12OrderExportTask.enums.OrdeExportTaskStatusEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: OmsOrderExportTask
 * @Description TODO 订单导出任务实体对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 18:20
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("oms_order_export_task")
public class OmsOrderExportTask extends BaseEntityWithTenantId {

    private static final long serialVersionUID = 1L;

    /**
     * 订单导出任务编号
     */
    private String orderExportTaskNo;
    /**
     * 租户ID，0表示平台默认
     */
    private Long tenantId;
    /**
     * 操作员ID
     */
    private Long operatorId;
    /**
     * 客户端ID（小程序/H5/PC）
     */
    private String clientId;
    /**
     * 订单状态：0-待付款 1-已付款 2-已发货 3-已完成 4-已取消 5-售后中
     */
    private Integer orderStatus;
    /**
     * 订单起始时间
     */
    private LocalDateTime orderStartTime;
    /**
     * 订单结束时间
     */
    private LocalDateTime orderEndTime;
    /**
     * 导出格式：excel/csv
     */
    private String exportFormat;
    /**
     * 导出数据总量
     */
    private Integer totalCount;
    /**
     * 任务状态：PENDING/RUNNING/SUCCESS/FAILED
     */
    private OrdeExportTaskStatusEnum status;
    /**
     * 导出文件地址
     */
    private String fileUrl;
    /**
     * 失败原因
     */
    private String failReason;
    /**
     * 任务开始执行时间
     */
    private LocalDateTime startTimeActual;
    /**
     * 任务完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
