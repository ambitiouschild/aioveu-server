package com.aioveu.oms.aioveu09MqDeadLetter.controller;


import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu09MqDeadLetter.model.form.MqDeadLetterForm;
import com.aioveu.oms.aioveu09MqDeadLetter.model.query.MqDeadLetterQuery;
import com.aioveu.oms.aioveu09MqDeadLetter.model.vo.MqDeadLetterVo;
import com.aioveu.oms.aioveu09MqDeadLetter.service.MqDeadLetterService;
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
 * @ClassName: MqDeadLetterController
 * @Description TODO MQ死信队列前端控制层
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:56
 * @Version 1.0
 **/
@Tag(name = "MQ死信队列接口")
@RestController
@RequestMapping("/aioveu/api/v8/admin/oms//mq-dead-letter")
@RequiredArgsConstructor
public class MqDeadLetterController {

    private final MqDeadLetterService mqDeadLetterService;

    @Operation(summary = "MQ死信队列分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqDeadLetter:mq-dead-letter:list')")
    public PageResult<MqDeadLetterVo> getMqDeadLetterPage(MqDeadLetterQuery queryParams ) {
        IPage<MqDeadLetterVo> result = mqDeadLetterService.getMqDeadLetterPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增MQ死信队列")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqDeadLetter:mq-dead-letter:create')")
    public Result<Void> saveMqDeadLetter(@RequestBody @Valid MqDeadLetterForm formData ) {
        boolean result = mqDeadLetterService.saveMqDeadLetter(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取MQ死信队列表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqDeadLetter:mq-dead-letter:update')")
    public Result<MqDeadLetterForm> getMqDeadLetterForm(
            @Parameter(description = "MQ死信队列ID") @PathVariable Long id
    ) {
        MqDeadLetterForm formData = mqDeadLetterService.getMqDeadLetterFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改MQ死信队列")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqDeadLetter:mq-dead-letter:update')")
    public Result<Void> updateMqDeadLetter(
            @Parameter(description = "MQ死信队列ID") @PathVariable Long id,
            @RequestBody @Validated MqDeadLetterForm formData
    ) {
        boolean result = mqDeadLetterService.updateMqDeadLetter(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除MQ死信队列")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqDeadLetter:mq-dead-letter:delete')")
    public Result<Void> deleteMqDeadLetters(
            @Parameter(description = "MQ死信队列ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = mqDeadLetterService.deleteMqDeadLetters(ids);
        return Result.judge(result);
    }
}
