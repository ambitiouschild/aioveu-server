package com.aioveu.tenant.aioveu10Log.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.tenant.aioveu10Log.model.form.LogQuery;
import com.aioveu.tenant.aioveu10Log.model.vo.LogPageVO;
import com.aioveu.tenant.aioveu10Log.service.LogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: LogController
 * @Description TODO 日志控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 14:39
 * @Version 1.0
 **/
@Tag(name = "09.日志接口")
@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(summary = "日志分页列表")
    @GetMapping
    public PageResult<LogPageVO> getLogPage(
            LogQuery queryParams
    ) {
        Page<LogPageVO> result = logService.getLogPage(queryParams);
        return PageResult.success(result);
    }

}
