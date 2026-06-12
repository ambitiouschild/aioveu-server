package com.aioveu.oms.aioveu12OrderExportTask.model.vo;


import com.aioveu.oms.aioveu12OrderExportTask.enums.OrdeExportTaskStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: OmsOrderExportTaskVo
 * @Description TODO 订单导出任务视图对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 18:22
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "订单导出任务视图对象")
public class OmsOrderExportTaskVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单导出任务ID")
    private Long id;
    @Schema(description = "订单导出任务编号")
    private String orderExportTaskNo;
    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "操作员ID")
    private Long operatorId;
    @Schema(description = "客户端ID（小程序/H5/PC）")
    private String clientId;
    @Schema(description = "订单状态：0-待付款 1-已付款 2-已发货 3-已完成 4-已取消 5-售后中")
    private Integer orderStatus;
    @Schema(description = "订单起始时间")
    private LocalDateTime orderStartTime;
    @Schema(description = "订单结束时间")
    private LocalDateTime orderEndTime;
    @Schema(description = "导出格式：excel/csv")
    private String exportFormat;
    @Schema(description = "导出数据总量")
    private Integer totalCount;
    @Schema(description = "任务状态：PENDING/RUNNING/SUCCESS/FAILED")
    private OrdeExportTaskStatusEnum status;
    @Schema(description = "导出文件地址")
    private String fileUrl;
    @Schema(description = "失败原因")
    private String failReason;
    @Schema(description = "任务开始执行时间")
    private LocalDateTime startTimeActual;
    @Schema(description = "任务完成时间")
    private LocalDateTime finishTime;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
