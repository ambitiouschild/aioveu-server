package com.aioveu.sms.aioveu04CouponSpu.controller;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.sms.aioveu04CouponSpu.model.form.SmsCouponSpuForm;
import com.aioveu.sms.aioveu04CouponSpu.model.query.SmsCouponSpuQuery;
import com.aioveu.sms.aioveu04CouponSpu.model.vo.SmsCouponSpuVO;
import com.aioveu.sms.aioveu04CouponSpu.service.SmsCouponSpuService;
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
 * @ClassName: SmsCouponSpuController
 * @Description TODO 优惠券适用的具体商品前端控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 12:19
 * @Version 1.0
 **/

@Tag(name = "优惠券适用的具体商品接口")
@RestController
@RequestMapping("/api/v1/sms-coupon-spu")
@RequiredArgsConstructor
public class SmsCouponSpuController {

    private final SmsCouponSpuService smsCouponSpuService;

    @Operation(summary = "优惠券适用的具体商品分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponSpu:sms-coupon-spu:query')")
    public PageResult<SmsCouponSpuVO> getSmsCouponSpuPage(SmsCouponSpuQuery queryParams ) {
        IPage<SmsCouponSpuVO> result = smsCouponSpuService.getSmsCouponSpuPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增优惠券适用的具体商品")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponSpu:sms-coupon-spu:add')")
    public Result<Void> saveSmsCouponSpu(@RequestBody @Valid SmsCouponSpuForm formData ) {
        boolean result = smsCouponSpuService.saveSmsCouponSpu(formData);
        return Result.judge(result);
    }

    @Operation(summary = "获取优惠券适用的具体商品表单数据")
    @GetMapping("/{id}/form")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponSpu:sms-coupon-spu:edit')")
    public Result<SmsCouponSpuForm> getSmsCouponSpuForm(
            @Parameter(description = "优惠券适用的具体商品ID") @PathVariable Long id
    ) {
        SmsCouponSpuForm formData = smsCouponSpuService.getSmsCouponSpuFormData(id);
        return Result.success(formData);
    }

    @Operation(summary = "修改优惠券适用的具体商品")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponSpu:sms-coupon-spu:edit')")
    public Result<Void> updateSmsCouponSpu(
            @Parameter(description = "优惠券适用的具体商品ID") @PathVariable Long id,
            @RequestBody @Validated SmsCouponSpuForm formData
    ) {
        boolean result = smsCouponSpuService.updateSmsCouponSpu(id, formData);
        return Result.judge(result);
    }

    @Operation(summary = "删除优惠券适用的具体商品")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('aioveuMallSmsCouponSpu:sms-coupon-spu:delete')")
    public Result<Void> deleteSmsCouponSpus(
            @Parameter(description = "优惠券适用的具体商品ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = smsCouponSpuService.deleteSmsCouponSpus(ids);
        return Result.judge(result);
    }
}
