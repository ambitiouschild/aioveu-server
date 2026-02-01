package com.aioveu.refund.aioveu03RefundDelivery.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.refund.aioveu03RefundDelivery.model.form.RefundDeliveryForm;
import com.aioveu.refund.aioveu03RefundDelivery.model.query.RefundDeliveryQuery;
import com.aioveu.refund.aioveu03RefundDelivery.model.vo.RefundDeliveryVO;
import com.aioveu.refund.aioveu03RefundDelivery.service.RefundDeliveryService;
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
 * @ClassName: RefundDeliveryController
 * @Description TODO  退款物流信息（用于退货）前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:08
 * @Version 1.0
 **/

@Tag(name = "退款物流信息（用于退货）接口")
@RestController
@RequestMapping("/api/v1/refund-delivery")
@RequiredArgsConstructor
public class RefundDeliveryController {

    private final RefundDeliveryService refundDeliveryService;

    @Operation(summary = "退款物流信息（用于退货）分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:query')")
    public PageResult<RefundDeliveryVO> getRefundDeliveryPage(RefundDeliveryQuery queryParams ) {
        IPage<RefundDeliveryVO> result = refundDeliveryService.getRefundDeliveryPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增退款物流信息（用于退货）")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:add')")
    public Result<Void> saveRefundDelivery(@RequestBody @Valid RefundDeliveryForm formData ) {
        boolean result = refundDeliveryService.saveRefundDelivery(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取退款物流信息（用于退货）表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:edit')")
    public Result<RefundDeliveryForm> getRefundDeliveryForm(
            @Parameter(description = "退款物流信息（用于退货）ID") @PathVariable Long id
    ) {
        RefundDeliveryForm formData = refundDeliveryService.getRefundDeliveryFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改退款物流信息（用于退货）")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:edit')")
    public Result<Void> updateRefundDelivery(
            @Parameter(description = "退款物流信息（用于退货）ID") @PathVariable Long id,
            @RequestBody @Validated RefundDeliveryForm formData
    ) {
        boolean result = refundDeliveryService.updateRefundDelivery(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除退款物流信息（用于退货）")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundDelivery:refund-delivery:delete')")
    public Result<Void> deleteRefundDeliverys(
            @Parameter(description = "退款物流信息（用于退货）ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = refundDeliveryService.deleteRefundDeliverys(ids);
        return Result.judge(result);
    }

}
