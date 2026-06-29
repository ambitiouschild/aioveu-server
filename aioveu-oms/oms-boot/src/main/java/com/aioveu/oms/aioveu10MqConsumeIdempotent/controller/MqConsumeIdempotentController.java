package com.aioveu.oms.aioveu10MqConsumeIdempotent.controller;


import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.form.MqConsumeIdempotentForm;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.query.MqConsumeIdempotentQuery;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.vo.MqConsumeIdempotentVo;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.service.MqConsumeIdempotentService;
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
 * @ClassName: MqConsumeIdempotentController
 * @Description TODO MQ消费幂等性前端控制层
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/10 18:22
 * @Version 1.0
 **/
@Tag(name = "MQ消费幂等性接口")
@RestController
@RequestMapping("/aioveu/api/v8/admin/oms//mq-consume-idempotent")
@RequiredArgsConstructor
public class MqConsumeIdempotentController {

    private final MqConsumeIdempotentService mqConsumeIdempotentService;

    @Operation(summary = "MQ消费幂等性分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqConsumeIdempotent:mq-consume-idempotent:list')")
    public PageResult<MqConsumeIdempotentVo> getMqConsumeIdempotentPage(MqConsumeIdempotentQuery queryParams ) {
        IPage<MqConsumeIdempotentVo> result = mqConsumeIdempotentService.getMqConsumeIdempotentPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增MQ消费幂等性")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqConsumeIdempotent:mq-consume-idempotent:create')")
    public Result<Void> saveMqConsumeIdempotent(@RequestBody @Valid MqConsumeIdempotentForm formData ) {
        boolean result = mqConsumeIdempotentService.saveMqConsumeIdempotent(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取MQ消费幂等性表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqConsumeIdempotent:mq-consume-idempotent:update')")
    public Result<MqConsumeIdempotentForm> getMqConsumeIdempotentForm(
            @Parameter(description = "MQ消费幂等性ID") @PathVariable Long id
    ) {
        MqConsumeIdempotentForm formData = mqConsumeIdempotentService.getMqConsumeIdempotentFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改MQ消费幂等性")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqConsumeIdempotent:mq-consume-idempotent:update')")
    public Result<Void> updateMqConsumeIdempotent(
            @Parameter(description = "MQ消费幂等性ID") @PathVariable Long id,
            @RequestBody @Validated MqConsumeIdempotentForm formData
    ) {
        boolean result = mqConsumeIdempotentService.updateMqConsumeIdempotent(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除MQ消费幂等性")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqConsumeIdempotent:mq-consume-idempotent:delete')")
    public Result<Void> deleteMqConsumeIdempotents(
            @Parameter(description = "MQ消费幂等性ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = mqConsumeIdempotentService.deleteMqConsumeIdempotents(ids);
        return Result.judge(result);
    }
}
