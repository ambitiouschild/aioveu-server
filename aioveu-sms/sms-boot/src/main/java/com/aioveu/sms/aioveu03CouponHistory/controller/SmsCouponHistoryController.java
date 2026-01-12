package com.aioveu.sms.aioveu03CouponHistory.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.sms.aioveu03CouponHistory.model.form.SmsCouponHistoryForm;
import com.aioveu.sms.aioveu03CouponHistory.model.query.SmsCouponHistoryQuery;
import com.aioveu.sms.aioveu03CouponHistory.model.vo.SmsCouponHistoryVO;
import com.aioveu.sms.aioveu03CouponHistory.service.SmsCouponHistoryService;
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
 * @ClassName: SmsCouponHistoryController
 * @Description TODO 优惠券领取/使用记录前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 12:01
 * @Version 1.0
 **/

@Tag(name = "优惠券领取/使用记录接口")
@RestController
@RequestMapping("/api/v1/sms-coupon-history")
@RequiredArgsConstructor
public class SmsCouponHistoryController {

    private final SmsCouponHistoryService smsCouponHistoryService;

    @Operation(summary = "优惠券领取/使用记录分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponHistory:sms-coupon-history:query')")
    public PageResult<SmsCouponHistoryVO> getSmsCouponHistoryPage(SmsCouponHistoryQuery queryParams ) {
        IPage<SmsCouponHistoryVO> result = smsCouponHistoryService.getSmsCouponHistoryPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增优惠券领取/使用记录")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponHistory:sms-coupon-history:add')")
    public Result<Void> saveSmsCouponHistory(@RequestBody @Valid SmsCouponHistoryForm formData ) {
        boolean result = smsCouponHistoryService.saveSmsCouponHistory(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取优惠券领取/使用记录表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponHistory:sms-coupon-history:edit')")
    public Result<SmsCouponHistoryForm> getSmsCouponHistoryForm(
            @Parameter(description = "优惠券领取/使用记录ID") @PathVariable Long id
    ) {
        SmsCouponHistoryForm formData = smsCouponHistoryService.getSmsCouponHistoryFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改优惠券领取/使用记录")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponHistory:sms-coupon-history:edit')")
    public Result<Void> updateSmsCouponHistory(
            @Parameter(description = "优惠券领取/使用记录ID") @PathVariable Long id,
            @RequestBody @Validated SmsCouponHistoryForm formData
    ) {
        boolean result = smsCouponHistoryService.updateSmsCouponHistory(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除优惠券领取/使用记录")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponHistory:sms-coupon-history:delete')")
    public Result<Void> deleteSmsCouponHistorys(
            @Parameter(description = "优惠券领取/使用记录ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = smsCouponHistoryService.deleteSmsCouponHistorys(ids);
        return Result.judge(result);
    }
}
