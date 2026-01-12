package com.aioveu.sms.aioveu05CouponSpuCategory.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.form.SmsCouponSpuCategoryForm;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.query.SmsCouponSpuCategoryQuery;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.vo.SmsCouponSpuCategoryVO;
import com.aioveu.sms.aioveu05CouponSpuCategory.service.SmsCouponSpuCategoryService;
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
 * @ClassName: SmsCouponSpuCategoryController
 * @Description TODO 优惠券适用的具体分类前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 13:12
 * @Version 1.0
 **/

@Tag(name = "优惠券适用的具体分类接口")
@RestController
@RequestMapping("/api/v1/sms-coupon-spu-category")
@RequiredArgsConstructor
public class SmsCouponSpuCategoryController {


    private final SmsCouponSpuCategoryService smsCouponSpuCategoryService;

    @Operation(summary = "优惠券适用的具体分类分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:query')")
    public PageResult<SmsCouponSpuCategoryVO> getSmsCouponSpuCategoryPage(SmsCouponSpuCategoryQuery queryParams ) {
        IPage<SmsCouponSpuCategoryVO> result = smsCouponSpuCategoryService.getSmsCouponSpuCategoryPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增优惠券适用的具体分类")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:add')")
    public Result<Void> saveSmsCouponSpuCategory(@RequestBody @Valid SmsCouponSpuCategoryForm formData ) {
        boolean result = smsCouponSpuCategoryService.saveSmsCouponSpuCategory(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取优惠券适用的具体分类表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:edit')")
    public Result<SmsCouponSpuCategoryForm> getSmsCouponSpuCategoryForm(
            @Parameter(description = "优惠券适用的具体分类ID") @PathVariable Long id
    ) {
        SmsCouponSpuCategoryForm formData = smsCouponSpuCategoryService.getSmsCouponSpuCategoryFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改优惠券适用的具体分类")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:edit')")
    public Result<Void> updateSmsCouponSpuCategory(
            @Parameter(description = "优惠券适用的具体分类ID") @PathVariable Long id,
            @RequestBody @Validated SmsCouponSpuCategoryForm formData
    ) {
        boolean result = smsCouponSpuCategoryService.updateSmsCouponSpuCategory(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除优惠券适用的具体分类")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:delete')")
    public Result<Void> deleteSmsCouponSpuCategorys(
            @Parameter(description = "优惠券适用的具体分类ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = smsCouponSpuCategoryService.deleteSmsCouponSpuCategorys(ids);
        return Result.judge(result);
    }
}
