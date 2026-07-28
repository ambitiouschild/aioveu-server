package com.aioveu.oms.aioveu12OrderExportTask.controller.app;


import com.aioveu.common.core.annotation.Log;
import com.aioveu.common.core.enums.LogModuleEnum;
import com.aioveu.common.core.result.Result;
import com.aioveu.oms.aioveu01Order.model.query.OrderExportQuery;
import com.aioveu.oms.aioveu12OrderExportTask.service.OmsOrderExportTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @ClassName: OmsOrderExportTaskController
 * @Description TODO 订单导出任务前端控制层
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 18:27
 * @Version 1.0
 **/
@Slf4j
@Tag(name = "订单导出任务接口")
@RestController
@RequestMapping("/aioveu/api/v8/app/oms/oms-order-export-task")
@RequiredArgsConstructor
public class OmsOrderExportTaskControllerForApp {

    private final OmsOrderExportTaskService omsOrderExportTaskService;



    /**
     * 导出订单（商家后台 / 小程序）
     */
    @Operation(summary ="导出订单（商家后台 / 小程序）")
    @PostMapping("/export")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:statistics')")
    @Log( value = "导出订单（商家后台 / 小程序）",module = LogModuleEnum.OMS)
    public Result<Long> exportOrders(
            @Valid @RequestBody OrderExportQuery query,
            @RequestHeader("Authorization") String token,
            @RequestHeader("X-Client-Id") String clientId
    ) {


        try {
            Long taskId = omsOrderExportTaskService.createExportTask(query, token, clientId);

            return Result.success(taskId);

        } catch (Exception e) {
            log.error("获取订单统计失败：", e);
            return Result.failed("导出失败");
        }
    }


    /**
     * 下载订单（商家后台 / 小程序）
     */
    @Operation(summary ="下载订单（商家后台 / 小程序）")
    @GetMapping("/download")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:statistics')")
    @Log( value = "下载订单（商家后台 / 小程序）",module = LogModuleEnum.OMS)
    public Result<Void> download(
            @RequestParam("exportNo") String exportNo,
            HttpServletResponse response
    )throws IOException {
        boolean result = omsOrderExportTaskService.download(exportNo,response);
        return Result.judge(result);
    }

}
