package com.aioveu.pay.aioveu11MqCompensationTask.controller;


import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu11MqCompensationTask.model.form.MqCompensationTaskForm;
import com.aioveu.pay.aioveu11MqCompensationTask.model.query.MqCompensationTaskQuery;
import com.aioveu.pay.aioveu11MqCompensationTask.model.vo.MqCompensationTaskVo;
import com.aioveu.pay.aioveu11MqCompensationTask.service.MqCompensationTaskService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: MqCompensationTaskController
 * @Description TODO MQ补偿任务前端控制层
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 22:55
 * @Version 1.0
 **/
@Tag(name = "MQ补偿任务接口")
@RestController
@RequestMapping("/aioveu/api/v8/admin/pay/mq-compensation-task")
@RequiredArgsConstructor
public class MqCompensationTaskController {

    private final MqCompensationTaskService mqCompensationTaskService;

    @Operation(summary = "MQ补偿任务分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayMqCompensationTask:mq-compensation-task:list')")
    public PageResult<MqCompensationTaskVo> getMqCompensationTaskPage(MqCompensationTaskQuery queryParams ) {
        IPage<MqCompensationTaskVo> result = mqCompensationTaskService.getMqCompensationTaskPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增MQ补偿任务")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPayMqCompensationTask:mq-compensation-task:create')")
    public Result<Void> saveMqCompensationTask(@RequestBody @Valid MqCompensationTaskForm formData ) {
        boolean result = mqCompensationTaskService.saveMqCompensationTask(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取MQ补偿任务表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayMqCompensationTask:mq-compensation-task:update')")
    public Result<MqCompensationTaskForm> getMqCompensationTaskForm(
            @Parameter(description = "MQ补偿任务ID") @PathVariable Long id
    ) {
        MqCompensationTaskForm formData = mqCompensationTaskService.getMqCompensationTaskFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改MQ补偿任务")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayMqCompensationTask:mq-compensation-task:update')")
    public Result<Void> updateMqCompensationTask(
            @Parameter(description = "MQ补偿任务ID") @PathVariable Long id,
            @RequestBody @Validated MqCompensationTaskForm formData
    ) {
        boolean result = mqCompensationTaskService.updateMqCompensationTask(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除MQ补偿任务")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPayMqCompensationTask:mq-compensation-task:delete')")
    public Result<Void> deleteMqCompensationTasks(
            @Parameter(description = "MQ补偿任务ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = mqCompensationTaskService.deleteMqCompensationTasks(ids);
        return Result.judge(result);
    }
}
