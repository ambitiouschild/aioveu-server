package com.aioveu.oms.aioveu08MqConsumeRecord.controller;


import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.form.MqConsumeRecordForm;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.query.MqConsumeRecordQuery;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.vo.MqConsumeRecordVo;
import com.aioveu.oms.aioveu08MqConsumeRecord.service.MqConsumeRecordService;
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
 * @ClassName: MqConsumeRecordController
 * @Description TODO MQ消息消费记录前端控制层
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:37
 * @Version 1.0
 **/
@Tag(name = "MQ消息消费记录接口")
@RestController
@RequestMapping("/api/v1/mq-consume-record")
@RequiredArgsConstructor
public class MqConsumeRecordController {

    private final MqConsumeRecordService mqConsumeRecordService;

    @Operation(summary = "MQ消息消费记录分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqConsumeRecord:mq-consume-record:list')")
    public PageResult<MqConsumeRecordVo> getMqConsumeRecordPage(MqConsumeRecordQuery queryParams ) {
        IPage<MqConsumeRecordVo> result = mqConsumeRecordService.getMqConsumeRecordPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增MQ消息消费记录")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqConsumeRecord:mq-consume-record:create')")
    public Result<Void> saveMqConsumeRecord(@RequestBody @Valid MqConsumeRecordForm formData ) {
        boolean result = mqConsumeRecordService.saveMqConsumeRecord(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取MQ消息消费记录表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqConsumeRecord:mq-consume-record:update')")
    public Result<MqConsumeRecordForm> getMqConsumeRecordForm(
            @Parameter(description = "MQ消息消费记录ID") @PathVariable Long id
    ) {
        MqConsumeRecordForm formData = mqConsumeRecordService.getMqConsumeRecordFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改MQ消息消费记录")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqConsumeRecord:mq-consume-record:update')")
    public Result<Void> updateMqConsumeRecord(
            @Parameter(description = "MQ消息消费记录ID") @PathVariable Long id,
            @RequestBody @Validated MqConsumeRecordForm formData
    ) {
        boolean result = mqConsumeRecordService.updateMqConsumeRecord(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除MQ消息消费记录")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallOmsMqConsumeRecord:mq-consume-record:delete')")
    public Result<Void> deleteMqConsumeRecords(
            @Parameter(description = "MQ消息消费记录ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = mqConsumeRecordService.deleteMqConsumeRecords(ids);
        return Result.judge(result);
    }
}
