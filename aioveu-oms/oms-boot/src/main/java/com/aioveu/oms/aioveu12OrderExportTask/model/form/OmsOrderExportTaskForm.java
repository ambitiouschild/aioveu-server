package com.aioveu.oms.aioveu12OrderExportTask.model.form;


import com.aioveu.oms.aioveu12OrderExportTask.enums.OrdeExportTaskStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: OmsOrderExportTaskForm
 * @Description TODO 订单导出任务表单对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 18:20
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "订单导出任务表单对象")
public class OmsOrderExportTaskForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单导出任务ID")
    private Long id;

    @Schema(description = "订单导出任务编号")
    @NotBlank(message = "订单导出任务编号不能为空")
    @Size(max=64, message="订单导出任务编号长度不能超过64个字符")
    private String orderExportTaskNo;

    @Schema(description = "租户ID，0表示平台默认")
    @NotNull(message = "租户ID，0表示平台默认不能为空")
    private Long tenantId;

    @Schema(description = "操作员ID")
    @NotBlank(message = "操作员ID不能为空")
    @Size(max=64, message="操作员ID长度不能超过64个字符")
    private Long operatorId;

    @Schema(description = "客户端ID（小程序/H5/PC）")
    @NotBlank(message = "客户端ID（小程序/H5/PC）不能为空")
    @Size(max=64, message="客户端ID（小程序/H5/PC）长度不能超过64个字符")
    private String clientId;

    @Schema(description = "订单状态：0-待付款 1-已付款 2-已发货 3-已完成 4-已取消 5-售后中")
    @NotNull(message = "订单状态：0-待付款 1-已付款 2-已发货 3-已完成 4-已取消 5-售后中不能为空")
    private Integer orderStatus;

    @Schema(description = "订单起始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderStartTime;

    @Schema(description = "订单结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderEndTime;

    @Schema(description = "导出格式：excel/csv")
    @Size(max=16, message="导出格式：excel/csv长度不能超过16个字符")
    private String exportFormat;

    @Schema(description = "导出数据总量")
    private Integer totalCount;

    @Schema(description = "任务状态：PENDING/RUNNING/SUCCESS/FAILED")
    @Size(max=32, message="任务状态：PENDING/RUNNING/SUCCESS/FAILED长度不能超过32个字符")
    private OrdeExportTaskStatusEnum status;

    @Schema(description = "导出文件地址")
    @Size(max=255, message="导出文件地址长度不能超过255个字符")
    private String fileUrl;

    @Schema(description = "失败原因")
    @Size(max=512, message="失败原因长度不能超过512个字符")
    private String failReason;

    @Schema(description = "任务开始执行时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTimeActual;

    @Schema(description = "任务完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;
}
