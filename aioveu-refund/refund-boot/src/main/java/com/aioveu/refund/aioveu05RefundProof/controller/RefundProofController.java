package com.aioveu.refund.aioveu05RefundProof.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.refund.aioveu05RefundProof.model.form.RefundProofForm;
import com.aioveu.refund.aioveu05RefundProof.model.query.RefundProofQuery;
import com.aioveu.refund.aioveu05RefundProof.model.vo.RefundProofVO;
import com.aioveu.refund.aioveu05RefundProof.service.RefundProofService;
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
 * @ClassName: RefundProofController
 * @Description TODO 退款凭证图片前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:09
 * @Version 1.0
 **/

@Tag(name = "退款凭证图片接口")
@RestController
@RequestMapping("/api/v1/refund-proof")
@RequiredArgsConstructor
public class RefundProofController {

    private final RefundProofService refundProofService;

    @Operation(summary = "退款凭证图片分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundProof:refund-proof:query')")
    public PageResult<RefundProofVO> getRefundProofPage(RefundProofQuery queryParams ) {
        IPage<RefundProofVO> result = refundProofService.getRefundProofPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增退款凭证图片")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundProof:refund-proof:add')")
    public Result<Void> saveRefundProof(@RequestBody @Valid RefundProofForm formData ) {
        boolean result = refundProofService.saveRefundProof(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取退款凭证图片表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundProof:refund-proof:edit')")
    public Result<RefundProofForm> getRefundProofForm(
            @Parameter(description = "退款凭证图片ID") @PathVariable Long id
    ) {
        RefundProofForm formData = refundProofService.getRefundProofFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改退款凭证图片")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundProof:refund-proof:edit')")
    public Result<Void> updateRefundProof(
            @Parameter(description = "退款凭证图片ID") @PathVariable Long id,
            @RequestBody @Validated RefundProofForm formData
    ) {
        boolean result = refundProofService.updateRefundProof(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除退款凭证图片")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundProof:refund-proof:delete')")
    public Result<Void> deleteRefundProofs(
            @Parameter(description = "退款凭证图片ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = refundProofService.deleteRefundProofs(ids);
        return Result.judge(result);
    }
}
