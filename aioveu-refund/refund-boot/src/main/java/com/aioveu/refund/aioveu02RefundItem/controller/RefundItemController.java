package com.aioveu.refund.aioveu02RefundItem.controller;

import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.refund.aioveu02RefundItem.model.form.RefundItemForm;
import com.aioveu.refund.aioveu02RefundItem.model.query.RefundItemQuery;
import com.aioveu.refund.aioveu02RefundItem.model.vo.RefundItemVO;
import com.aioveu.refund.aioveu02RefundItem.service.RefundItemService;
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
 * @ClassName: RefundItemController
 * @Description TODO  退款商品明细前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 17:05
 * @Version 1.0
 **/

@Tag(name = "退款商品明细接口")
@RestController
@RequestMapping("/api/v1/refund-item")
@RequiredArgsConstructor
public class RefundItemController {

    private final RefundItemService refundItemService;

    @Operation(summary = "退款商品明细分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundItem:refund-item:query')")
    @Log(value = "退款商品明细分页列表", module = LogModuleEnum.REFUND)
    public PageResult<RefundItemVO> getRefundItemPage(RefundItemQuery queryParams ) {
        IPage<RefundItemVO> result = refundItemService.getRefundItemPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增退款商品明细")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundItem:refund-item:add')")
    public Result<Void> saveRefundItem(@RequestBody @Valid RefundItemForm formData ) {
        boolean result = refundItemService.saveRefundItem(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取退款商品明细表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundItem:refund-item:edit')")
    public Result<RefundItemForm> getRefundItemForm(
            @Parameter(description = "退款商品明细ID") @PathVariable Long id
    ) {
        RefundItemForm formData = refundItemService.getRefundItemFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改退款商品明细")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundItem:refund-item:edit')")
    public Result<Void> updateRefundItem(
            @Parameter(description = "退款商品明细ID") @PathVariable Long id,
            @RequestBody @Validated RefundItemForm formData
    ) {
        boolean result = refundItemService.updateRefundItem(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除退款商品明细")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallRefundItem:refund-item:delete')")
    public Result<Void> deleteRefundItems(
            @Parameter(description = "退款商品明细ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = refundItemService.deleteRefundItems(ids);
        return Result.judge(result);
    }
}
