package com.aioveu.pay.aioveu10MqSendRecord.controller;


import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu10MqSendRecord.model.form.MqSendRecordForm;
import com.aioveu.pay.aioveu10MqSendRecord.model.query.MqSendRecordQuery;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.MqSendRecordVo;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
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
 * @ClassName: MqSendRecordController
 * @Description TODO MQ消息发送记录前端控制层
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:50
 * @Version 1.0
 **/
@Tag(name = "MQ消息发送记录接口")
@RestController
@RequestMapping("/api/v1/mq-send-record")
@RequiredArgsConstructor
public class MqSendRecordController {

    private final MqSendRecordService mqSendRecordService;

    @Operation(summary = "MQ消息发送记录分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallPay:mq-send-record:list')")
    public PageResult<MqSendRecordVo> getMqSendRecordPage(MqSendRecordQuery queryParams ) {
        IPage<MqSendRecordVo> result = mqSendRecordService.getMqSendRecordPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增MQ消息发送记录")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallPay:mq-send-record:create')")
    public Result<Void> saveMqSendRecord(@RequestBody @Valid MqSendRecordForm formData ) {
        boolean result = mqSendRecordService.saveMqSendRecord(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取MQ消息发送记录表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallPay:mq-send-record:update')")
    public Result<MqSendRecordForm> getMqSendRecordForm(
            @Parameter(description = "MQ消息发送记录ID") @PathVariable Long id
    ) {
        MqSendRecordForm formData = mqSendRecordService.getMqSendRecordFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改MQ消息发送记录")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPay:mq-send-record:update')")
    public Result<Void> updateMqSendRecord(
            @Parameter(description = "MQ消息发送记录ID") @PathVariable Long id,
            @RequestBody @Validated MqSendRecordForm formData
    ) {
        boolean result = mqSendRecordService.updateMqSendRecord(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除MQ消息发送记录")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallPay:mq-send-record:delete')")
    public Result<Void> deleteMqSendRecords(
            @Parameter(description = "MQ消息发送记录ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = mqSendRecordService.deleteMqSendRecords(ids);
        return Result.judge(result);
    }
}
