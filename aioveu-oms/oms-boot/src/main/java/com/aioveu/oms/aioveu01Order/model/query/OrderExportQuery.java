package com.aioveu.oms.aioveu01Order.model.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @ClassName: OrderExportQuery
 * @Description TODO 订单导出查询对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 17:11
 * @Version 1.0
 **/

@Schema(description ="订单导出查询对象")
@Data
public class OrderExportQuery {

    @Schema(description = "导出任务ID")
    private Long exportId;

    @Schema(description = "订单状态")
    private Integer orderStatus;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "租户ID（平台管理员必填）")
    private Long tenantId;

    @Schema(description = "操作人ID（自动填充）")
    private Long operatorId;

    @Schema(description = "导出格式：excel / csv")
    private String exportFormat = "excel";

    @Schema(description = "页码（分页导出）")
    private Integer pageNum = 1;

    @Schema(description = "每页条数")
    private Integer pageSize = 1000;
}
