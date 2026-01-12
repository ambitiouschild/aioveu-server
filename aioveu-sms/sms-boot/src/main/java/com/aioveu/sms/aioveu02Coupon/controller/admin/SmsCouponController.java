package com.aioveu.sms.aioveu02Coupon.controller.admin;

import com.aioveu.sms.aioveu02Coupon.model.form.SmsCouponForm;
import com.aioveu.sms.aioveu02Coupon.model.vo.SmsCouponVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.sms.aioveu02Coupon.model.query.SmsCouponQuery;
import com.aioveu.sms.aioveu02Coupon.service.SmsCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin-优惠券管理")
@RestController
//@RequestMapping("/api/v1/coupons")
@RequestMapping("/api/v1/sms-coupon")
@RequiredArgsConstructor
public class SmsCouponController {

    private final SmsCouponService smsCouponService;

    @Operation(summary= "优惠券分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCoupon:sms-coupon:query')")
    public PageResult<SmsCouponVO> getSmsCouponPage(SmsCouponQuery queryParams) {
        IPage<SmsCouponVO> result = smsCouponService.getSmsCouponPage(queryParams);
        return PageResult.success(result);
    }

//    @Operation(summary= "优惠券表单数据")
//    @GetMapping("/{couponId}/form_data")
//    public Result<SmsCouponForm> getCouponFormData(@Parameter(name = "优惠券ID") @PathVariable Long couponId) {
//        SmsCouponForm smsCouponForm = smsCouponService.getCouponFormData(couponId);
//        return Result.success(smsCouponForm);
//    }

    @Operation(summary = "获取优惠券表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCoupon:sms-coupon:edit')")
    public Result<SmsCouponForm> getSmsCouponForm(
            @Parameter(description = "优惠券ID") @PathVariable Long id
    ) {
        SmsCouponForm formData = smsCouponService.getSmsCouponFormData(id);
        return Result.success(formData);
    }

    @Operation(summary ="新增优惠券")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCoupon:sms-coupon:add')")
    public Result<Void> saveSmsCoupon(@RequestBody @Valid SmsCouponForm formData) {
        boolean result = smsCouponService.saveSmsCoupon(formData);
        return Result.judge(result);
    }

    @Operation(summary ="修改优惠券")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCoupon:sms-coupon:edit')")
    public Result<Void> updateSmsCoupon(
            @Parameter(description = "优惠券ID") @PathVariable Long id,
            @RequestBody @Validated SmsCouponForm formData
    ) {
        boolean result = smsCouponService.updateSmsCoupon(id, formData);
        return Result.judge(result);
    }

    @Operation(summary= "删除优惠券")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCoupon:sms-coupon:delete')")
    public Result<Void> deleteSmsCoupons(
            @Parameter(description = "优惠券ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = smsCouponService.deleteSmsCoupons(ids);
        return Result.judge(result);
    }
}
